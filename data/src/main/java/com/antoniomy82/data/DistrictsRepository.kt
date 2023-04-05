package com.antoniomy82.data

import com.antoniomy82.data.model.District
import kotlinx.coroutines.flow.MutableStateFlow


class DistrictsRepository(urlId: String) {
    val remoteDistrictRepository: MutableStateFlow<District> = DistrictRemoteDataSource().getDistrictList(urlId)
}