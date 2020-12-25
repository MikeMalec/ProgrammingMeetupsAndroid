package com.example.programmingmeetups.framework.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.programmingmeetups.R
import com.example.programmingmeetups.databinding.ActivityMainBinding
import com.example.programmingmeetups.framework.presentation.auth.AuthViewModel
import com.example.programmingmeetups.framework.presentation.auth.AuthViewModelFactory
import com.example.programmingmeetups.utils.AUTH_FACTORY_IMPL
import com.example.programmingmeetups.utils.extensions.view.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavController.OnDestinationChangedListener, UIController {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    @Named(AUTH_FACTORY_IMPL)
    @Inject
    lateinit var viewModelFactory: AuthViewModelFactory
    lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_ProgrammingMeetups)
        setViewModel()
        setViewBinding()
        setNavController()
        setupActionBar()
        observeToken()
    }

    private fun setViewModel() {
        authViewModel = ViewModelProvider(this, viewModelFactory).get(AuthViewModel::class.java)
    }

    private fun setViewBinding() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setNavController() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController
    }

    private fun setupActionBar() {
        setSupportActionBar(binding.toolbar)
    }

    private var initialSetup = true
    private fun observeToken() {
        lifecycleScope.launchWhenStarted {
            authViewModel.token.observe(this@MainActivity, Observer {
                if(initialSetup) {
                    when (it) {
                        null -> setGraphAndNavigation(false)
                        else -> setGraphAndNavigation(true)
                    }
                }
                initialSetup = false
            })
        }
    }

    private fun setGraphAndNavigation(authenticated: Boolean) {
        val inflater = navController.navInflater
        val graph = inflater.inflate(R.navigation.main_navigation_graph)
        if (!authenticated) {
            graph.startDestination = R.id.authFragment
        } else {
            graph.startDestination = R.id.mapFragment
        }
        navController.setGraph(graph, intent.extras)
        setupNavigation()
        navController.addOnDestinationChangedListener(this)
    }

    private fun setupNavigation() {
        binding.bottomNavigationView.setupWithNavController(navController)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.mapFragment,
                R.id.userEventsFragment,
                R.id.userProfileFragment,
                R.id.authFragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        when (destination.id) {
            R.id.authFragment, R.id.eventFragment, R.id.createEventFragment -> bottomNavigationView.hide()
            else -> bottomNavigationView.show()
        }
    }

    override fun showShortToast(message: String) = shortToast(message)

    override fun showLongToast(message: String) = longToast(message)

    override fun showShortSnackbar(message: String) = shortSnackbar(message)

    override fun showLongSnackbar(message: String) = longSnackbar(message)
}