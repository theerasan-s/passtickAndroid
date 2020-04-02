package coe.project.passtick


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.*


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
        val ref : DatabaseReference = FirebaseDatabase.getInstance().getReference("shop")
        shopListRecyclerView = shopView.findViewById(R.id.shop_list_recycleView)
        shopListRecyclerView.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)

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
        val myPlace = LatLng(16.457618,102.8260633)  // this is New York
        map.addMarker(MarkerOptions().position(myPlace).title("My Favorite City"))
        map.moveCamera(CameraUpdateFactory.newLatLng(myPlace))
    }

}
