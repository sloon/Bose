package hum.swaroop.bose.activity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import hum.swaroop.bose.BoseApplication
import hum.swaroop.bose.R
import hum.swaroop.bose.adapter.CitiesAdapter
import hum.swaroop.bose.viewmodel.CitiesViewModel
import kotlinx.android.synthetic.main.activity_cities.*
import javax.inject.Inject

class CitiesActivity : AppCompatActivity() {

    @Inject
    lateinit var adapter: CitiesAdapter

    init {
        BoseApplication.component.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cities)

        val linearLayoutManager = LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false)
        list.layoutManager = linearLayoutManager
        list.adapter = adapter
        val dividerItemDecoration = DividerItemDecoration(this,
                linearLayoutManager.orientation)

        list.addItemDecoration(dividerItemDecoration)

        val viewModel = ViewModelProviders.of(this).get(CitiesViewModel::class.java)
        viewModel.getCitiesLiveData().observe(this, Observer { resource ->
            wait.visibility = View.GONE
            resource?.let {
                if (it.success) {
                    adapter.setCities(it.data)
                } else {
                    Toast.makeText(this, it.errorMessage, Toast.LENGTH_SHORT).show()
                }
            }

        })

        viewModel.getCities()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
