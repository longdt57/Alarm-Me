package leegroup.module.photosample.domain.usecases.photolist

import kotlinx.coroutines.delay
import javax.inject.Inject

internal class PhotoListQueryDelayUseCase @Inject constructor() {

    suspend operator fun invoke(query: String) {
        if (query.isNotEmpty()) {
            delay(QUERY_DELAY)
        }
    }

    companion object {
        const val QUERY_DELAY = 500L
    }
}