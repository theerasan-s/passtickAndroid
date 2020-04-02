package coe.project.passtick


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
//import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.shop_list.view.*
import kotlinx.android.synthetic.main.shop_list.view.shop_name
import kotlinx.android.synthetic.main.shop_rank.view.*

class ShopRank (val shopList: List<Shops>) : RecyclerView.Adapter<ShopRank.ShopHolder>() {


    class ShopHolder(val view: View): RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopHolder {
        return ShopHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.shop_rank , parent , false)
        )
    }

    override fun getItemCount(): Int {
        return shopList.size
    }

    override fun onBindViewHolder(holder: ShopHolder, position: Int) {
        val shop = shopList[position]
        holder.view.shop_rank_number.text = shop.rank.toString()
        holder.view.shop_name_rank.text = shop.shopName
        holder.view.shop_piece.text = "ลดพลาสติก " + shop.pieces.toString() + " ชิ้น"
        holder.view.shop_profile_rank.setImageResource(R.drawable.ic_profile)
        Picasso.get().load(shop.logo).into(holder.view.shop_profile_rank)
    }

}