package coe.project.passtick


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.NavController
import androidx.navigation.Navigation

/**
 * A simple [Fragment] subclass.
 */
class LoginFragment : Fragment() {

    lateinit var loginView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        loginView = inflater.inflate(R.layout.fragment_login , container , false)
        val registerButton: Button = loginView.findViewById(R.id.to_register_button)
        val loginButton: Button = loginView.findViewById(R.id.login_button)
        registerButton.setOnClickListener{view: View ->
            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_registerFragment)
        }
        loginButton.setOnClickListener { view: View ->
            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_profileFragment)
        }
        return loginView
    }


    }



