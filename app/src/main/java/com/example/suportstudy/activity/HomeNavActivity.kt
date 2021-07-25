package com.example.suportstudy.activity

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.fragment.NavHostFragment
import com.example.suportstudy.R
import kotlinx.android.synthetic.main.activity_home_nav.*

class HomeNavActivity : AppCompatActivity(){
    private lateinit var navController: NavController
    private lateinit var navGraph: NavGraph

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_nav)
        setUpStartDestination()
    }

    private fun setUpStartDestination() {
        val navHostFragment = nav_host_fragment as NavHostFragment
        val graphInflater = navHostFragment.navController.navInflater
        navGraph = graphInflater.inflate(R.navigation.home_nav_navigation)
        navController = navHostFragment.navController
        navGraph.startDestination = R.id.newsFeesFragment
        navController.graph = navGraph
    }

}