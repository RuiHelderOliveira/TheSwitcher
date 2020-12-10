package com.me.theswitcher_ruioliveira

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar


/**
 * An activity representing a list of Pings. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a [ItemDetailActivity] representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
class ItemListActivity : AppCompatActivity() {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private var twoPane: Boolean = false

    private val viewModel: HouseViewModel by viewModels()

    companion object {
        private const val TAG = "ItemListActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_list)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.title = title

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            onCreateAddElementDialog().show()
        }

        if (findViewById<NestedScrollView>(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            twoPane = true
        }

        viewModel.houseResultLiveData.observe(this) { houseResult ->

            if (houseResult == null) {
                Log.i(TAG, "Error on load divisions")
            } else {
                val divisions: House = houseResult

                setupRecyclerView(findViewById(R.id.item_list), divisions)
            }
        }

        viewModel.loadDivisions()
    }

    fun onCreateAddElementDialog(): Dialog {
        return this.let {
            val builder = AlertDialog.Builder(it)
            // Get the layout inflater

            val inflater = layoutInflater;
            val view = inflater.inflate(R.layout.dialog_add_element, null)
            val divisionTXT = view.findViewById<EditText>(R.id.elemnt)
            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            builder.setView(view)
                // Add action buttons
                .setPositiveButton(R.string.add,
                    DialogInterface.OnClickListener { dialog, id ->
                        // sign in the user ...
                        addDivision(divisionTXT.text.toString())
                    })
                .setNegativeButton(R.string.cancel,
                    DialogInterface.OnClickListener { dialog, id ->
                        dialog.cancel()
                    })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun setupRecyclerView(recyclerView: RecyclerView, house: House) {

        recyclerView.addItemDecoration(
            DividerItemDecoration(
                recyclerView.context,
                DividerItemDecoration.VERTICAL
            )
        )

        recyclerView.adapter = ItemRecyclerViewAdapter(this, house, twoPane)
    }

    fun setChangeState(item: Division, checked: Boolean) {

        viewModel.ChangeItem(item, checked)
    }

    fun addDivision(division: String) {
        viewModel.addItem(division)
    }

    fun deleteDivision(item: Division) {
        viewModel.deleteItem(item)
    }
}