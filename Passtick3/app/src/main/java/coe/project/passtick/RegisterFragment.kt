package coe.project.passtick


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.Navigation

/**
 * A simple [Fragment] subclass.
 */
class RegisterFragment : Fragment() {
    private lateinit var registerView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        registerView = inflater.inflate(R.layout.fragment_register , container ,false)
        val registerButton: Button = registerView.findViewById<Button>(R.id.button2)
        registerButton.setOnClickListener { view: View ->
            Navigation.findNavController(view).navigate(R.id.action_registerFragment_to_profileFragment)
        }
        return registerView
    }


}
