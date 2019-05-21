package ru.ifr0z.searchplacebox.example.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat.START
import androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_LOCKED_CLOSED
import androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_UNLOCKED
import com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.search_place.*
import org.jetbrains.anko.toast
import ru.ifr0z.searchplacebox.example.R
import ru.ifr0z.searchplacebox.example.extension.onEditorAction
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

        val requestStart = getString(R.string.search_place_request_start)
        val requestEnd = getString(R.string.search_place_request_end)
        search_place_et.onEditorAction(
            IME_ACTION_SEARCH, latLngPattern, requestStart, requestEnd
        ) { arrayLatLng ->
            searchPlaceCursorOff()

            val searchPlaceLatitude = arrayLatLng!![0].toDouble()
            val searchPlaceLongitude = arrayLatLng[1].toDouble()

            toast("$searchPlaceLatitude, $searchPlaceLongitude")
        }
        search_place_et.onTextChanges(clear_search_iv)
        clear_search_iv.setOnClickListener {
            search_place_et.text.clear()
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
