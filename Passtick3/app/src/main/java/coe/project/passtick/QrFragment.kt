package coe.project.passtick


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
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
    private final var MY_PERMISSIONS_REQUEST_CAMERA: Int = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        QRView =  inflater.inflate(R.layout.activity_qr,container,false)

        if(ContextCompat.checkSelfPermission((activity as MainActivity), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            this.requestPermissions(
                arrayOf(Manifest.permission.CAMERA),
                MY_PERMISSIONS_REQUEST_CAMERA);

        }
        else{
            startCamera()
        }

        return QRView
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Log.d("permission","success")
                    startCamera()
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    Log.d("permission","fail")
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return
            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
                Log.d("permission","what")
            }
        }
    }


    private fun startCamera(){
        Log.d("cameraStart","start camera")
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
                shopName = "Eat At Home"
                findNavController().navigate(QrFragmentDirections.actionQrFragmentToFormFragment(shopName,"Eat At Home"))
            }
            else if(result.getText().toString() == "Mali"){
                zXingScannerView.stopCamera()
                Toast.makeText(activity,"QR = " + result,Toast.LENGTH_LONG).show()
                shopName = "Mali Cakery"
                findNavController().navigate(QrFragmentDirections.actionQrFragmentToFormFragment(shopName ,"Mali Cakery Homemade"))
            }


        }
    }




}



