package coe.project.passtick

import android.util.Log
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_shop.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
//import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.shop_list.view.*



class ShopMapAdapter( val items: List<Shops>, val shopFragment:ShopFragment) : RecyclerView.Adapter<ShopMapAdapter.ShopHolder>() {


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
        holder.itemView.setOnClickListener {
            val shopLocation = LatLng(shop.lat!!, shop.lng!!)
            shopFragment.shopView.findViewById<TextView>(R.id.shop_card_title).text = shop.shopName
            shopFragment.shopView.findViewById<TextView>(R.id.phone_number).text = shop.tel
            shopFragment.map.addMarker(MarkerOptions().position(shopLocation).title(shop.shopName))
            shopFragment.map.animateCamera(CameraUpdateFactory.newLatLngZoom(shopLocation, 18F),5000,null)

        }
    }

}