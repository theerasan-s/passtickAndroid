package coe.project.passtick


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.fragment.findNavController
import me.dm7.barcodescanner.zxing.ZXingScannerView

/**
 * A simple [Fragment] subclass.
 */
class QrFragment : Fragment() {


    private lateinit var zXingScannerView : ZXingScannerView
    private lateinit var QRView : View
    private lateinit var scanLayout : FrameLayout
    lateinit var shopName : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        QRView =  inflater.inflate(R.layout.activity_qr,container,false)
        scanLayout = QRView.findViewById(R.id.qrLayout)
        zXingScannerView = ZXingScannerView(activity as MainActivity)
        scanLayout.addView(zXingScannerView)
        zXingScannerView.startCamera()
        zXingScannerView.setAutoFocus(true)

        zXingScannerView.setResultHandler {
            result -> result.getText().toString()

            Log.d("qrResult",result.text.toString())

            if (result.getText().toString() == "Eat At Home"){
                zXingScannerView.stopCamera()
                Toast.makeText(activity,"QR = " + result,Toast.LENGTH_LONG).show()
                shopName = "Eat at Home"
                findNavController().navigate(QrFragmentDirections.actionQrFragmentToFormFragment(shopName,"Eat At Home"))
            }
            else if(result.getText().toString() == "Mali"){
                zXingScannerView.stopCamera()
                Toast.makeText(activity,"QR = " + result,Toast.LENGTH_LONG).show()
                shopName = "Mali Cakery"
                findNavController().navigate(QrFragmentDirections.actionQrFragmentToFormFragment(shopName ,"Mali Cakery Homemade"))
            }

        }
        return QRView
    }





}



