package coe.project.passtick


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.Navigation
import java.util.zip.Inflater

/**
 * A simple [Fragment] subclass.
 */
class ThankFragment : Fragment() {

    private lateinit var thankView: View
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        thankView = inflater.inflate(R.layout.activity_thanks,container,false)
        val backButton: Button = thankView.findViewById(R.id.back_to_home_button)
        backButton.setOnClickListener { view ->
            Navigation.findNavController(view).navigate(R.id.action_thankFragment_to_homeFragment)


        }
        return thankView
    }


}
