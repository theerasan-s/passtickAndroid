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
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

/**
 * A simple [Fragment] subclass.
 */
class RegisterFragment : Fragment() {
    private lateinit var registerView: View
    private lateinit var mAuth: FirebaseAuth
    private lateinit var userDatabase: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        registerView = inflater.inflate(R.layout.fragment_register , container ,false)
        mAuth = FirebaseAuth.getInstance()
        userDatabase = FirebaseDatabase.getInstance().getReference("user")
        val registerButton: Button = registerView.findViewById<Button>(R.id.button2)
        val username: EditText = registerView.findViewById(R.id.user_name_register)
        val password: EditText = registerView.findViewById(R.id.password_register)
        val confirmPassword: EditText = registerView.findViewById(R.id.password_register_confirm)
        val fname: EditText = registerView.findViewById(R.id.fname_register)
        val lname: EditText = registerView.findViewById(R.id.lname_register)
        val email: EditText = registerView.findViewById(R.id.email_register)
        registerButton.setOnClickListener { view: View ->
            if(password.text.toString() == confirmPassword.text.toString()){
                val user = Users(username.text.toString(),email.text.toString(),
                    password!!.text.toString(),fname.text.toString(),lname.text.toString()
                    ,"https://firebasestorage.googleapis.com/v0/b/passtick2.appspot.com/o/profile%2Fprofile.png?alt=media&token=fc4c6e8d-829f-4e70-b49f-590e7246fcdf"
                    ,"customer",0)
                register(user,view)
            } else {
                Toast.makeText(context,"ยืนยันรหัสผ่านไม่ถูกต้อง",Toast.LENGTH_SHORT).show()
            }
        }


        return registerView
    }

    private fun register(user:Users,view:View){
        Log.d("loggingIn","id:${user.email} pass:${user.password}")
        mAuth.createUserWithEmailAndPassword(user.email!!.toString(),user.password!!).addOnCompleteListener { task ->
            if (!task.isSuccessful){
                Log.d("loginError",task.exception.toString())
            } else {
                Toast.makeText(context,"ok",Toast.LENGTH_SHORT).show()
                createUser(user, view)

            }
        }

    }

    private fun createUser(user:Users,view: View){
        userDatabase.push().setValue(user).continueWith {
            Navigation.findNavController(view).navigate(R.id.action_registerFragment_to_profileFragment)
        }
    }



}
