package com.antoniomy82.poi_challenge.viewmodel

import android.annotation.SuppressLint
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
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.antoniomy82.poi_challenge.R
import com.antoniomy82.poi_challenge.databinding.FragmentDistrictListBinding
import com.antoniomy82.poi_challenge.databinding.FragmentMapBinding
import com.antoniomy82.poi_challenge.databinding.PopUpPoisDetailBinding
import com.antoniomy82.poi_challenge.model.District
import com.antoniomy82.poi_challenge.model.DistrictListMockUp
import com.antoniomy82.poi_challenge.model.Pois
import com.antoniomy82.poi_challenge.ui.common.DetailFragment
import com.antoniomy82.poi_challenge.ui.districtlist.PoisDistrictListAdapter
import com.antoniomy82.poi_challenge.ui.districtlist.PoisDistrictListFragment
import com.antoniomy82.poi_challenge.ui.homedistrict.HomeDistrictAdapter
import com.antoniomy82.poi_challenge.ui.homedistrict.HomeDistrictFragment
import com.antoniomy82.poi_challenge.ui.map.MapFragment

import com.antoniomy82.poi_challenge.utils.PoisUtils
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
import java.lang.String.format
import java.lang.ref.WeakReference
import java.util.*
import kotlin.system.exitProcess


class PoisViewModel : ViewModel(), OnMapReadyCallback {

    //Main Fragment values
    private var frgMainActivity: WeakReference<Activity>? = null
    private var frgMainContext: WeakReference<Context>? = null
    private var frgMainView: WeakReference<View>? = null
    private var mainBundle: Bundle? = null
    private var fragmentDistrictListBinding: FragmentDistrictListBinding? = null
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


    //Media player
    val playBackProgress = MutableLiveData<Int>()
    val remainingTime = MutableLiveData<String>()
    var popUpBinding: PopUpPoisDetailBinding? = null
    var totalDuration: Long? = null
    private var mediaPlayer: MediaPlayer? = null
    private var myUri: Uri? = null
    private var launchTimer: CountDownTimer? = null
    var popUpLocation: Int = 0


    fun setHomeUI(view: View, activity: FragmentActivity?, context: Context?) {

        //Top bar title
        val headerTitle = view.findViewById<View>(R.id.headerTitle) as TextView
        headerTitle.text = context?.getString(R.string.home_title)

        //Back arrow
        view.findViewById<View>(R.id.headerBack)?.setOnClickListener {
            activity?.finish()
            exitProcess(0)
        }

        //Hardcoded list of district
        val mDistrictList = ArrayList<DistrictListMockUp>()
        mDistrictList.add(DistrictListMockUp("MADRID", "Lavapíes", R.mipmap.ic_madrid))
        mDistrictList.add(DistrictListMockUp("MADRID", "Malasaña", R.mipmap.ic_madrid))
        mDistrictList.add(
            DistrictListMockUp(
                "SEVILLA",
                "Alfalfa - Casco Antiguo",
                R.mipmap.ic_sevilla_round
            )
        )
        mDistrictList.add(
            DistrictListMockUp(
                "SEVILLA",
                "Triana - Los Remedios",
                R.mipmap.ic_sevilla_round
            )
        )
        mDistrictList.add(DistrictListMockUp("BARCELONA", "Gótico", R.mipmap.ic_barcelona_round))
        mDistrictList.add(
            DistrictListMockUp(
                "BARCELONA",
                "Eixample - Gracia",
                R.mipmap.ic_barcelona_round
            )
        )
        mDistrictList.add(DistrictListMockUp("MADRID", "Chueca", R.mipmap.ic_madrid))

        //Set counter
        poisCount.value = mDistrictList.size.toString()

        //Start recyclerView
        context?.let { setHomeRecyclerViewAdapter(mDistrictList, it, view) }
    }


    fun setPoisListUI() {

        //Top bar title
        val headerTitle = frgMainView?.get()?.findViewById<View>(R.id.headerTitle) as TextView
        headerTitle.text = selectedCity

        //Back arrow
        frgMainView?.get()?.findViewById<View>(R.id.headerBack)?.setOnClickListener {
            PoisUtils.replaceFragment(
                HomeDistrictFragment(),
                (frgMainContext?.get() as AppCompatActivity).supportFragmentManager
            )
        }

        fragmentDistrictListBinding?.progressBar?.visibility = View.VISIBLE
        fragmentDistrictListBinding?.mapLayout?.visibility = View.GONE

    }


    @SuppressLint("SetTextI18n")
    fun setMapsUI() {

        //Top bar title
        val headerTitle = frgMapsView?.get()?.findViewById<View>(R.id.headerTitle) as TextView
        headerTitle.text = selectedCity

        //Back arrow
        frgMapsView?.get()?.findViewById<View>(R.id.headerBack)?.setOnClickListener {
            PoisUtils.replaceFragment(
                HomeDistrictFragment(),
                (frgMapsContext?.get() as AppCompatActivity).supportFragmentManager
            )
        }

        if (retrieveDistrict != null) {
            districtTittle.value = retrieveDistrict?.name?.uppercase(Locale.ROOT)

            if (retrieveDistrict?.pois?.size == 0) poisCount.value = "0"
            else poisCount.value = retrieveDistrict?.pois?.size.toString()

        }

        fragmentMapBinding?.poisVM = this //Update the view with databinding
        loadMap()
        map?.let { onMapReady(it) }
    }

    //Set Main fragment parameters in this VM
    fun setDistrictListFragmentBinding(
        frgActivity: Activity,
        frgContext: Context,
        frgView: View,
        mainBundle: Bundle?,
        fragmentDistrictListBinding: FragmentDistrictListBinding,
        position: Int
    ) {
        this.frgMainActivity = WeakReference(frgActivity)
        this.frgMainContext = WeakReference(frgContext)
        this.frgMainView = WeakReference(frgView)
        this.mainBundle = mainBundle
        this.fragmentDistrictListBinding = fragmentDistrictListBinding
        this.position = position
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

    //Set District List RecyclerView
    fun setDistrictListRecyclerViewAdapter(mDistrict: District) {

        val mRecycler: RecyclerView =
            frgMainView?.get()?.findViewById(R.id.rvPois) as RecyclerView
        val recyclerView: RecyclerView = mRecycler
        val manager: RecyclerView.LayoutManager =
            LinearLayoutManager(frgMainActivity?.get()) //Orientation
        recyclerView.layoutManager = manager
        recyclerView.adapter = frgMainContext?.get()?.let {
            PoisDistrictListAdapter(
                this, mDistrict,
                it
            )
        }

        fragmentDistrictListBinding?.progressBar?.visibility = View.GONE
        fragmentDistrictListBinding?.mapLayout?.visibility = View.VISIBLE
    }

    //Set Home RecyclerView
    private fun setHomeRecyclerViewAdapter(
        mDistrictMock: ArrayList<DistrictListMockUp>,
        context: Context,
        view: View
    ) {

        val mRecycler: RecyclerView = view.findViewById(R.id.rvHome) as RecyclerView
        val recyclerView: RecyclerView = mRecycler
        val manager: RecyclerView.LayoutManager = LinearLayoutManager(context) //Orientation
        recyclerView.layoutManager = manager
        recyclerView.adapter = HomeDistrictAdapter(mDistrictMock, context)
    }

    fun setTittleFromAdapter(tittle: String, count: String) {
        districtTittle.value = tittle.uppercase(Locale.ROOT)
        poisCount.value = count

        Log.d("tittleBar", tittle + "count:" + count)
    }

    //Set the POI detail in a popup
    @SuppressLint("DefaultLocale")
    fun popUpDetail(mPoi: Pois?, mContext: Context? = null, popUpBinding: PopUpPoisDetailBinding) {

        //Media Player values
        myUri = Uri.parse(mPoi?.audio?.url.toString()) // initialize Uri here
        mediaPlayer = MediaPlayer.create(frgMainContext?.get(), myUri)
        totalDuration = mediaPlayer?.duration?.toLong()
        val timeValue =
            (format("%tT", totalDuration?.minus(TimeZone.getDefault().rawOffset))).toString()
        remainingTime.value = timeValue

        if (timeValue != "null") popUpBinding.soundLayout.visibility = View.VISIBLE

        popUpBinding.titlePopup.text = mPoi?.name
        popUpBinding.streetPopup.text = mPoi?.description

        //Set image
        if (mPoi?.image?.url != null) {
            frgMainContext?.get()?.let {
                popUpBinding.photoPopup.let { it1 ->
                    Glide.with(it).load(mPoi.image?.url).into(it1)
                }
            }
        }

        //Set icon image
        frgMainContext?.get()?.let {
            popUpBinding.iconPopup.let { it1 ->
                Glide.with(it).load(mPoi?.category?.icon?.url.toString()).into(it1)
            }
        }

        iconCategory = mPoi?.category?.icon?.url.toString()

        //Set likes counter
        if (mPoi?.likesCount == null) popUpBinding.likeQty.text = "0"
        else popUpBinding.likeQty.text = mPoi.likesCount.toString()


        selectedPoi = mPoi
        popUpBinding.vm = this //Update the view with databinding


        popUpBinding.let {
            if (mContext != null) {
                loadMapPopUp(it, mContext)
            } else {
                frgMapsContext?.get()?.let { it1 -> loadMapPopUp(it, it1) }
            }
        }

    }

    /**
     * Maps functions block
     */

    //Load Map fragment
    fun goToMap() {
        PoisUtils.replaceFragment(
            MapFragment(this, selectedCity),
            (frgMainContext?.get() as AppCompatActivity).supportFragmentManager
        )

    }

    //Load Map fragment
    fun goToList() {
        PoisUtils.replaceFragment(
            position?.let { PoisDistrictListFragment(retrieveDistrict, selectedCity, it) },
            (frgMainContext?.get() as AppCompatActivity).supportFragmentManager
        )
    }


    // Allows map styling and theme to be customized.
    private fun setMapStyle(map: GoogleMap) {
        try {
            // Customize the styling of the base map using a JSON object defined, in a raw resource file
            val success = map.setMapStyle(
                frgMapsContext?.get()?.let {
                    MapStyleOptions.loadRawResourceStyle(
                        it,
                        R.raw.map_style
                    )
                }
            )

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


    //Util to use glide into marker
    private fun Marker.loadIcon(context: Context, url: String?) {

        Glide.with(context)
            .asBitmap()
            .load(url)
            .error(R.drawable.location_icon) // to show a default icon in case of any errors
            .listener(object : RequestListener<Bitmap> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Bitmap>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

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
            }).submit()
    }

    fun getVM(): PoisViewModel {
        return this
    }

    fun closePopUp() {
        when (popUpLocation) {
            0 -> PoisUtils.replaceFragment(position?.let {
                PoisDistrictListFragment(
                    retrieveDistrict, selectedCity,
                    it
                )
            }, (frgMainContext?.get() as AppCompatActivity).supportFragmentManager)

            1 -> PoisUtils.replaceFragment(
                MapFragment(this, selectedCity),
                (frgMapsContext?.get() as AppCompatActivity).supportFragmentManager
            )
        }
        buttonStop()
    }

    /**
     * Media player
     */


    @SuppressLint("DefaultLocale")
    fun buttonPlay() {
        popUpBinding?.tvPass?.visibility = View.VISIBLE
        launchTimer = mediaProgress()
        mediaPlayer?.start()
        popUpBinding?.playBtn?.visibility = View.GONE
        popUpBinding?.stopBtn?.visibility = View.VISIBLE
    }


    fun buttonStop() {
        popUpBinding?.tvPass?.text = ""
        playBackProgress.value = 0
        popUpBinding?.playBtn?.visibility = View.VISIBLE
        popUpBinding?.stopBtn?.visibility = View.GONE
        mediaPlayer?.stop()

        mediaPlayer = MediaPlayer.create(frgMainContext?.get(), myUri)
        launchTimer?.cancel()
        playBackProgress.value = 0
        popUpBinding?.vm = getVM() //Update the view with databinding
    }

    @SuppressLint("DefaultLocale")
    fun mediaProgress(): CountDownTimer {

        var counter = 1
        val timer = object : CountDownTimer(totalDuration!!, 1000) {


            @SuppressLint("DefaultLocale")
            override fun onTick(millisUntilFinished: Long) {
                remainingTime.value = (format(
                    "%tT",
                    millisUntilFinished.minus(TimeZone.getDefault().rawOffset)
                )).toString()
                var percentageRemaining = (totalDuration?.toInt())?.div(10000)
                counter++

                if (percentageRemaining != null) {
                    percentageRemaining *= counter * percentageRemaining
                }

                if (percentageRemaining != null) {
                    playBackProgress.value = percentageRemaining

                }
                popUpBinding?.vm = getVM() //Update the view with databinding
            }


            override fun onFinish() {
                playBackProgress.value = 0
                popUpBinding?.tvPass?.text = ""
                popUpBinding?.vm = getVM() //Update the view with databinding
            }

        }
        timer.start()
        return timer
    }

    override fun onMapReady(googleMap: GoogleMap) {

        googleMap.let { map = it }

        googleMap.uiSettings.isZoomControlsEnabled = true //Zoom in/out

        val poisSize = retrieveDistrict?.pois?.size ?: 0
        val zoomLevel = 15f
        var mLatLng: LatLng

        if (!isIntoPopUp) {
            for (i in 0 until poisSize) {

                mLatLng = LatLng(
                    retrieveDistrict?.pois?.get(i)?.latitude!!.toDouble(),
                    retrieveDistrict?.pois?.get(i)?.longitude!!.toDouble()
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

                val mPoi: Pois? =
                    retrieveDistrict?.pois?.find { p -> p.latitude?.toDouble() == it.position.latitude && p.longitude?.toDouble() == it.position.longitude }

                isIntoPopUp = false
                popUpLocation = 1
                PoisUtils.replaceFragment(
                    mPoi?.let { it1 -> DetailFragment(it1, this) },
                    (frgMapsContext?.get() as AppCompatActivity).supportFragmentManager
                )
                true
            }

            map?.let { setMapStyle(it) }


        } else {
            if (selectedPoi != null) {
                frgMainActivity?.get()?.let {
                    map?.addMarker(
                        MarkerOptions().position(
                            LatLng(
                                selectedPoi?.latitude?.toDouble()!!,
                                selectedPoi?.longitude?.toDouble()!!
                            )
                        )
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    )
                }

                map?.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(
                            selectedPoi?.latitude?.toDouble()!!,
                            selectedPoi?.longitude?.toDouble()!!
                        ), zoomLevel
                    )
                )
            }
        }
    }

}


