package leegroup.module.photosample

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import leegroup.module.photosample.ui.models.PhotoDetailUiModel
import leegroup.module.photosample.ui.screens.main.photodetail.PhotoDetailContent
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PhotoDetailScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()
    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun test_screen_appbar_title() {
        val title = context.getString(R.string.photo_detail_screen_title)

        // Start the app
        composeTestRule.setContent {
            leegroup.module.designsystem.theme.ComposeTheme {
                PhotoDetailContent(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                        .navigationBarsPadding(),
                    uiModel = PhotoDetailUiModel(),
                    onBack = {},
                    onFavoriteClick = {}
                )
            }
        }

        composeTestRule.onNodeWithText(title).assertIsDisplayed()
    }

    @Test
    fun test_screen_content() {
        val sampleUiModel = PhotoDetailUiModel(
            id = 1,
            title = "Sunset at the Beach",
            url = "https://example.com/sunset.jpg",
            isFavorite = true
        )
        composeTestRule.setContent {
            leegroup.module.designsystem.theme.ComposeTheme {
                PhotoDetailContent(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                        .navigationBarsPadding(),
                    uiModel = sampleUiModel,
                    onFavoriteClick = {}
                )
            }
        }

        val favoriteDescription = context.getString(R.string.remove_from_favorites)
        composeTestRule.onNodeWithText(sampleUiModel.title).assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription(favoriteDescription).assertIsDisplayed()
    }

}