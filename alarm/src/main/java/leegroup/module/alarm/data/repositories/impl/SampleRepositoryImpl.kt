package leegroup.module.alarm.data.repositories.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import leegroup.module.alarm.data.models.SampleModel
import leegroup.module.alarm.data.repositories.SampleRepository
import javax.inject.Inject

internal class SampleRepositoryImpl @Inject constructor() : SampleRepository {
    override fun getSample(): Flow<SampleModel> {
        return flowOf(SampleModel(1))
    }
}
