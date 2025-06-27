package leegroup.module.photosample.support.extensions

internal fun String.convertToDummyUrl(): String {
    return replace("https://via.placeholder.com", "https://dummyimage.com")
}