package leegroup.module.sample.gituser.support.extensions

internal fun String.formattedUrl(): String {
    return if (this.startsWith("http://") || this.startsWith("https://")) {
        this
    } else {
        "http://$this"
    }
}