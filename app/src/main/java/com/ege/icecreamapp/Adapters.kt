package com.ege.icecreamapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import kotlinx.android.synthetic.main.item_group_setting.view.*
import kotlinx.android.synthetic.main.item_setting.view.*

class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {}

class AdapterSettings(private val settings: List<Setting>, private val canMultiSelected: Boolean): RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_setting, parent, false))
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        switchSelectedMode(holder.itemView.context, settings[position].isActivation, holder.itemView)
        holder.itemView.setting.text = settings[position].name
        holder.itemView.setOnClickListener {
            if (!canMultiSelected)
                settings.forEachIndexed { index, setting ->  if (setting.isActivation && index != position) { setting.isActivation = false; notifyItemChanged(index)}}
            val isNowActivation = !settings[position].isActivation
            settings[position].isActivation = isNowActivation
            notifyItemChanged(position)
        }
    }
    override fun getItemCount(): Int = settings.size

    private fun switchSelectedMode(context: Context, isSelected: Boolean, view: View){
        view.setting.background = AppCompatResources.getDrawable(context, if (isSelected) R.drawable.shape_accent_button else R.drawable.shape_button)
        (view.setting as TextView).setTextColor(context.getColor(if (isSelected) R.color.black else R.color.colorHint))
    }
}

class AdapterGroupSettings(private val groups: List<GroupSetting>): RecyclerView.Adapter<ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_group_setting, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.title_group_setting.text = groups[position].title
        val flexboxLayoutManager = FlexboxLayoutManager(holder.itemView.context)
        flexboxLayoutManager.flexDirection = FlexDirection.ROW
        flexboxLayoutManager.flexWrap = FlexWrap.WRAP
        holder.itemView.rec_setting.apply {
            adapter = AdapterSettings(groups[position].settings, groups[position].canMultiSelect)
            layoutManager = flexboxLayoutManager
        }
    }

    override fun getItemCount(): Int = groups.size

}