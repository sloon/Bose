package hum.swaroop.bose.adapter

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import hum.swaroop.bose.R
import hum.swaroop.bose.entity.City
import javax.inject.Inject

const val CITY_ID = "city_id"

class CitiesAdapter @Inject constructor() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var cities: List<City>? = null

    fun setCities(cities: List<City>?) {
        this.cities = cities
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {
        val textView = LayoutInflater.from(p0.context).inflate(R.layout.item_city, p0, false)
        return CityViewHolder(textView as TextView)
    }

    override fun getItemCount(): Int {
        cities?.let {
            return it.size
        }
        return 0
    }

    override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {
        (p0.itemView as? TextView)?.text = p0.itemView.context.getString(R.string.city_name,
                cities?.get(p1)?.name, cities?.get(p1)?.country)
        p0.itemView.setOnClickListener {
            val activity = p0.itemView.context as? Activity
            val intent = activity?.intent
            intent?.putExtra(CITY_ID, cities?.get(p1)?.id)
            intent?.putExtra("lat", cities?.get(p1)?.coord?.lat)
            intent?.putExtra("lng", cities?.get(p1)?.coord?.lon)
            activity?.setResult(RESULT_OK, intent)
            activity?.finish()
        }
    }

    private class CityViewHolder(view: TextView) : RecyclerView.ViewHolder(view)
}