package leegroup.module.sample.gituser.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TestUser(
    @SerialName("id")
    val id: Int,

    @SerialName("firstName")
    val firstName: String,

    @SerialName("lastName")
    val lastName: String
)