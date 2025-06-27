package leegroup.modul.sample.domain.usecases

import leegroup.modul.sample.data.repositories.SampleRepository
import javax.inject.Inject

internal class SampleUseCase @Inject constructor(
    private val sampleRepository: SampleRepository
) {

    operator fun invoke() = sampleRepository.getSample()
}