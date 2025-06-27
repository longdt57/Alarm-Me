package leegroup.module.sample.gituser.ui.mapper

import android.content.Context
import io.mockk.every
import io.mockk.mockk
import leegroup.module.sample.gituser.domain.models.GitUserDetailModel
import leegroup.module.sample.gituser.ui.models.GitUserDetailUiModel
import org.junit.Assert.assertEquals
import org.junit.Test

class GitUserDetailUiMapperImplTest {

    private val notSet = "Not set"

    private val mockContext: Context = mockk {
        every { getString(any()) } returns notSet
    }
    private val mapper = GitUserDetailUiMapperImpl(mockContext)

    @Test
    fun `mapToUiModel should correctly map all fields from model to uiModel`() {
        val oldUiModel = GitUserDetailUiModel()

        val model = GitUserDetailModel(
            id = 1,
            login = "newLogin",
            name = "newName",
            avatarUrl = "newAvatarUrl",
            blog = "newBlog",
            location = "newLocation",
            followers = 1500,
            following = 800
        )

        val expectedUiModel = oldUiModel.copy(
            name = "newName",
            avatarUrl = "newAvatarUrl",
            blog = "newBlog",
            location = "newLocation",
            followers = "100+",
            following = "100+"
        )

        val result = mapper.mapToUiModel(oldUiModel, model)
        assertEquals(expectedUiModel, result)
    }

    @Test
    fun `mapToUiModel should use login if name is null`() {
        val oldUiModel = GitUserDetailUiModel()
        val model = GitUserDetailModel(
            id = 1,
            login = "newLogin",
            name = null,
            avatarUrl = "newAvatarUrl",
            blog = "newBlog",
            location = "newLocation",
            followers = 500,
            following = 200
        )

        val result = mapper.mapToUiModel(oldUiModel, model)

        assertEquals("newLogin", result.name)
    }

    @Test
    fun `mapToUiModel should use empty string if blog or avatarUrl is null`() {
        val oldUiModel = GitUserDetailUiModel()
        val model = GitUserDetailModel(
            id = 1,
            login = "newLogin",
            name = "newName",
            avatarUrl = null,
            blog = null,
            location = "newLocation",
            followers = 500,
            following = 200
        )

        val result = mapper.mapToUiModel(oldUiModel, model)

        assertEquals("", result.avatarUrl)
        assertEquals("", result.blog)
    }

    @Test
    fun `mapToUiModel should use context string for location if null or blank`() {
        val oldUiModel = GitUserDetailUiModel()
        val model = GitUserDetailModel(
            id = 1,
            login = "newLogin",
            name = "newName",
            avatarUrl = "newAvatarUrl",
            blog = "newBlog",
            location = null,
            followers = 500,
            following = 200
        )

        val result = mapper.mapToUiModel(oldUiModel, model)

        assertEquals(notSet, result.location)
    }

    @Test
    fun `mapToUiModel should format followers and following numbers correctly`() {
        val oldUiModel = GitUserDetailUiModel()
        val model = GitUserDetailModel(
            id = 1,
            login = "newLogin",
            name = "newName",
            avatarUrl = "newAvatarUrl",
            blog = "newBlog",
            location = "newLocation",
            followers = 1200,
            following = 80
        )

        val result = mapper.mapToUiModel(oldUiModel, model)

        assertEquals("100+", result.followers)
        assertEquals("80", result.following)
    }
}
