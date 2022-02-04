package com.ege.icecreamapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_ice_cream.view.*

class IceCreamAdapter(val ice_creams: List<ModelIceCream>): RecyclerView.Adapter<IceCreamAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_ice_cream, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.img_ice_cream.setImageResource(ice_creams[position].imgRes)
    }

    override fun getItemCount(): Int = ice_creams.size
}