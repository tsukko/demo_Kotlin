package jp.co.integrityworks.prototype.ui.contract

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import jp.co.integrityworks.prototype.R
import jp.co.integrityworks.prototype.databinding.InForceContractItemBinding
import jp.co.integrityworks.prototype.db.entity.Insurance

class InForceContractAdapter(private val mInForceContractCallback: InForceContractCallback?) :
        RecyclerView.Adapter<InForceContractAdapter.InForceContractViewHolder>() {

    internal var mInForceContractList: List<Insurance>? = null

    init {
        setHasStableIds(true)
    }

    fun setContactList(inForceContractList: List<Insurance>) {
        if (mInForceContractList == null) {
            mInForceContractList = inForceContractList
            notifyItemRangeInserted(0, inForceContractList.size)
        } else {
            val result = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                override fun getOldListSize(): Int {
                    return mInForceContractList!!.size
                }

                override fun getNewListSize(): Int {
                    return inForceContractList.size
                }

                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    return mInForceContractList!![oldItemPosition].id == inForceContractList[newItemPosition].id
                }

                override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    val newProduct = inForceContractList[newItemPosition]
                    val oldProduct = mInForceContractList!![oldItemPosition]
                    return (newProduct.id == oldProduct.id)
//                            && newProduct.description == oldProduct.description
//                            && newProduct.name == oldProduct.name
//                            && newProduct.price == oldProduct.price)
                }
            })
            mInForceContractList = inForceContractList
            result.dispatchUpdatesTo(this)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InForceContractViewHolder {
        val binding = DataBindingUtil
                .inflate<InForceContractItemBinding>(LayoutInflater.from(parent.context), R.layout.in_force_contract_item,
                        parent, false)
        binding.callback = mInForceContractCallback
        return InForceContractViewHolder(binding)
    }

    override fun onBindViewHolder(holder: InForceContractViewHolder, position: Int) {
        holder.binding.insurance = mInForceContractList!![position]
        when (holder.binding.insurance?.insuranceTypeCode) {
            "automobile" -> holder.binding.contractImageView.setImageResource(R.drawable.automobile)
            "life" -> holder.binding.contractImageView.setImageResource(R.drawable.life)
            "house" -> holder.binding.contractImageView.setImageResource(R.drawable.house)
            "accident" -> holder.binding.contractImageView.setImageResource(R.drawable.accident)
            "etc" -> holder.binding.contractImageView.setImageResource(R.drawable.etc)
            "creditcard" -> holder.binding.contractImageView.setImageResource(R.drawable.creditcard)
            "warranty" -> holder.binding.contractImageView.setImageResource(R.drawable.warranty)
            else -> holder.binding.contractImageView.setImageResource(R.drawable.etc)
        }
        holder.binding.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return if (mInForceContractList == null) 0 else mInForceContractList!!.size
    }

    override fun getItemId(position: Int): Long {
        return mInForceContractList!![position].id.toLong()
    }

    class InForceContractViewHolder(val binding: InForceContractItemBinding) : RecyclerView.ViewHolder(binding.root)
}
