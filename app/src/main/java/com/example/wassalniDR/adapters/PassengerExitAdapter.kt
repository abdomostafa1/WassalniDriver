package com.example.wassalniDR.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.wassalniDR.R
import com.example.wassalniDR.data.Passenger
import com.example.wassalniDR.databinding.FeeBinding
import com.example.wassalniDR.databinding.PassengerExitItemBinding
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject


class PassengerExitAdapter @Inject constructor(@ActivityContext val context: Context) :
    RecyclerView.Adapter<PassengerExitAdapter.ViewHolder>() {

    private var passengers = emptyList<Passenger>()
    private var tripPrice = 0.0
    private var onClickLeaveBtn: (String) -> Unit = {}


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            PassengerExitItemBinding.inflate(
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
        holder.binding.ticket.text = "${context.getString(R.string.ticket)} ${passenger.ticket}"
        holder.binding.seatsNum.text =
            "${context.getString(R.string.seats_num)} ${passenger.numOfSeat}"

        if (passenger.hasPaid)
            holder.binding.hasPaid.text = context.getString(R.string.paid)
        else
            holder.binding.hasPaid.text = context.getString(R.string.not_paid)

        holder.binding.passsengerLeave.setOnClickListener {
            val id = passenger.id
            if (passenger.hasPaid)
                openPaidDialog(passenger, position)
            else
                openUnPaidDialog(passenger, position)
        }

    }

    private fun openPaidDialog(passenger: Passenger, position: Int) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(context.getString(R.string.passenger_has_paid))
            .setPositiveButton(context.getString(R.string.ok)) { dialog, _ ->
                onClickLeaveBtn.invoke(passenger.id)
                dialog.dismiss()
                passengers = passengers.minusElement(passenger)
                notifyDataSetChanged()
            }
            .setNegativeButton(context.getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun openUnPaidDialog(passenger: Passenger, position: Int) {
        val builder = AlertDialog.Builder(context)
        val binding = FeeBinding.inflate((context as Activity).layoutInflater)
        builder.setView(binding.root)
        val dialog = builder.create()
        binding.fare.text = "${passenger.numOfSeat * tripPrice}"
        binding.okButton.setOnClickListener {
            onClickLeaveBtn.invoke(passenger.id)
            dialog.dismiss()
            passengers = passengers.minusElement(passenger)
            notifyDataSetChanged()
        }
        binding.cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }


    override fun getItemCount(): Int = passengers.size

    fun setData(passengers: List<Passenger>, price: Double) {
        this.passengers = passengers
        tripPrice = price
        notifyDataSetChanged()
    }

    inner class ViewHolder(binding: PassengerExitItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val binding = binding
    }

    fun setOnClickListeners(onLeave: (String) -> Unit) {
        onClickLeaveBtn = onLeave
    }
}