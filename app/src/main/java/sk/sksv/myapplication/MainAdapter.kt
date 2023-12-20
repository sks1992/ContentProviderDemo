package sk.sksv.myapplication

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import sk.sksv.myapplication.databinding.ViewHolderBinding

class MainAdapter(private val list: List<String>) :
    RecyclerView.Adapter<MainAdapter.MyViewHolder>() {
    inner class MyViewHolder(val viewDataBinding: ViewHolderBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ViewHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val binding = holder.viewDataBinding
        binding.tvName.text = list[position]
    }
}