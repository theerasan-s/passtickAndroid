package coe.project.passtick

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import kotlin.math.log

class Home : AppCompatActivity() {
    private lateinit var shopRecyclerView: RecyclerView
    private lateinit var userRecyclerView: RecyclerView
    private lateinit var shopDatabase: DatabaseReference
    private var shopList = mutableListOf<Shops>()


    //var shopName = arrayListOf<String>("shop1","shop2","shop3")
    //var shopImage = arrayListOf<Int>(R.drawable.ic_profile,R.drawable.ic_profile,R.drawable.ic_profile)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        shopRecyclerView = findViewById(R.id.shop_recyclerView)
        userRecyclerView = findViewById(R.id.user_recyclerView)
        shopDatabase = FirebaseDatabase.getInstance().getReference("shop")
        shopDatabase.keepSynced(true)
        shopRecyclerView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        userRecyclerView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        readShopData()




        /*val shops = listOf<Shops>(
            Shops("Test1", R.drawable.ic_profile),
            Shops("Test2", R.drawable.ic_shop),
            Shops("Test3", R.drawable.ic_profile),
            Shops("Test4", R.drawable.ic_profile),
            Shops("Test5", R.drawable.ic_profile),
            Shops("Test6", R.drawable.ic_profile),
            Shops("Test7", R.drawable.ic_profile),
            Shops("Test8", R.drawable.ic_profile),
            Shops("Test9", R.drawable.ic_profile),
            Shops("Test10", R.drawable.ic_profile)
        )*/
        val users = listOf<Users>(
            Users("Tee1",R.drawable.ic_profile),
            Users("Tee2",R.drawable.ic_profile),
            Users("Tee3",R.drawable.ic_profile),
            Users("Tee4",R.drawable.ic_profile),
            Users("Tee5",R.drawable.ic_profile),
            Users("Tee6",R.drawable.ic_profile),
            Users("Tee7",R.drawable.ic_profile),
            Users("Tee8",R.drawable.ic_profile),
            Users("Tee9",R.drawable.ic_profile),
            Users("Tee10",R.drawable.ic_profile)
        )
        Log.e("users",users.toString())
        userRecyclerView.adapter = UserAdapter(users)



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
                if(dataSnapshot!!.exists()){
                    for ( i in dataSnapshot.children) {
                        var shop = i.getValue(Shops::class.java)
                        Log.e("what is I" , i.toString())
                        shop!!.shopName = i.key.toString()
                        shopList.add(shop!!)
                    }
                    Log.w("LoadData",shopList.toString())
                    shopRecyclerView.adapter = MyAdapter(shopList)
                }
            }
        }
        shopDatabase.addValueEventListener(postListener)
    }
}
