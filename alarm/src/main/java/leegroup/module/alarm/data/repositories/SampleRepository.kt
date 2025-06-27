package leegroup.module.alarm.data.repositories

import kotlinx.coroutines.flow.Flow
import leegroup.module.alarm.data.models.SampleModel

internal interface SampleRepository {

    fun getSample(): Flow<SampleModel>

}