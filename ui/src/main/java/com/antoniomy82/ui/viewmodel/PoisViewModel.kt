package com.antoniomy82.ui.viewmodel

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.antoniomy82.data.model.District
import com.antoniomy82.data.model.Pois
import com.antoniomy82.mycities.ui.R
import com.antoniomy82.mycities.ui.databinding.FragmentMapBinding
import com.antoniomy82.mycities.ui.databinding.PopUpPoisDetailBinding
import com.antoniomy82.ui.detail.DetailFragment
import com.antoniomy82.ui.districtlist.PoisDistrictListFragment
import com.antoniomy82.ui.getTimeResult
import com.antoniomy82.ui.homedistrict.HomeDistrictFragment
import com.antoniomy82.ui.map.MapFragment
import com.antoniomy82.ui.mediaProgress
import com.antoniomy82.ui.replaceFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import java.lang.ref.WeakReference
import kotlin.properties.Delegates


class PoisViewModel : ViewModel(), OnMapReadyCallback {

    //Main Fragment values
    lateinit var frgMainContext: Context

    private var mainBundle: Bundle? = null
    private var position: Int? = 0

    //Main fragment values
    val districtTittle = MutableLiveData<String>()
    val poisCount = MutableLiveData<String>().also { it.value = "0" }

    //Maps Fragment values
    private var frgMapsActivity: WeakReference<Activity>? = null
    private var frgMapsContext: WeakReference<Context>? = null
    private var frgMapsView: WeakReference<View>? = null
    private var fragmentMapBinding: FragmentMapBinding? = null
    private var mapsBundle: Bundle? = null

    //Global values
    var retrieveDistrict: District? = null
    private var mapView: WeakReference<MapView>? = null
    private var map: GoogleMap? = null
    private var isIntoPopUp: Boolean = false
    private var selectedPoi: Pois? = null
    private var iconCategory: String? = null
    private var listContext: WeakReference<Context>? = null
    var selectedCity: String = ""
    private lateinit var timeValue: String

    //Media player
    val remainingTime = MutableLiveData<String>()
    var popUpBinding: PopUpPoisDetailBinding? = null
    var totalDuration by Delegates.notNull<Long>()
    private var mediaPlayer: MediaPlayer? = null
    private var myUri: Uri? = null
    private var launchTimer: CountDownTimer? = null
    var popUpLocation: Int = 0


    fun setMapsUI() {
        //Top bar title
        val headerTitle = frgMapsView?.get()?.findViewById<View>(R.id.headerTitle) as TextView
        headerTitle.text = selectedCity

        //Back arrow
        frgMapsView?.get()?.findViewById<View>(R.id.headerBack)?.setOnClickListener {
            //TODO : Cities Navigator
           goToHome()
        }

        if (retrieveDistrict != null) {
            districtTittle.value = retrieveDistrict?.name?.uppercase()

            if (retrieveDistrict?.pois?.size == 0) poisCount.value = "0"
            else poisCount.value = retrieveDistrict?.pois?.size.toString()

        }

        fragmentMapBinding?.poisVM = this //Update the view with dataBinding
        loadMap()
        map?.let { onMapReady(it) }
    }

    //Set Maps fragment parameters in this VM
    fun setMapsFragmentBinding(
        frgActivity: Activity,
        frgContext: Context,
        frgView: View,
        fragmentMapBinding: FragmentMapBinding,
        mapsBundle: Bundle?
    ) {
        this.frgMapsActivity = WeakReference(frgActivity)
        this.frgMapsContext = WeakReference(frgContext)
        this.frgMapsView = WeakReference(frgView)
        this.fragmentMapBinding = fragmentMapBinding
        this.mapsBundle = mapsBundle
    }

    //Set the POI detail in a popup
    fun popUpDetail(mPoi: Pois?, mContext: Context? = null, popUpBinding: PopUpPoisDetailBinding) {

        //Media Player values
        myUri = Uri.parse(mPoi?.audio?.url.toString()) // initialize Uri here
        mediaPlayer = MediaPlayer.create(frgMainContext, myUri)
        totalDuration = mediaPlayer?.duration?.toLong() ?: 0
        timeValue = getTimeResult(totalDuration)
        remainingTime.value = timeValue

        if (timeValue != "null") popUpBinding.soundLayout.visibility = View.VISIBLE

        popUpBinding.titlePopup.text = mPoi?.name
        popUpBinding.streetPopup.text = mPoi?.description

        //Set image
        if (mPoi?.image?.url != null) {
            frgMainContext.let {
                popUpBinding.photoPopup.let { it1 ->
                    Glide.with(it).load(mPoi.image?.url).into(it1)
                }
            }
        }

        //Set icon image
        frgMainContext.let {
            popUpBinding.iconPopup.let { it1 ->
                Glide.with(it).load(mPoi?.category?.icon?.url.toString()).into(it1)
            }
        }

        iconCategory = mPoi?.category?.icon?.url.toString()

        //Set likes counter
        if (mPoi?.likesCount == null) popUpBinding.likeQty.text = "0"
        else popUpBinding.likeQty.text = mPoi.likesCount.toString()

        selectedPoi = mPoi
        popUpBinding.vm = this //Update the view with dataBinding

        popUpBinding.let {
            if (mContext != null) {
                loadMapPopUp(it, mContext)
            } else {
                frgMapsContext?.get()?.let { it1 -> loadMapPopUp(it, it1) }
            }
        }
    }

    //TODO : Cities Navigator
    private fun goToHome() =  replaceFragment(HomeDistrictFragment(), (frgMapsContext?.get() as AppCompatActivity).supportFragmentManager)
    fun goToMap() = replaceFragment(MapFragment(this, selectedCity), (frgMainContext as AppCompatActivity).supportFragmentManager)

    fun goToList() = replaceFragment(position?.let { PoisDistrictListFragment(retrieveDistrict, selectedCity, it) }, (frgMainContext as AppCompatActivity).supportFragmentManager)

    private fun goToDetail(mPoi: Pois?) = replaceFragment(mPoi?.let { it1 -> DetailFragment(it1, this) }, (frgMapsContext?.get() as AppCompatActivity).supportFragmentManager)

    fun closePopUp() {
        when (popUpLocation) {
            //TODO : Cities Navigator
            0 -> replaceFragment(position?.let { PoisDistrictListFragment(retrieveDistrict, selectedCity, it) }, (frgMainContext as AppCompatActivity).supportFragmentManager)

            1 -> replaceFragment(MapFragment(this, selectedCity), (frgMapsContext?.get() as AppCompatActivity).supportFragmentManager)
        }
        buttonStop()
    }


    // Allows map styling and theme to be customized.
    private fun setMapStyle(map: GoogleMap) {
        try {
            // Customize the styling of the base map using a JSON object defined, in a raw resource file
            val success = map.setMapStyle(
                frgMapsContext?.get()
                    ?.let { MapStyleOptions.loadRawResourceStyle(it, R.raw.map_style) })

            if (!success) Log.e("__MAP", "Style parsing failed.")

        } catch (e: Resources.NotFoundException) {
            Log.e("__MAP", "Can't find style. Error: ", e)
        }
    }

    //Map fragment
    private fun loadMap() {
        mapView = WeakReference(frgMapsView?.get()?.findViewById(R.id.map) as MapView)
        mapView?.get()?.onCreate(mapsBundle)
        mapView?.get()?.onResume()
        mapView?.get()?.getMapAsync(this)
        isIntoPopUp = false
    }

    //Map into PopUp
    private fun loadMapPopUp(popUpBinding: PopUpPoisDetailBinding, mContext: Context) {
        mapView = WeakReference(popUpBinding.mapPopup)
        mapView?.get()?.onCreate(mainBundle)
        mapView?.get()?.onResume()
        mapView?.get()?.getMapAsync(this)
        listContext = WeakReference(mContext)
        isIntoPopUp = true
    }


    fun getVM(): PoisViewModel = this


    //Media player

    fun buttonPlay() {
        launchTimer = mediaProgress(totalDuration, this)
        mediaPlayer?.start()

        popUpBinding?.apply {
            tvPass.visibility = View.VISIBLE
            playBtn.visibility = View.GONE
            stopBtn.visibility = View.VISIBLE
        }
    }

    fun buttonStop() {
        mediaPlayer?.stop()
        mediaPlayer = MediaPlayer.create(frgMainContext, myUri)
        launchTimer?.cancel()

        popUpBinding?.apply {
            tvPass.text = timeValue
            playBtn.visibility = View.VISIBLE
            stopBtn.visibility = View.GONE
            vm = getVM() //Update the view with dataBinding
        }
    }

    /**
     * Map
     */

    //Util to use glide into marker
    private fun Marker.loadIcon(context: Context, url: String?) {

        Glide.with(context)
            .asBitmap()
            .load(url)
            .error(R.drawable.location_icon) // to show a default icon in case of any errors
            .listener(object : RequestListener<Bitmap> {


                override fun onResourceReady(
                    resource: Bitmap?,
                    model: Any?,
                    target: Target<Bitmap>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    return resource?.let {
                        BitmapDescriptorFactory.fromBitmap(it)
                    }?.let {
                        setIcon(it)
                        true
                    } ?: false
                }

                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Bitmap>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }
            }).submit()
    }


    override fun onMapReady(googleMap: GoogleMap) {

        googleMap.let { map = it }
        googleMap.uiSettings.isZoomControlsEnabled = true //Zoom in/out

        val poisSize = retrieveDistrict?.pois?.size ?: 0
        val zoomLevel = 15f
        var mLatLng: LatLng

        when (isIntoPopUp) {
            false -> {
                for (i in 0 until poisSize) {
                    mLatLng = LatLng(
                        retrieveDistrict?.pois?.get(i)?.latitude?.toDouble() ?: 0.0,
                        retrieveDistrict?.pois?.get(i)?.longitude?.toDouble() ?: 0.0
                    )

                    val mMarker: Marker? = map?.addMarker(MarkerOptions().position(mLatLng))

                    frgMapsContext?.get()?.let {
                        mMarker?.loadIcon(
                            it,
                            retrieveDistrict?.pois?.get(i)?.category?.marker?.url.toString()
                        )
                    }
                    map?.moveCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, zoomLevel))
                }

                googleMap.setOnMarkerClickListener {
                    it.position.latitude
                    val mPoi: Pois? = retrieveDistrict?.pois?.find { p -> p.latitude?.toDouble() == it.position.latitude && p.longitude?.toDouble() == it.position.longitude }
                    isIntoPopUp = false
                    popUpLocation = 1
                    //TODO : Cities Navigator
                    goToDetail(mPoi)

                    true
                }
                map?.let { setMapStyle(it) }
            }

            true -> {
                selectedPoi.let {
                    frgMainContext.let {
                        map?.addMarker(
                            MarkerOptions().position(
                                LatLng(
                                    selectedPoi?.latitude?.toDouble() ?: 0.0,
                                    selectedPoi?.longitude?.toDouble() ?: 0.0
                                )
                            )
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                        )
                    }

                    map?.moveCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            LatLng(
                                selectedPoi?.latitude?.toDouble() ?: 0.0,
                                selectedPoi?.longitude?.toDouble() ?: 0.0
                            ), zoomLevel
                        )
                    )
                }
            }
        }//When
    }

}