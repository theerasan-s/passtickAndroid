package coe.project.passtick

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import kotlin.math.log

class Home : AppCompatActivity() {
    private lateinit var shopRecyclerView: RecyclerView
    private lateinit var userRecyclerView: RecyclerView
    private lateinit var shopDatabase: DatabaseReference
    private lateinit var userDatabase: DatabaseReference
    private lateinit var costText: TextView
    private lateinit var reduceText: TextView
    private var shopList = mutableListOf<Shops>()
    private var userList = mutableListOf<Users>()


    //var shopName = arrayListOf<String>("shop1","shop2","shop3")
    //var shopImage = arrayListOf<Int>(R.drawable.ic_profile,R.drawable.ic_profile,R.drawable.ic_profile)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


        shopRecyclerView = findViewById(R.id.shop_recyclerView)
        userRecyclerView = findViewById(R.id.user_recyclerView)
        costText = findViewById(R.id.money_num)
        reduceText = findViewById(R.id.reduce_num)
        shopDatabase = FirebaseDatabase.getInstance().getReference("shop")
        userDatabase = FirebaseDatabase.getInstance().getReference("user")
        shopDatabase.keepSynced(true)
        shopRecyclerView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        userRecyclerView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        readUserData()
        readShopData()






    }

    private fun readShopData(){
        val postListener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("Error", "loadPost:onCancelled")
                // ...
            }
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.e("test","testing")
                var allCost:Long = 0
                var allReduce:Long = 0
                if(dataSnapshot!!.exists()){
                    for ( i in dataSnapshot.children) {
                        var shop = i.getValue(Shops::class.java)
                        Log.e("what is I" , i.toString())
                        allCost += shop!!.cost!!
                        allReduce += shop!!.pieces!!

                        shop!!.shopName = i.key.toString()
                        shopList.add(shop!!)
                    }
                    Log.w("LoadData",shopList.toString())
                    costText.text = allCost.toString()
                    reduceText.text = allReduce.toString()
                    shopRecyclerView.adapter = MyAdapter(shopList)
                }
            }
        }
        shopDatabase.addValueEventListener(postListener)
    }

    private fun readUserData() {
        val postListener = object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                Log.w("Error", "loadPost:onCancelled")
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(dataSnapshot!!.exists()) {
                    for(userData in dataSnapshot.children) {
                        var user = userData.getValue(Users::class.java)
                        if(user?.role == "customer"){
                            userList.add(user!!)
                        }

                    }
                    userRecyclerView.adapter = UserAdapter(userList)
                }
            }

        }
        userDatabase.addValueEventListener(postListener)
    }
}
