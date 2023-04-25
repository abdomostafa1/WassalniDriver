package com.example.wassalniDR.adapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.wassalniDR.data.Trips
import com.example.wassalniDR.databinding.TripCardBinding
import com.example.wassalniDR.fragments.TripsFragmentDirections
import com.example.wassalniDR.util.DateUseCase
import okhttp3.internal.immutableListOf


class TripsAdapter:RecyclerView.Adapter<TripsAdapter.ViewHolder>()
{

    private var trips= immutableListOf<Trips>()



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            TripCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val trip=trips[position]
        holder.binding.startDestnation.text=trip.start
        holder.binding.endDestnation.text=trip.destination
        holder.binding.price.text=trip.price.toString()
        holder.binding.startTime.text=DateUseCase.fromMillisToString1(trip.startTime)
        holder.binding.endTime.text=DateUseCase.fromMillisToString1(trip.endTime)
        val id=trip.id
        holder.itemView.setOnClickListener {
            val action=TripsFragmentDirections.actionTripsFragment2ToTripDetailsFragment(id)
            it.findNavController().navigate(action)

        }


    }

    override fun getItemCount(): Int =trips.size
    fun setData(newTrips:List<Trips>)
    {
        trips=newTrips
        notifyDataSetChanged()
    }
    inner class ViewHolder(binding: TripCardBinding) : RecyclerView.ViewHolder(binding.root) {
        val binding=binding
    }
}