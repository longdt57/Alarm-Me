package leegroup.modul.sample.data.repositories.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import leegroup.modul.sample.data.models.SampleModel
import leegroup.modul.sample.data.repositories.SampleRepository
import javax.inject.Inject

internal class SampleRepositoryImpl @Inject constructor() : SampleRepository {
    override fun getSample(): Flow<SampleModel> {
        return flowOf(SampleModel(1))
    }
}
