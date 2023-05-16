package com.example.wassalniDR.adapters

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.wassalniDR.R
import com.example.wassalniDR.data.Passenger
import com.example.wassalniDR.databinding.PassengerEntryItemBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.dialog.MaterialDialogs
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PassengerEntryAdapter @Inject constructor(@ActivityContext val context: Context) :
    RecyclerView.Adapter<PassengerEntryAdapter.ViewHolder>() {

    private var passengers = emptyList<Passenger>()
    private var onClickAttendBtn: (String) -> Unit = {}
    private var onClickNotAttendBtn: (String) -> Unit = {}
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            PassengerEntryItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val passenger = passengers[position]
        holder.binding.name.text = passenger.name
        holder.binding.ticket.text="${context.getString(R.string.ticket)} ${passenger.ticket}"
        holder.binding.seatsNum.text="${context.getString(R.string.seats_num)} ${passenger.numOfSeat}"

        holder.binding.attend.setOnClickListener {
            val id=passenger.id
            passengers=passengers.minusElement(passenger)
            notifyDataSetChanged()
            onClickAttendBtn.invoke(id)
        }
        holder.binding.notAttend.setOnClickListener {
            openDialog(passenger, position)
        }
    }

    private fun openDialog(passenger: Passenger, position: Int) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(context.getString(R.string.are_you_sure))
            .setPositiveButton(context.getString(R.string.yes)) { dialog, _ ->
                onClickNotAttendBtn.invoke(passenger.id)
                dialog.dismiss()
                passengers=passengers.minusElement(passenger)
                notifyDataSetChanged()
            }
            .setNegativeButton(context.getString(R.string.yes)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    override fun getItemCount(): Int = passengers.size

    fun setData(passengers: List<Passenger>) {
        this.passengers = passengers
        notifyDataSetChanged()
    }

    inner class ViewHolder(binding: PassengerEntryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val binding = binding
    }

    fun setOnClickListeners(onAttend: (String) -> Unit, onNotAttend: (String) -> Unit) {
        onClickAttendBtn = onAttend
        onClickNotAttendBtn = onNotAttend
    }
}