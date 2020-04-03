package coe.project.passtick


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_shop.*


/**
 * A simple [Fragment] subclass.
 */
class ShopFragment : Fragment() , OnMapReadyCallback{

    lateinit var shopView: View
    private lateinit var shopListRecyclerView : RecyclerView
    private var  shopList = mutableListOf<Shops>()
    private val ref : DatabaseReference = FirebaseDatabase.getInstance().getReference("shop")
    lateinit var map: GoogleMap
    lateinit var mapView: MapView



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        shopView = inflater.inflate(R.layout.activity_shop , container , false)
        shopListRecyclerView = shopView.findViewById(R.id.shop_list_recycleView)
        shopListRecyclerView.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        val shopCardTitle: TextView = shopView.findViewById(R.id.shop_card_title)
        val phoneNumber: TextView = shopView.findViewById(R.id.phone_number)

        readShopData()


        return shopView

    }



    fun readShopData(){
        val postListener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.w("Error", "loadPost:onCancelled")
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                shopList.clear()
                if(dataSnapshot!!.exists()){
                    for(item in dataSnapshot.children){
                        var shop : Shops? = item.getValue(Shops::class.java)
                        shop!!.shopName = item.key.toString()
                        shopList.add(shop)
                    }
                    shopListRecyclerView.adapter = ShopMapAdapter(shopList,this@ShopFragment)
                }
            }

        }
        ref.addValueEventListener(postListener)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView = view.findViewById(R.id.maps)
        if(mapView != null){
            mapView.onCreate(null)
            mapView.onResume()
            mapView.getMapAsync(this)
        }

    }

    override fun onMapReady(googleMap: GoogleMap?) {
        MapsInitializer.initialize(context)
        map = googleMap!!
        shop_card_title.text = shopList[0].shopName
        phone_number.text = shopList[0].tel
        val firstMapLocation:LatLng = LatLng(shopList[0].lat!!,shopList[0].lng!!)
        map.addMarker(MarkerOptions().position(firstMapLocation).title(shopList[0].shopName))
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(firstMapLocation, 18F),5000,null)
    }

}
