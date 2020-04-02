package coe.project.passtick


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
//import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.shop_list.view.*


class MyAdapter( val items: List<Shops>) : RecyclerView.Adapter<MyAdapter.ShopHolder>() {


    class ShopHolder(val view: View): RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopHolder {
        return ShopHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.shop_list , parent , false)
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ShopHolder, position: Int) {
        val shop = items[position]
        holder.view.shop_name.text = shop.shopName
        holder.view.shop_profile.setImageResource(R.drawable.ic_profile)
        Picasso.get().load(shop.logo).into(holder.view.shop_profile)
    }

}