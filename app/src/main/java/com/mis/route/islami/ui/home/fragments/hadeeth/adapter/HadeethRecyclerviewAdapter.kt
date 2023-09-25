package com.mis.route.islami.ui.home.fragments.hadeeth.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mis.route.islami.databinding.HadeethItemBinding
import com.mis.route.islami.ui.home.fragments.hadeeth.model.Hadeeth

class HadeethRecyclerviewAdapter(private val hadeethTitlesList: List<String>) : RecyclerView.Adapter<HadeethRecyclerviewAdapter.ViewHolder>() {

    // TODO: take a binding property and pass the root view?
    class ViewHolder(val binding: HadeethItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = HadeethItemBinding.inflate(
            LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.hadeethTitle.text = hadeethTitlesList[position]
        holder.binding.root.setOnClickListener {
            onHadeethClickListener.onClick(position)
        }
    }

    override fun getItemCount() = hadeethTitlesList.size


    private lateinit var onHadeethClickListener: OnHadeethClickListener

    fun setOnHadeethClickListener(listener: OnHadeethClickListener) {
        onHadeethClickListener = listener
    }

    fun interface OnHadeethClickListener {
        fun onClick(position: Int)
    }
}