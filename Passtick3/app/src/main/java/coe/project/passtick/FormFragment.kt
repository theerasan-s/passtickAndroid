package coe.project.passtick


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf

/**
 * A simple [Fragment] subclass.
 */
class FormFragment : Fragment() {

    private lateinit var FormView : View
    private lateinit var shopNameTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        FormView = inflater.inflate(R.layout.activity_form, container, false)
        shopNameTextView = FormView.findViewById(R.id.shop_name)
        arguments?.let {
            val args = FormFragmentArgs.fromBundle(arguments!!)
            val shopName =  args.shopName
            shopNameTextView.text = shopName
        }

        //var args = FormFragmentArgs.fromBundle(arguments)
        return FormView
        }
    }



