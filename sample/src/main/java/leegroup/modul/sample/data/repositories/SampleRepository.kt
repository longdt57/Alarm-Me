package leegroup.modul.sample.data.repositories

import kotlinx.coroutines.flow.Flow
import leegroup.modul.sample.data.models.SampleModel

internal interface SampleRepository {

    fun getSample(): Flow<SampleModel>

}