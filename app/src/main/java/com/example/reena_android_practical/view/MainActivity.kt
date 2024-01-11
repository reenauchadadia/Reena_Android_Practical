package com.example.reena_android_practical.view

import CatImageAdapter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.reena_android_practical.R
import com.example.reena_android_practical.databinding.ActivityMainBinding
import com.example.reena_android_practical.model.CatImageModel
import com.example.reena_android_practical.retrofit.APIClient
import com.example.reena_android_practical.utils.Common
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    private var common: Common? = Common()
    private lateinit var binding: ActivityMainBinding
    private var isGrid: Boolean = false;
    private var catImages: ArrayList<CatImageModel>? = ArrayList<CatImageModel>();
    private var adapter: CatImageAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        adapter = CatImageAdapter(catImages!!)
        setupRecyclerView()
        fetchCatImages()
        implementPagination()
    }


    private fun implementPagination() {

        // Implement pagination
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                var layoutManager = if (isGrid) {
                    recyclerView.layoutManager as GridLayoutManager
                } else {
                    recyclerView.layoutManager as LinearLayoutManager
                }

                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

//                Log.e("Visible::", "visibleItemCount: $visibleItemCount" )
//                Log.e("Visible::", "totalItemCount: $totalItemCount" )
//                Log.e("Visible::", "firstVisibleItemPosition: $firstVisibleItemPosition" )

                if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0) {
                    // Load more data when reaching the end of the list
//                    Log.e("Visible::", "onScrolled Called: ")
                    fetchCatImages()
                }
            }
        })
    }

    private fun fetchCatImages() {
        val call = common?.api_key?.let {
            APIClient.catApiService.getCatImages(it, 10)
        }

        call?.enqueue(object : Callback<ArrayList<CatImageModel>> {
            override fun onResponse(
                call: Call<ArrayList<CatImageModel>>,
                response: Response<ArrayList<CatImageModel>>
            ) {
                if (response.isSuccessful) {
                    val images = response.body() ?: ArrayList()
                    catImages?.addAll(images)
                    adapter?.notifyDataSetChanged()
                    binding.recyclerView.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.GONE
                } else {
                    // Handle API call failure
                }
            }

            override fun onFailure(call: Call<ArrayList<CatImageModel>>, t: Throwable) {
                // Handle failure
            }
        })
    }

    private fun setupRecyclerView() {

        if (isGrid) {
            binding.recyclerView.layoutManager = GridLayoutManager(this, 2)

        } else {
            binding.recyclerView.layoutManager = LinearLayoutManager(this)
        }
        binding.recyclerView.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.grid_menu -> {
                if (isGrid) {
                    item.setIcon(R.drawable.baseline_grid_on)
                    isGrid = false
                } else {
                    item.setIcon(R.drawable.baseline_grid_off)
                    isGrid = true
                }

                setupRecyclerView()
            }

        }
        return super.onOptionsItemSelected(item)
    }
}