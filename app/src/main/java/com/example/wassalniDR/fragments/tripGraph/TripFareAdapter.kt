package com.example.wassalniDR.fragments.tripGraph

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.wassalniDR.R
import com.example.wassalniDR.data.Passenger

import com.example.wassalniDR.fragments.tripGraph.placeholder.PlaceholderContent.PlaceholderItem
import com.example.wassalniDR.databinding.FragmentTripFareBinding

/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 * TODO: Replace the implementation with code for your data type.
 */
class TripFareAdapter(
    private val passengers: List<Passenger>
    ,private val tripPrice:Double
) : RecyclerView.Adapter<TripFareAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentTripFareBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val passenger = passengers[position]
        holder.binding.name.append(": ${passenger.name}")
        holder.binding.ticketNum.append(": ${passenger.ticket}")
        holder.binding.seatsNum.append(": ${passenger.numOfSeat}")
        val fare=tripPrice * passenger.numOfSeat
        holder.binding.fare.append(": $fare")

    }

    override fun getItemCount(): Int = passengers.size

    inner class ViewHolder(val binding: FragmentTripFareBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

}