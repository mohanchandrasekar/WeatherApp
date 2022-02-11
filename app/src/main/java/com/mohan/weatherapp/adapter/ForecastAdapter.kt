package com.mohan.weatherapp.adapter

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.mohan.weatherapp.R
import com.mohan.weatherapp.data.Country
import timber.log.Timber


class ForecastAdapter(forecastData: List<Country>) : RecyclerView.Adapter<ForecastAdapter.ViewHolder?>() {
    private var parentRecycler: RecyclerView? = null
    private val data: List<Country> = forecastData
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        parentRecycler = recyclerView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val v: View = inflater.inflate(R.layout.item_city_card, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val iconTint: Int =
            ContextCompat.getColor(holder.itemView.context, R.color.grayIconTint)
        val country: Country = data[position]
        Glide.with(holder.itemView.context)
            .load(country.cityIcon)
            .listener(TintOnLoad(holder.imageView, iconTint))
            .into(holder.imageView)
        holder.textView.text = country.cityName
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val imageView: ImageView = itemView.findViewById(R.id.city_image)
        val textView: TextView = itemView.findViewById(R.id.city_name)
        fun showText() {
            val parentHeight: Int = (imageView.parent as View).height
            val scale: Float =
                (parentHeight - textView.height.toFloat()) / imageView.height.toFloat()
            imageView.pivotX = imageView.width * 0.5f
            imageView.pivotY = 0f
            imageView.animate().scaleX(scale)
                .withEndAction {
                    textView.visibility = View.VISIBLE
                    imageView.setColorFilter(Color.BLACK)
                }
                .scaleY(scale).setDuration(200)
                .start()
        }

        fun hideText() {
            imageView.setColorFilter(
                ContextCompat.getColor(
                    imageView.context,
                    R.color.grayIconTint
                )
            )
            textView.visibility = View.INVISIBLE
            imageView.animate().scaleX(1f).scaleY(1f)
                .setDuration(200)
                .start()
        }

        override fun onClick(v: View) {
            parentRecycler?.smoothScrollToPosition(adapterPosition)
        }

        init {
            itemView.findViewById<View>(R.id.container).setOnClickListener(this)
        }
    }

    private class TintOnLoad(view: ImageView, private val tintColor: Int) : RequestListener<Drawable?> {
        private val imageView: ImageView = view

        override fun onResourceReady(
            resource: Drawable?,
            model: Any?,
            target: Target<Drawable?>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            imageView.setColorFilter(tintColor)
            return false
        }

        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Drawable?>?,
            isFirstResource: Boolean
        ): Boolean {
            return false
        }
    }

}