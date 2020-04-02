package coe.project.passtick


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

/**
 * A simple [Fragment] subclass.
 */
class LoginFragment : Fragment() {

    lateinit var loginView: View
    private lateinit var userDatabase: DatabaseReference
    private var userList = mutableListOf<Users>()
    private lateinit var mAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        loginView = inflater.inflate(R.layout.fragment_login , container , false)
        userDatabase = FirebaseDatabase.getInstance().getReference("user")
        val registerButton: Button = loginView.findViewById(R.id.to_register_button)
        val loginButton: Button = loginView.findViewById(R.id.login_button)
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


    }



