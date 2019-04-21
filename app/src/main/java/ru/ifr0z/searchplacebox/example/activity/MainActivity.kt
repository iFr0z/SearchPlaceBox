package ru.ifr0z.searchplacebox.example.activity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener
import android.support.v4.view.GravityCompat.START
import android.support.v4.widget.DrawerLayout.LOCK_MODE_LOCKED_CLOSED
import android.support.v4.widget.DrawerLayout.LOCK_MODE_UNLOCKED
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.search_place.*
import org.jetbrains.anko.toast
import ru.ifr0z.searchplacebox.example.R
import ru.ifr0z.searchplacebox.example.extension.onTextChanges

class MainActivity : AppCompatActivity(), OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userInterface()
    }

    private fun userInterface() {
        initToggleToolbar()

        searchPlace()
    }

    private fun initToggleToolbar() {
        setSupportActionBar(search_place_toolbar)
        val toggle = ActionBarDrawerToggle(
            this, drawer_l, search_place_toolbar, R.string.app_name, R.string.app_name
        )
        drawer_l.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
    }

    private fun searchPlace() {
        search_place_et.setOnTouchListener { _, _ ->
            searchPlaceCursorOn()
            false
        }
        search_place_et.setOnEditorActionListener(TextView.OnEditorActionListener { _, id, _ ->
            if (id == IME_ACTION_SEARCH) {
                searchPlaceCursorOff()

                if (search_place_et.text.matches(latLngPattern.toRegex())) {
                    val lat = getString(R.string.search_place_latitude)
                    val long = getString(R.string.search_place_longitude)

                    val arrayLatLng = search_place_et.text.split(",".toRegex())
                    val searchPlaceLatitude = arrayLatLng[0].toDouble()
                    val searchPlaceLongitude = arrayLatLng[1].toDouble()

                    toast("$lat $searchPlaceLatitude\n$long $searchPlaceLongitude")
                } else {
                    val requestStart = getString(R.string.search_place_request_start)
                    val requestEnd = getString(R.string.search_place_request_end)

                    toast("$requestStart '${search_place_et.text}' $requestEnd")
                }
                return@OnEditorActionListener true
            }
            true
        })
        search_place_et.onTextChanges { sequence ->
            when {
                sequence!!.isNotEmpty() -> {
                    clear_search_iv.animate().alpha(1.0f).setListener(
                        object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator) {
                                clear_search_iv.visibility = View.VISIBLE
                                clear_search_iv.setOnClickListener {
                                    search_place_et.setText("")
                                }
                            }
                        }
                    )
                }
                sequence.isEmpty() -> {
                    clear_search_iv.animate().alpha(0.0f).setListener(
                        object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator) {
                                clear_search_iv.visibility = View.INVISIBLE
                            }
                        }
                    )
                }
            }
        }
    }

    private fun searchPlaceCursorOn() {
        search_place_et.isCursorVisible = true

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        search_place_toolbar.setNavigationOnClickListener {
            searchPlaceCursorOff()
        }

        drawer_l.setDrawerLockMode(LOCK_MODE_LOCKED_CLOSED)
    }

    private fun searchPlaceCursorOff() {
        val inputMethodManager = this.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(search_place_et.windowToken, 0)

        search_place_et.isCursorVisible = false

        supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        initToggleToolbar()

        drawer_l.setDrawerLockMode(LOCK_MODE_UNLOCKED)
    }

    override fun onBackPressed() {
        val stateDrawer = drawer_l.isDrawerOpen(START)
        val stateCursor = search_place_et.isCursorVisible
        when {
            stateDrawer -> drawer_l.closeDrawer(START)
            stateCursor -> searchPlaceCursorOff()
            !stateDrawer || !stateCursor -> super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_camera -> {}
            R.id.nav_gallery -> {}
            R.id.nav_slideshow -> {}
            R.id.nav_manage -> {}
            R.id.nav_share -> {}
            R.id.nav_send -> {}
        }

        drawer_l.closeDrawer(START)
        return true
    }

    companion object {
        //  Regular expression for matching coordinates: https://stackoverflow.com/a/18690202
        const val latLngPattern = "(^[-+]?(?:[1-8]?\\d(?:\\.\\d+)?|90(?:\\.0+)?))," +
                "\\s*([-+]?(?:180(?:\\.0+)?|(?:(?:1[0-7]\\d)|(?:[1-9]?\\d))(?:\\.\\d+)?))\$"
    }
}
