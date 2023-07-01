package com.example.wassalniDR.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.wassalniDR.data.Trip
import com.example.wassalniDR.databinding.FinishedTripCardBinding
import com.example.wassalniDR.databinding.TripCardBinding
import com.example.wassalniDR.util.DateUseCase

private const val TAG = "FinishedTripAdapter"
class FinishedTripsAdapter: RecyclerView.Adapter<FinishedTripsAdapter.ViewHolder>()
{

    private var trips= emptyList<Trip>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FinishedTripsAdapter.ViewHolder {

        return ViewHolder(
            FinishedTripCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int =trips.size

    fun setData(newTrips:List<Trip>)
    {
        trips=newTrips
        Log.e(TAG, "trips Data: $trips" )
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: FinishedTripsAdapter.ViewHolder, position: Int) {
        val trip=trips[position]
        holder.binding.startDestnation.text=trip.start
        holder.binding.endDestnation.text=trip.destination
        holder.binding.price.text=trip.price.toString()
        holder.binding.startTime.text= DateUseCase.fromMillisToHhMma(trip.startTime)
        holder.binding.endTime.text= DateUseCase.fromMillisToHhMma(trip.endTime)
    }

    inner class ViewHolder(binding: FinishedTripCardBinding) : RecyclerView.ViewHolder(binding.root) {
        val binding=binding
    }
}