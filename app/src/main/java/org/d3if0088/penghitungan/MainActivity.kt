package org.d3if0088.penghitungan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import org.d3if0088.penghitungan.core.network.ApiConfig
import org.d3if0088.penghitungan.core.network.NetworkService
import retrofit2.create

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navController = findNavController(R.id.myNavHostFragment)
        NavigationUI.setupActionBarWithNavController(this, navController)
        initNetwork()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }

    private fun initNetwork(){
        val retrofit = ApiConfig.retrofitService()
        retrofit.create(NetworkService::class.java)
    }
}