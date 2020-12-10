package com.me.theswitcher_ruioliveira

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView

class ItemRecyclerViewAdapter(
    private val parentActivity: ItemListActivity,
    private val values: House,
    private val twoPane: Boolean
) : RecyclerView.Adapter<ItemRecyclerViewAdapter.ViewHolder>() {

    private val onClickListener: View.OnClickListener
    private val onLongListener: View.OnLongClickListener
    private val onChangeListener: CompoundButton.OnCheckedChangeListener

    init {
        onClickListener = View.OnClickListener { v ->
            val item = v.tag as Division
            if (twoPane) {
                val fragment = ItemDetailFragment().apply {
                    arguments = Bundle().apply {
                        putString(ItemDetailFragment.ARG_ITEM_DIVISION, item.division)
                        putBoolean(ItemDetailFragment.ARG_ITEM_STATE, item.state)
                    }
                }
                parentActivity.supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.item_detail_container, fragment)
                    .commit()
            } else {
                val intent = Intent(v.context, ItemDetailActivity::class.java).apply {
                    putExtra(ItemDetailFragment.ARG_ITEM_DIVISION, item.division)
                    putExtra(ItemDetailFragment.ARG_ITEM_STATE, item.state)
                }
                v.context.startActivity(intent)
            }
        }

        onLongListener = View.OnLongClickListener { v ->
            val item = v.tag as Division

            onCreateDeleteElementDialog(item).show()
            return@OnLongClickListener true
        }

        onChangeListener = CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            val item = buttonView.tag as Division

            parentActivity.setChangeState(item, isChecked)

            if (twoPane) {
                val fragment = ItemDetailFragment().apply {
                    arguments = Bundle().apply {
                        putString(ItemDetailFragment.ARG_ITEM_DIVISION, item.division)
                        putBoolean(ItemDetailFragment.ARG_ITEM_STATE, item.state)
                    }
                }
                parentActivity.supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.item_detail_container, fragment)
                    .commit()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list_content, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values.divisions[position]
        holder.division.text = item.division
        holder.switchBtn.isChecked = item.state

        with(holder.itemView) {
            tag = item
            setOnClickListener(onClickListener)
            setOnLongClickListener(onLongListener)
        }

        with(holder.switchBtn) {
            tag = item
            holder.switchBtn.setOnCheckedChangeListener(onChangeListener)
        }

    }

    override fun getItemCount() = values.divisions.size

    fun onCreateDeleteElementDialog(item: Division): Dialog {
        return parentActivity.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)
            builder.setMessage(parentActivity.getString(R.string.delete_division, item.division))
                .setPositiveButton(R.string.delete,
                    DialogInterface.OnClickListener { dialog, id ->
                        parentActivity.deleteDivision(item)
                        removeCurrentFragment()
                    })
                .setNegativeButton(R.string.cancel,
                    DialogInterface.OnClickListener { dialog, id ->
                        dialog.cancel()
                    })
            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    fun removeCurrentFragment() {

        if (twoPane) {
            parentActivity.supportFragmentManager.findFragmentById(R.id.item_detail_container)?.let {
                parentActivity.supportFragmentManager.beginTransaction().remove(
                    it
                ).commit()
            };
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val division: TextView = view.findViewById(R.id.division)
        val switchBtn: Switch = view.findViewById(R.id.switch_btn)
    }
}