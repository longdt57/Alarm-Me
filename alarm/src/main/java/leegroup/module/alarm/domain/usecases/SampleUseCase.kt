package leegroup.module.alarm.domain.usecases

import leegroup.module.alarm.data.repositories.SampleRepository
import javax.inject.Inject

internal class SampleUseCase @Inject constructor(
    private val sampleRepository: SampleRepository
) {

    operator fun invoke() = sampleRepository.getSample()
}