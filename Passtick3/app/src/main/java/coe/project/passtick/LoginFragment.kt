package coe.project.passtick


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.facebook.*
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.*


/**
 * A simple [Fragment] subclass.
 */
class LoginFragment : Fragment() {

    lateinit var loginView: View
    private lateinit var userDatabase: DatabaseReference
    private var userList = mutableListOf<Users>()
    private lateinit var mAuth: FirebaseAuth
    lateinit var callbackManager: CallbackManager
    lateinit var  mGoogleSignInClient: GoogleSignInClient
    val RC_SIGN_IN: Int = 1


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        loginView = inflater.inflate(R.layout.fragment_login , container , false)
        userDatabase = FirebaseDatabase.getInstance().getReference("user")
        val registerButton: Button = loginView.findViewById(R.id.to_register_button)
        val loginButton: Button = loginView.findViewById(R.id.login_button)
        val facebookButton = loginView.findViewById<LoginButton>(R.id.fb_login_button)
        val myFaceboookButton = loginView.findViewById<Button>(R.id.login_facebook_button)
        //val googleButton: SignInButton = loginView.findViewById(R.id.google_sign_in_button)
        val myGoogleButton: Button = loginView.findViewById(R.id.login_gmail_button)
        /// Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(activity as MainActivity,gso)


        myFaceboookButton.setOnClickListener {
            facebookButton.performClick()
        }


        myGoogleButton.setOnClickListener {
            signInGoogle()
        }


        callbackManager = (activity as MainActivity).callbackManager
        facebookButton.fragment = this
        facebookButton.setPermissions("email", "public_profile")
        facebookButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                Log.d("success", "facebook:onSuccess:$loginResult")
                handleFacebookAccessToken(loginResult.accessToken)
            }

            override fun onCancel() {
                Log.d("cancel", "facebook:onCancel")
                // ...
            }

            override fun onError(error: FacebookException) {
                Log.d("error", "facebook:onError", error)
                // ...
            }
        })
        // ...







        mAuth = FirebaseAuth.getInstance()
        readUserData()
        if(mAuth.currentUser != null){
            Log.d("checkLogIn","loggedIn")
            (activity as MainActivity).navController.navigate(R.id.action_loginFragment_to_profileFragment)
        }
        else{
            Log.d("checkLogin","not LoggedIn")
        }

        registerButton.setOnClickListener{view: View ->
            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_registerFragment)
        }
        loginButton.setOnClickListener { view: View ->
            val username = loginView.findViewById<EditText>(R.id.user_name_login)
            val password = loginView.findViewById<EditText>(R.id.password_login)
            login(username,password,view)

        }

        return loginView
    }

    private fun login(username:EditText,password:EditText,view: View){
        val user = username.text.toString().trim{it <= ' '}
        val pass = password.text.toString().trim{it <= ' '}
        Log.d("checkUser",userList.toString())
        val loginUser = userList.find {e -> e.username.toString() == user && e.password == pass}
        if(loginUser!=null){
            Log.d("loginUser",loginUser.toString())
            mAuth.signInWithEmailAndPassword(loginUser.email.toString(),loginUser.password.toString()).addOnCompleteListener {task ->
                if (!task.isSuccessful){
                    Log.d("loginError","got some login error")
                } else {
                    Toast.makeText(context,"ok",Toast.LENGTH_SHORT).show()
                    Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_profileFragment)
                }
            }
        }
    }

    private fun readUserData() {
        Log.d("hellotest" , "tmanranger3")
        val postListener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.w("Error", "loadPost:onCancelled")
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.d("please" ,"what is going on")
                if(dataSnapshot!!.exists()) {
                    userList.clear()
                    for(userData in dataSnapshot.children) {
                        val user = userData.getValue(Users::class.java)
                        if(user?.role == "customer"){
                            userList.add(user!!)
                        }
                    }
                }
            }

        }
        userDatabase.addValueEventListener(postListener)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("tee1","yes")
        // Pass the activity result back to the Facebook SDK
        callbackManager.onActivityResult(requestCode, resultCode, data)

        // Google SignIn
        if(requestCode == RC_SIGN_IN){
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w("googleSignIn", "Google sign in failed", e)
                // ...
            }
        }

    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        Log.d("facebook", "handleFacebookAccessToken:$token")

        val credential = FacebookAuthProvider.getCredential(token.token)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener((activity as MainActivity)) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("facebook", "signInWithCredential:success")
                    val user = mAuth.currentUser
                    if(userList.find { e -> e.email == user!!.email } != null){
                        (activity as MainActivity).navController.navigate(R.id.action_loginFragment_to_profileFragment)
                    }
                    else{
                        val fname = user!!.displayName!!.split(" ")[0]
                        val lname = user!!.displayName!!.split(" ")[1]
                        val newUser:Users = Users(user!!.displayName,user.email,"",fname,lname,user.photoUrl.toString()+"?width=1000","customer",0)
                        createUser(newUser)

                    }
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("facebook", "signInWithCredential:failure", task.exception)
                    Toast.makeText(context, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()

                }

                // ...
            }
    }

    private fun createUser(user:Users){
        userDatabase.push().setValue(user).continueWith {
            (activity as MainActivity).navController.navigate(R.id.action_loginFragment_to_profileFragment)
        }
    }

    private fun signInGoogle(){
        val signIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signIntent,RC_SIGN_IN)
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Log.d("googleSignIn", "firebaseAuthWithGoogle:" + acct.id!!)

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(activity as MainActivity) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("googleSignIn", "signInWithCredential:success")
                    val user = mAuth.currentUser
                    if(userList.find { e -> e.email == user!!.email } != null){
                        (activity as MainActivity).navController.navigate(R.id.action_loginFragment_to_profileFragment)
                    }
                    else{
                        val fname = user!!.displayName!!.split(" ")[0]
                        val lname = user!!.displayName!!.split(" ")[1]
                        val newUser:Users = Users(user!!.displayName,user.email,"",fname,lname,user.photoUrl.toString(),"customer",0)
                        createUser(newUser)

                    }

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("googleSignIn", "signInWithCredential:failure", task.exception)

                }

                // ...
            }
    }








}



