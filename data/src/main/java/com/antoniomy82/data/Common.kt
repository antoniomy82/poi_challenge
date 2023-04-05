package com.antoniomy82.data

import com.antoniomy82.data.model.CitiesListHome
import com.antoniomy82.mycities.data.R

const val urlCities = "https://cityme-services.prepro.site/app_dev.php/api/districts/"
const val timeOut: Long = 60

val mad_icon = R.mipmap.ic_madrid_round
val sev_icon = R.mipmap.ic_sevilla_round
val bcn_icon = R.mipmap.ic_barcelona_round

const val mad_name = "Madrid"
const val sev_name = "Sevilla"
const val bcn_name = "Barcelona"

fun getCities() = ArrayList<CitiesListHome>().apply {
    add(CitiesListHome(mad_name, "Lavapíes", mad_icon, 1))
    add(CitiesListHome(mad_name, "Centro", mad_icon, 2))
    add(CitiesListHome(mad_name, "Malasaña", mad_icon, 3))
    add(CitiesListHome(mad_name, "Chueca", mad_icon, 5))
    add(CitiesListHome(mad_name, "Huertas", mad_icon, 6))
    add(CitiesListHome(sev_name, "Alfalfa, Casco Antiguo", sev_icon, 7))
    add(CitiesListHome(sev_name, "Arenal, Museo", sev_icon, 8))
    add(CitiesListHome(sev_name, "Macarena, S.Luis, S.Vicente", sev_icon, 9))
    add(CitiesListHome(sev_name, "Santa Cruz, Juderia", sev_icon, 10))
    add(CitiesListHome(sev_name, "Triana, Los Remedios", sev_icon, 11))
    add(CitiesListHome(bcn_name, "Barceloneta, Poble Nou", bcn_icon, 12))
    add(CitiesListHome(bcn_name, "El born, Ribera", bcn_icon, 13))
    add(CitiesListHome(bcn_name, "Gótico", bcn_icon, 14))
    add(CitiesListHome(bcn_name, "Raval Poble Sec", bcn_icon, 15))
    add(CitiesListHome(bcn_name, "Eixample, Gracia", bcn_icon, 16))
}

