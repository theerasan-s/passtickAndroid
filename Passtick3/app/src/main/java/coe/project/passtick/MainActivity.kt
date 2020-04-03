package coe.project.passtick

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentController
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.facebook.CallbackManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.IO_PARALLELISM_PROPERTY_NAME
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class MainActivity : AppCompatActivity() {
    private lateinit var bottomNavigation : BottomNavigationView
    lateinit var navController: NavController
    private lateinit var userDatabase: DatabaseReference
    lateinit var callbackManager: CallbackManager
    var userList = mutableListOf<Users>()
    var logedIn: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        callbackManager = CallbackManager.Factory.create()
        userDatabase = FirebaseDatabase.getInstance().getReference("user")
        readUserData()
        userDatabase.keepSynced(true)
        bottomNavigation = findViewById(R.id.bottom_navigation)
        navController = Navigation.findNavController(this,R.id.nav_host_fragment)
        NavigationUI.setupWithNavController(bottomNavigation,navController)


    }



    private fun readUserData() {
        Log.d("hello" , "tmanranger2")
        val postListener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.w("Error", "loadPost:onCancelled")
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.d("test","testing")
                var allCost:Long = 0
                var allReduce:Long = 0
                userList.clear()
                if(dataSnapshot!!.exists()){
                    for ( i in dataSnapshot.children) {
                        var user = i.getValue(Users::class.java)
                        if(user!!.role == "customer"){
                            user.key = i.key.toString()
                            userList.add(user!!)
                        }
                    }
                    userList.sortBy { user ->  user.save}
                    userList.reverse()
                    for (j in 0 until userList.size){
                        userList[j].rank = j+1
                    }

                }
            }
        }
        userDatabase.addValueEventListener(postListener)
    }
}
