package com.example.wassalniDR.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.wassalniDR.data.Trip

import com.example.wassalniDR.databinding.SupportCardBinding
import com.example.wassalniDR.util.DateUseCase
import com.example.wassalniDR.util.OnItemClickListner

private const val TAG = "TripsAdapter"
class SupportAdapter(private val listener: OnItemClickListner):RecyclerView.Adapter<SupportAdapter.ViewHolder>() {

    private var trips= emptyList<Trip>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SupportAdapter.ViewHolder {
        return ViewHolder(
            SupportCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SupportAdapter.ViewHolder, position: Int) {
        val trip=trips[position]

        holder.binding.startDestnation.text=trip.start
        holder.binding.endDestnation.text=trip.destination
        holder.binding.price.text=trip.price.toString()
        holder.binding.startTime.text= DateUseCase.fromMillisToHhMma(trip.startTime)
        holder.binding.endTime.text= DateUseCase.fromMillisToHhMma(trip.endTime)
        val id=trip.id
        holder.binding.btnSupport.setOnClickListener {
            listener.onSendMessageClick(id,position)
        }
    }

    override fun getItemCount(): Int =trips.size
    fun setData(newTrips:List<Trip>)
    {
        trips=newTrips
        Log.e(TAG, "trips Data: $trips" )
        notifyDataSetChanged()
    }
    class ViewHolder(binding: SupportCardBinding):RecyclerView.ViewHolder(binding.root){
        val binding=binding

    }
}