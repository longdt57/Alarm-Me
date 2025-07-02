package leegroup.module.alarm.ui.screens.components

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import leegroup.module.alarm.R
import leegroup.module.alarm.support.extensions.canUseFullScreenIntent
import leegroup.module.alarm.support.extensions.openFullScreenIntentSettings
import leegroup.module.alarm.support.extensions.openNotificationSettings

@Composable
internal fun CheckNotificationPermissionAndSettings(
    notificationsCallback: (granted: Boolean) -> Unit,
) {
    val context = LocalContext.current
    var showNotificationDialog by remember { mutableStateOf(false) }
    var showFullScreenDialog by remember { mutableStateOf(false) }

    fun checkFullScreenAndNotify() {
        if (!context.canUseFullScreenIntent()) {
            showFullScreenDialog = true
        } else {
            notificationsCallback(true)
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            checkFullScreenAndNotify()
        } else {
            showNotificationDialog = true
        }
    }

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val isGranted = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

            if (!isGranted) {
                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            } else {
                checkFullScreenAndNotify()
            }
        } else {
            checkFullScreenAndNotify()
        }
    }

    if (showNotificationDialog) {
        AppDialog(
            title = stringResource(id = R.string.alert_permission_post_notifications_title),
            message = stringResource(id = R.string.allow_notifications_reminders),
            onDismissed = { showNotificationDialog = false },
            primaryButtonText = stringResource(id = R.string.ok),
            onPrimaryClick = {
                context.openNotificationSettings()
                notificationsCallback(false)
                showNotificationDialog = false
            },
            secondaryButtonText = stringResource(id = R.string.cancel),
            onSecondaryButtonClick = {
                notificationsCallback(false)
                showNotificationDialog = false
            }
        )
    }

    if (showFullScreenDialog) {
        AppDialog(
            title = stringResource(id = R.string.alert_permission_post_notifications_title),
            message = stringResource(id = R.string.allow_full_screen_notifications_reminders),
            onDismissed = { showFullScreenDialog = false },
            primaryButtonText = stringResource(id = R.string.ok),
            onPrimaryClick = {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                    context.openFullScreenIntentSettings(context.packageName)
                }
                notificationsCallback(false)
                showFullScreenDialog = false
            },
            secondaryButtonText = stringResource(id = R.string.cancel),
            onSecondaryButtonClick = {
                notificationsCallback(false)
                showFullScreenDialog = false
            }
        )
    }
}
