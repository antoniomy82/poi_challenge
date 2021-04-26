package com.antoniomy82.poi_challenge.viewmodel

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.antoniomy82.poi_challenge.R
import com.antoniomy82.poi_challenge.databinding.FragmentMapBinding
import com.antoniomy82.poi_challenge.databinding.PopUpPoisDetailBinding
import com.antoniomy82.poi_challenge.model.District
import com.antoniomy82.poi_challenge.model.Pois
import com.antoniomy82.poi_challenge.ui.MapFragment
import com.antoniomy82.poi_challenge.ui.PoisDistrictListAdapter
import com.antoniomy82.poi_challenge.ui.PoisDistrictListFragment
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import java.lang.ref.WeakReference
import java.util.*


class PoisViewModel : ViewModel(), OnMapReadyCallback {

    //Main Fragment values
    private var frgMainActivity: WeakReference<Activity>? = null
    private var frgMainContext: WeakReference<Context>? = null
    private var frgMainView: WeakReference<View>? = null
    private var mainBundle: Bundle? = null

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
    private var selectedPoi:Pois ?=null

    @SuppressLint("SetTextI18n")
    fun setMainUI() {

        val headerTitle = frgMainView?.get()?.findViewById<View>(R.id.headerTitle) as TextView
        headerTitle.text = "MADRID"

    }

    @SuppressLint("SetTextI18n")
    fun setMapsUI() {
        val headerMapsTitle = frgMapsView?.get()?.findViewById<View>(R.id.headerTitle) as TextView
        headerMapsTitle.text = "MADRID"

        if (retrieveDistrict != null) {
            districtTittle.value = retrieveDistrict?.name?.toUpperCase(Locale.ROOT)

            if (retrieveDistrict?.pois?.size == 0) poisCount.value = "0"
            else poisCount.value = retrieveDistrict?.pois?.size.toString()

        }

        fragmentMapBinding?.poisVM = this //Update the view with databinding
        loadMap()
        onMapReady(map)
    }

    //Set Main fragment parameters in this VM
    fun setMainFragmentBinding(
        frgActivity: Activity,
        frgContext: Context,
        frgView: View,
        mainBundle: Bundle?
    ) {
        this.frgMainActivity = WeakReference(frgActivity)
        this.frgMainContext = WeakReference(frgContext)
        this.frgMainView = WeakReference(frgView)
        this.mainBundle = mainBundle
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

    //Set RecyclerView
    fun setRecyclerViewAdapter(mDistrict: District) {

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
    }

    fun setTittleFromAdapter(tittle: String, count: String) {
        districtTittle.value = tittle.toUpperCase(Locale.ROOT)
        poisCount.value = count

        Log.d("tittleBar", tittle + "count:" + count)
    }

    //Set the POI detail in a popup
    fun popUpDetail(mPoi: Pois?) {

        val popUpBinding = frgMainActivity?.get()?.let {
            frgMainContext?.get()?.let { it1 ->
                PoisUtils.genericDialog(
                    it,
                    it1, R.layout.pop_up_pois_detail
                ) as PopUpPoisDetailBinding
            }
        }



        popUpBinding?.titlePopup?.text = mPoi?.name
        popUpBinding?.streetPopup?.text = mPoi?.description

        //Set image
        if (mPoi?.image?.url != null) {
            Log.d("galleryImages", mPoi.galleryImages?.get(0)?.url.toString())
            frgMainContext?.get()?.let {
                popUpBinding?.photoPopup?.let { it1 ->
                    Glide.with(it).load(mPoi.image?.url).into(it1)
                }
            }
        }

        //Set icon image
        if (mPoi?.category?.icon?.url != null) {
            frgMainContext?.get()?.let {
                popUpBinding?.iconPopup?.let { it1 ->
                    Glide.with(it).load(mPoi.category?.icon?.url).into(it1)
                }
            }
        }

        //Set likes counter
        if (mPoi?.likesCount == null) popUpBinding?.likeQty?.text = "0"
        else popUpBinding?.likeQty?.text = mPoi.likesCount.toString()

        popUpBinding?.vm = this //Update the view with databinding
        popUpBinding?.mapPopup

        selectedPoi=mPoi
        popUpBinding?.let { loadMapPopUp(it) }

    }

    /**
     * Maps functions block
     */

    //Load Map fragment
    fun goToMap() {
        PoisUtils.replaceFragment(
            MapFragment(this),
            (frgMainContext?.get() as AppCompatActivity).supportFragmentManager
        )
    }

    //Load Map fragment
    fun goToList() {
        PoisUtils.replaceFragment(
            PoisDistrictListFragment(retrieveDistrict),
            (frgMainContext?.get() as AppCompatActivity).supportFragmentManager
        )
    }


    // Allows map styling and theme to be customized.
    private fun setMapStyle(map: GoogleMap) {
        try {
            // Customize the styling of the base map using a JSON object defined, in a raw resource file
            val success = map.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    frgMapsContext?.get(),
                    R.raw.map_style
                )
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
        isIntoPopUp=false
    }

    //Map into PopUp
    private fun loadMapPopUp(popUpBinding: PopUpPoisDetailBinding) {

        mapView = WeakReference(popUpBinding.mapPopup)
        mapView?.get()?.onCreate(mainBundle)
        mapView?.get()?.onResume()
        mapView?.get()?.getMapAsync(this)
        isIntoPopUp=true
    }

    override fun onMapReady(googleMap: GoogleMap?) {

        if (googleMap != null) {
            map = googleMap
        }

        googleMap?.uiSettings?.isZoomControlsEnabled = true //Zoom in/out

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


            googleMap?.setOnMarkerClickListener { it ->

                it.position.latitude

                val mPoi: Pois? =
                    retrieveDistrict?.pois?.find { p -> p.latitude?.toDouble() == it.position.latitude && p.longitude?.toDouble() == it.position.longitude }

                isIntoPopUp=true
                popUpDetail(mPoi)

                true
            }

            map?.let { setMapStyle(it) }
        }else{
            if(selectedPoi!=null){

                val mMarker: Marker? = map?.addMarker(MarkerOptions().position(LatLng(selectedPoi?.latitude?.toDouble()!!,selectedPoi?.longitude?.toDouble()!!)))

                frgMapsContext?.get()?.let {
                    mMarker?.loadIcon(
                        it,
                        selectedPoi?.category?.marker?.url.toString()
                    )
                }

                map?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(selectedPoi?.latitude?.toDouble()!!,selectedPoi?.longitude?.toDouble()!!), zoomLevel))
            }

        }
    }

    //Util to use glide into marker
    private fun Marker.loadIcon(context: Context, url: String?) {

        CoroutineScope(IO).let {
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
    }

}


