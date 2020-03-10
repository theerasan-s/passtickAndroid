package coe.project.passtick


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

/**
 * A simple [Fragment] subclass.
 */
class ShopFragment : Fragment() {

    private lateinit var shopView: View
    private lateinit var shopListRecyclerView : RecyclerView
    private var  shopList = mutableListOf<Shops>()
    private val ref : DatabaseReference = FirebaseDatabase.getInstance().getReference("shop")


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
                    shopListRecyclerView.adapter = MyAdapter(shopList)
                }
            }

        }
        ref.addValueEventListener(postListener)
    }

}
