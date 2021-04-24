package com.antoniomy82.poi_challenge.model

data class District(
    var poisCount: Int? = null,
    var id: Int? = null,
    var name: String ?=null,
    var image: Multimedia? = null,
    var galleryImages: ArrayList<Multimedia>? = null,
    var coordinates: String? = null,
    var video: Multimedia? = null,
    var audio: Multimedia? = null,
    var pois: List<Pois>? = null
)
