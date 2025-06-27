package leegroup.module.photosample

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.collections.immutable.persistentListOf
import leegroup.module.photosample.ui.models.PhotoListUiModel
import leegroup.module.photosample.ui.models.PhotoUiModel
import leegroup.module.photosample.ui.screens.main.photolist.PhotoListContent
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PhotoListScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()
    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun test_screen_appbar_title() {
        val title = context.getString(R.string.photo_list_screen_title)

        // Start the app
        composeTestRule.setContent {
            leegroup.module.designsystem.theme.ComposeTheme {
                PhotoListContent(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                        .navigationBarsPadding(),
                    uiModel = PhotoListUiModel(
                        query = "nature",
                        photos = persistentListOf(),
                        hasMore = true
                    ),
                    showRefresh = false
                )
            }
        }

        composeTestRule.onNodeWithText(title).assertIsDisplayed()
    }

    @Test
    fun test_screen_content() {
        val samplePhotos = persistentListOf(
            PhotoUiModel(
                id = 1,
                albumId = 1,
                thumbnailUrl = "https://example.com/thumbnail1.jpg",
                title = "Beautiful Sunset",
                url = "https://example.com/photo1.jpg",
                isFavorite = true
            ),
            PhotoUiModel(
                id = 2,
                albumId = 1,
                thumbnailUrl = "https://example.com/thumbnail2.jpg",
                title = "Mountain View",
                url = "https://example.com/photo2.jpg",
                isFavorite = false
            ),
            PhotoUiModel(
                id = 3,
                albumId = 2,
                thumbnailUrl = "https://example.com/thumbnail3.jpg",
                title = "City Skyline",
                url = "https://example.com/photo3.jpg",
                isFavorite = true
            )
        )
        composeTestRule.setContent {
            leegroup.module.designsystem.theme.ComposeTheme {
                PhotoListContent(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                        .navigationBarsPadding(),
                    uiModel = PhotoListUiModel(
                        query = "nature",
                        photos = samplePhotos,
                        hasMore = true
                    ),
                    showRefresh = false
                )
            }
        }

        composeTestRule.onNodeWithText(samplePhotos.first().title).assertIsDisplayed()
        composeTestRule.onNodeWithText(samplePhotos.last().title).assertIsDisplayed()
    }

}