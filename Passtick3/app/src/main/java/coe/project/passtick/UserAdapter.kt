package coe.project.passtick

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.user_list.view.*

class UserAdapter(val userList: List<Users>): RecyclerView.Adapter<UserAdapter.UserHolder>(){
    class UserHolder (val view : View): RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserHolder {
        return  UserHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.user_list, parent ,false)
        )
    }

    override fun getItemCount(): Int {
        return  userList.size
    }

    override fun onBindViewHolder(holder: UserHolder, position: Int) {
        val users = userList[position]
        holder.view.user_name_text.text = users.username
        if(users.profile != ""){
            Picasso.get().load(users.profile).into(holder.view.user_profile)
        }
        else{
            holder.view.user_profile.setImageResource(R.drawable.ic_profile)
        }


    }
}