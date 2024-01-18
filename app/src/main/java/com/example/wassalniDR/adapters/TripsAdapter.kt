package com.example.wassalniDR.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.wassalniDR.data.Trip


import com.example.wassalniDR.databinding.TripCardBinding
import com.example.wassalniDR.fragments.TripsFragmentDirections
//import com.example.wassalniDR.fragments.TripsFragmentDirections
import com.example.wassalniDR.util.DateUseCase
import okhttp3.internal.immutableListOf

private const val TAG = "SupportAdapter"
class TripsAdapter:RecyclerView.Adapter<TripsAdapter.ViewHolder>()
{

    private var trips= emptyList<Trip>()
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
        holder.binding.startTime.text=DateUseCase.fromMillisToHhMma(trip.startTime)
        holder.binding.endTime.text=DateUseCase.fromMillisToHhMma(trip.endTime)
        holder.binding.day.text=DateUseCase.fromMillisToPatternddMMyyyy(trip.startTime)
        val id=trip.id
        holder.itemView.setOnClickListener {
            val action=TripsFragmentDirections.actionTripsFragmentToTripGraph(id)
            it.findNavController().navigate(action)

        }


    }

    override fun getItemCount(): Int =trips.size
    fun setData(newTrips:List<Trip>)
    {
        trips=newTrips
        Log.e(TAG, "trips Data: $trips" )
        notifyDataSetChanged()
    }
    inner class ViewHolder(binding: TripCardBinding) : RecyclerView.ViewHolder(binding.root) {
        val binding=binding
    }
}