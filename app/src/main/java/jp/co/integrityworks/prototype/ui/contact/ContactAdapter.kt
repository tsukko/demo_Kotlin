package jp.co.integrityworks.prototype.ui.contact

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import jp.co.integrityworks.prototype.R
import jp.co.integrityworks.prototype.databinding.ContactItemBinding
import jp.co.integrityworks.prototype.db.entity.Person

class ContactAdapter(private val mContactInfoCallback: ContactInfoCallback?) :
        RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

    internal var mContactList: List<Person>? = null

    init {
        setHasStableIds(true)
    }

    fun setContactList(contactList: List<Person>) {
        if (mContactList == null) {
            mContactList = contactList
            notifyItemRangeInserted(0, contactList.size)
        } else {
            val result = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                override fun getOldListSize(): Int {
                    return mContactList!!.size
                }

                override fun getNewListSize(): Int {
                    return contactList.size
                }

                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    return mContactList!![oldItemPosition].id == contactList[newItemPosition].id
                }

                override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    val newProduct = contactList[newItemPosition]
                    val oldProduct = mContactList!![oldItemPosition]
                    return (newProduct.id == oldProduct.id)
//                            && newProduct.description == oldProduct.description
//                            && newProduct.name == oldProduct.name
//                            && newProduct.price == oldProduct.price)
                }
            })
            mContactList = contactList
            result.dispatchUpdatesTo(this)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val binding = DataBindingUtil
                .inflate<ContactItemBinding>(LayoutInflater.from(parent.context), R.layout.contact_item,
                        parent, false)
        binding.callback = mContactInfoCallback
        return ContactViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.binding.person = mContactList!![position]
        holder.binding.executePendingBindings()
    }

    override fun getItemCount(): Int {

        return if (mContactList == null) 0 else mContactList!!.size
    }

    override fun getItemId(position: Int): Long {
        return mContactList!![position].id.toLong()
    }

    class ContactViewHolder(val binding: ContactItemBinding) : RecyclerView.ViewHolder(binding.root)
}
