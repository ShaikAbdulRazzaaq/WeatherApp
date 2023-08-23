package com.razzaaq.weatherApp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.razzaaq.weatherApp.R
import com.razzaaq.weatherApp.utils.Utils
import com.razzaaq.weatherApp.data.dto.GetForecastApiResponseDTO
import com.razzaaq.weatherApp.databinding.ItemWeatherBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ForeCastWeatherRecycler : RecyclerView.Adapter<ForeCastWeatherRecycler.ForeCastViewHolder>() {
    class ForeCastViewHolder(val binding: ItemWeatherBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForeCastViewHolder =
        ForeCastViewHolder(
            ItemWeatherBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: ForeCastViewHolder, position: Int) {
        val item = differ.currentList[position]
        item?.apply {
            holder.binding.apply {
                val context = holder.itemView.context
                tvDescription.text = weather?.firstOrNull()?.description
                tvHumidity.text = holder.itemView.context.getString(
                    R.string.humidity, main?.humidity
                )
                tvTime.text = dt?.let {
                    SimpleDateFormat(
                        "dd MMM yyyy HH:mm", Locale.getDefault()
                    ).format(Date(it * 1000))
                }
                tvTemp.text = buildString {
                    append(main?.temp?.toInt())
                    append("°C")
                }
                tvWind.text = context.getString(
                    R.string.wind, wind?.speed
                )
                tvPressure.text = context.getString(
                    R.string.pressure, main?.pressure
                )
                tvFeelsLike.text = context.getString(
                    R.string.feels_like_, main?.feelsLike?.toInt()
                )

                tvVisibility.text = context.getString(
                    R.string.visibility, visibility?.div(1000)
                )
                val link = weather?.firstOrNull()?.icon?.let {
                    Utils.returnImageUrlFromCode(
                        it
                    )
                }
                link?.let {
                    ivIcon.load(link)
                }
            }
        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<GetForecastApiResponseDTO.Items>() {
        override fun areItemsTheSame(
            oldItem: GetForecastApiResponseDTO.Items, newItem: GetForecastApiResponseDTO.Items
        ): Boolean = oldItem.dt == newItem.dt


        override fun areContentsTheSame(
            oldItem: GetForecastApiResponseDTO.Items, newItem: GetForecastApiResponseDTO.Items
        ): Boolean = oldItem == newItem

    }
    val differ = AsyncListDiffer(this, diffCallback)
}