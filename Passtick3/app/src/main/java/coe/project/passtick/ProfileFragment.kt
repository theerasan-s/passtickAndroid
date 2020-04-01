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
class ProfileFragment : Fragment() {
    private lateinit var profileView : View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        profileView = inflater.inflate(R.layout.fragment_profile,container,false)
        val signOutButton: Button = profileView.findViewById(R.id.logout_button)
        signOutButton.setOnClickListener { view: View ->
            Navigation.findNavController(view).navigate(R.id.action_profileFragment_to_homeFragment)
        }
        return profileView
        }
    }


