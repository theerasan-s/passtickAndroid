package coe.project.passtick


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.Date;

/**
 * A simple [Fragment] subclass.
 */
class FormFragment : Fragment() {

    private lateinit var formView : View
    private lateinit var shopNameTextView: TextView
    private lateinit var spinner: Spinner
    private lateinit var mAuth: FirebaseAuth
    private lateinit var ref: String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        formView = inflater.inflate(R.layout.activity_form, container, false)
        shopNameTextView = formView.findViewById(R.id.shop_name)
        mAuth = FirebaseAuth.getInstance()
        spinner = formView.findViewById(R.id.amount_spinner)
        val amountOfPlastic = arrayOf("1","2","3","4","5","6","7","8","9","10")
        val arrayAdapter = ArrayAdapter(formView.context,android.R.layout.simple_list_item_1,amountOfPlastic)
        val sendButton: Button = formView.findViewById(R.id.send_button)
        spinner.adapter = arrayAdapter
        arguments?.let {
            val args = FormFragmentArgs.fromBundle(arguments!!)
            val shopName =  args.shopName
            ref = args.ref
            shopNameTextView.text = shopName
        }
        sendButton.setOnClickListener {view ->
            val dbRef = FirebaseDatabase.getInstance().getReference("request/$ref")
            if(mAuth.currentUser != null){
                val userList = (activity as MainActivity).userList
                val user = userList.find { e -> e.email == mAuth.currentUser!!.email }
                if(user != null){
                    dbRef.push().setValue(Request(user!!.username.toString(),user.profile.toString(),user.key,Date().time,spinner.selectedItem.toString().toInt())).continueWith {
                        Navigation.findNavController(view).navigate(R.id.action_formFragment_to_thankFragment)
                    }
                }

            } else {
                dbRef.push().setValue(Request(time = Date().time, pieces = spinner.selectedItem.toString().toInt())).continueWith{
                    Navigation.findNavController(view).navigate(R.id.action_formFragment_to_thankFragment)
                }
            }
        }

        //var args = FormFragmentArgs.fromBundle(arguments)
        return formView
        }
    }



