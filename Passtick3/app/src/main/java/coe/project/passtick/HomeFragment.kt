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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {
    private lateinit var shopRecyclerView: RecyclerView
    private lateinit var userRecyclerView: RecyclerView
    private lateinit var shopDatabase: DatabaseReference
    private lateinit var userDatabase: DatabaseReference
    private lateinit var costText: TextView
    private lateinit var reduceText: TextView
    private var shopList = mutableListOf<Shops>()
    private var userList = mutableListOf<Users>()
    private lateinit var homeView: View
    private lateinit var mAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mAuth = FirebaseAuth.getInstance()
        homeView =  inflater.inflate(R.layout.activity_home,container,false)
        costText = homeView.findViewById(R.id.money_num)
        reduceText = homeView.findViewById(R.id.reduce_num)
        shopDatabase = FirebaseDatabase.getInstance().getReference("shop")
        userDatabase = FirebaseDatabase.getInstance().getReference("user")
        shopRecyclerView = homeView.findViewById(R.id.shop_recyclerView)
        userRecyclerView = homeView.findViewById(R.id.user_recyclerView)
        shopRecyclerView.setHasFixedSize(true)
        userRecyclerView.setHasFixedSize(true)
        userRecyclerView.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        shopRecyclerView.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        shopRecyclerView.adapter = MyAdapter(shopList)
        userRecyclerView.adapter = UserAdapter(userList)
        shopDatabase.keepSynced(true)
        userDatabase.keepSynced(true)
        readUserData()
        readShopData()
        val profile = homeView.findViewById<CircleImageView>(R.id.profile_image)
        if(mAuth.currentUser != null){
            val userList = (activity as MainActivity).userList
            val user = userList.find { e -> e.email == mAuth.currentUser!!.email }
            if(user != null){
                Picasso.get().load(user.profile).into(profile)
            } else{
                profile.setImageResource(R.drawable.ic_profile)
            }
        } else {
            profile.setImageResource(R.drawable.ic_profile)
        }

        return homeView
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
                    shopList.sortBy {shop -> shop.pieces}
                    shopList.reverse()
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
                        userList.sortBy { user ->  user.save}
                        userList.reverse()
                        for (j in 0 until userList.size){
                            userList[j].rank = j+1
                        }

                    }
                    userRecyclerView.adapter = UserAdapter(userList)
                }
            }

        }
        userDatabase.addValueEventListener(postListener)
    }
}



