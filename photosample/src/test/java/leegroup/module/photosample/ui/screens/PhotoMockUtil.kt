package leegroup.module.photosample.ui.screens

import leegroup.module.photosample.domain.models.PhotoModelD
import leegroup.module.photosample.ui.models.PhotoUiModel

internal object PhotoMockUtil {
    val photoModel = PhotoModelD(
        id = 1,
        albumId = 1,
        thumbnailUrl = "https://example.com",
        title = "Test Photo",
        url = "https://example.com/photo.jpg",
    )

    val photoUiModel = PhotoUiModel(
        id = 1,
        albumId = 1,
        thumbnailUrl = "https://example.com",
        title = "Test Photo",
        url = "https://example.com/photo.jpg",
        isFavorite = false
    )
}