package com.android.santanu.decimalconverter.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import android.annotation.SuppressLint
import com.android.santanu.decimalconverter.R
import com.android.santanu.decimalconverter.data.CustomNumber
import com.android.santanu.decimalconverter.databinding.RecyclerLayoutBinding

class NumberRecyclerAdapter(
    private var data: List<CustomNumber>,
    private val mListener: RecyclerAdapterListener
) : RecyclerView.Adapter<NumberRecyclerAdapter.NumberRecyclerAdapterViewHolder>() {

    private lateinit var mLayout: RecyclerLayoutBinding

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NumberRecyclerAdapterViewHolder {
        mLayout = RecyclerLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NumberRecyclerAdapterViewHolder(mLayout, mListener)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(
        holder: NumberRecyclerAdapterViewHolder, position: Int
    ) = holder.dataBind(data[position], position)

    @SuppressLint("NotifyDataSetChanged")
    fun onRecyclerDataRefresh(data: List<CustomNumber>) {
        this.data = data
        notifyDataSetChanged()
    }

    class NumberRecyclerAdapterViewHolder(
        val layout : RecyclerLayoutBinding,
        private val mListener: RecyclerAdapterListener
    ) : RecyclerView.ViewHolder(layout.root) {
        fun dataBind(data: CustomNumber, position: Int) = with(itemView) {

            layout.tvOutputText.text = data.title
            layout.tvOutputData.text = data.data
            layout.ivCopyData.setOnClickListener {
                mListener.onItemClickedListener(data, position, true)
            }
            layout.root.setOnClickListener {
                mListener.onItemClickedListener(data, position)
            }
        }
    }

    interface RecyclerAdapterListener {
        fun onItemClickedListener(item: CustomNumber, position: Int, isCopy: Boolean = false)

    }
}