package coe.project.passtick


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.user_list.view.*

/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment() {
    private lateinit var profileView : View
    private lateinit var mAuth: FirebaseAuth
    private lateinit var user: Users
    private var userList = mutableListOf<Users>()
    private lateinit var userDatabase: DatabaseReference
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        profileView = inflater.inflate(R.layout.fragment_profile,container,false)
        val username:TextView = profileView.findViewById(R.id.username_profile)
        val reduceNum:TextView = profileView.findViewById(R.id.reduce_num_text)
        userDatabase = FirebaseDatabase.getInstance().getReference("user")
        //val moneyNum:TextView = profileView.findViewById(R.id.money_text)
        val rank:TextView = profileView.findViewById(R.id.rank_num_text)
        val fullname:TextView = profileView.findViewById(R.id.profile_name_text)
        val email:TextView = profileView.findViewById(R.id.profile_email_text)
        val signOutButton: Button = profileView.findViewById(R.id.logout_button)
        val profileImage:ImageView = profileView.findViewById(R.id.profile_image)
        userDatabase.keepSynced(true)
        readUserData()


        // check login
        mAuth = FirebaseAuth.getInstance()
        if(mAuth.currentUser != null){
            user = (activity as MainActivity).userList.find { e -> e.email == mAuth.currentUser!!.email }!!
            if(user != null){
                // set component value
                username.text = user.username
                reduceNum.text = user.save.toString()
                rank.text = user.rank.toString()
                fullname.text = "ชื่อ : ${user.fname} ${user.lname}"
                email.text = "อีเมล : ${user.email}"
                if(user.profile != ""){
                    Picasso.get().load(user.profile).into(profileImage)
                }
                else{
                    profileImage.setImageResource(R.drawable.ic_profile)
                }


            }
        }








        signOutButton.setOnClickListener { view: View ->
            signOut()
            Navigation.findNavController(view).navigate(R.id.action_profileFragment_to_homeFragment)
        }
        return profileView
        }

    private fun signOut(){
        mAuth.signOut()
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
                }
            }

        }
        userDatabase.addValueEventListener(postListener)
    }

    }



