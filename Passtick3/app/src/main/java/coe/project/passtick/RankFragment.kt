package coe.project.passtick


import android.graphics.Color
import android.os.Bundle
import android.provider.CalendarContract
import android.text.style.BackgroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TabHost
import android.widget.TabWidget
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTabHost
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_rank.*


/**
 * A simple [Fragment] subclass.
 */
class RankFragment : Fragment() {

    private lateinit var tabHost: TabHost
    private lateinit var shopRecyclerView: RecyclerView
    private lateinit var userRecyclerView: RecyclerView
    private lateinit var shopDatabase: DatabaseReference
    private lateinit var userDatabase: DatabaseReference
    //private lateinit var costText: TextView
    //private lateinit var reduceText: TextView
    private var shopList = mutableListOf<Shops>()
    private var userList = mutableListOf<Users>()
    private lateinit var rankView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        rankView =  inflater.inflate(R.layout.activity_rank,container,false)
        shopDatabase = FirebaseDatabase.getInstance().getReference("shop")
        userDatabase = FirebaseDatabase.getInstance().getReference("user")

        tabHost = rankView.findViewById(R.id.tab_host);
        tabHost.setup()

        var spec = tabHost.newTabSpec("ผู้ใช้")
        spec.setContent(R.id.user_rank)
        spec.setIndicator("ผู้ใช้")
        tabHost.addTab(spec)

        spec = tabHost.newTabSpec("ร้าน")
        spec.setContent(R.id.shop_rank)
        spec.setIndicator("ร้าน")
        tabHost.addTab(spec)




        shopRecyclerView = rankView.findViewById(R.id.shop_rank_recycleview)
        userRecyclerView = rankView.findViewById(R.id.user_rank_recycleview)
        shopRecyclerView.setHasFixedSize(true)
        userRecyclerView.setHasFixedSize(true)
        userRecyclerView.layoutManager = LinearLayoutManager(context,
            LinearLayoutManager.VERTICAL,false)
        shopRecyclerView.layoutManager = LinearLayoutManager(context,
            LinearLayoutManager.VERTICAL,false)
        shopRecyclerView.adapter = ShopRank(shopList)
        userRecyclerView.adapter = UserRank(userList)
        shopDatabase.keepSynced(true)
        userDatabase.keepSynced(true)
        userRecyclerView.adapter = UserRank(userList)

        readUserData()
        readShopData()

        return rankView
        //return inflater.inflate(R.layout.activity_rank,container,false)
        }

    private fun readShopData(){
        Log.d("hello2" , "tmanranger")
        val postListener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("Error", "loadPost:onCancelled")
                // ...
            }
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.d("test","testing")
                var allCost:Long = 0
                var allReduce:Long = 0
                shopList.clear()
                if(dataSnapshot!!.exists()){
                    for ( i in dataSnapshot.children) {
                        var shop = i.getValue(Shops::class.java)
                        Log.d("what is I" , i.toString())
                        allCost += shop!!.cost!!
                        allReduce += shop!!.pieces!!

                        shop!!.shopName = i.key.toString()
                        shopList.add(shop!!)
                    }
                    Log.w("LoadData",shopList.toString())
                    shopList.sortBy {shop -> shop.pieces}
                    shopList.reverse()
                    for(j in 0 until shopList.size) {
                        shopList[j].rank = j + 1
                    }
                    shopRecyclerView.adapter = ShopRank(shopList)
                }

            }
        }
        shopDatabase.addValueEventListener(postListener)
    }

    private fun readUserData() {
        Log.d("hello" , "tmanranger2")
        val postListener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.w("Error", "loadPost:onCancelled")
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.d("please" ,"what is going on")
                if(dataSnapshot!!.exists()) {
                    userList.clear()
                    for(userData in dataSnapshot.children) {
                        var user = userData.getValue(Users::class.java)
                        if(user?.role == "customer"){
                            userList.add(user!!)
                        }

                    }
                    userList.sortBy { user ->  user.save}
                    userList.reverse()
                    for(j in 0 until userList.size) {
                        userList[j].rank = j + 1
                    }
                    userRecyclerView.adapter = UserRank(userList)
                }
            }

        }
        userDatabase.addValueEventListener(postListener)
    }
    }



