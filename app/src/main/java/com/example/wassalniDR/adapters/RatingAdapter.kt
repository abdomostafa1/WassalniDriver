package com.example.wassalniDR.adapters

import android.media.Rating
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import androidx.recyclerview.widget.RecyclerView
import com.example.wassalniDR.databinding.RatingCardBinding
import com.example.wassalniDR.databinding.TripCardBinding

private const val TAG = "RatingAdapter"
class RatingAdapter:RecyclerView.Adapter<RatingAdapter.ViewHolder>() {
    var rating= emptyList<com.example.wassalniDR.data.Rating>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RatingAdapter.ViewHolder {
        return ViewHolder(
            RatingCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: RatingAdapter.ViewHolder, position: Int) {
        val rating=rating[position]
        holder.binding.ratingText.text=rating.message
        holder.binding.ratingBar.numStars=rating.ratingAverage
    }

    override fun getItemCount():  Int =rating.size

    fun setData(newRating:List<com.example.wassalniDR.data.Rating>)
    {
        rating=newRating
        Log.e(TAG, "trips Data: $rating" )
        notifyDataSetChanged()
    }

    class ViewHolder(binding: RatingCardBinding) : RecyclerView.ViewHolder(binding.root) {
        val binding=binding
    }
}