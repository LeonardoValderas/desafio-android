package com.picpay.desafio.android.presenter

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.picpay.desafio.android.R
import com.picpay.desafio.android.core.data.network.response.StatusResponse
import com.picpay.desafio.android.databinding.ActivityMainBinding
import com.picpay.desafio.android.presenter.adapter.UserListAdapter
import com.picpay.desafio.android.utils.ComponentUtils
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.viewModel

@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: UserViewModel by viewModel()
    private lateinit var adapter: UserListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    override fun onResume() {
        super.onResume()
        initRecyclerView()
        initViewModelObserver()
    }

    private fun initRecyclerView(){
        adapter = UserListAdapter()
        with(binding) {
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }

    private fun initViewModelObserver(){
        with(viewModel) {
            users.observe(this@MainActivity, Observer { resource ->
                when (resource.status) {
                    StatusResponse.SUCCESS -> {
                        resource.data?.let { users ->
                            adapter.users = users
                            progressBarVisibilityHandler(false)
                            recyclerViewVisibilityHandler(true)
                        } ?: run {
                            showError()
                        }
                    }
                    StatusResponse.LOADING -> {
                        progressBarVisibilityHandler(true)
                    }
                    StatusResponse.FAILURE -> {
                        showError()
                    }
                    else -> {
                        showError()
                    }
                }
            })
            getUsers(true)
        }
    }

    private fun showError(){
        ComponentUtils.showToast(this@MainActivity, getString(R.string.error))
        progressBarVisibilityHandler(false)
        recyclerViewVisibilityHandler(false)
    }

    private fun progressBarVisibilityHandler(show: Boolean){
        binding.userListProgressBar.visibility = if (show) View.VISIBLE else  View.GONE
    }

    private fun recyclerViewVisibilityHandler(show: Boolean){
        binding.recyclerView.visibility = if (show) View.VISIBLE else  View.GONE
    }
}
