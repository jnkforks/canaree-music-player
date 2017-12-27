package dev.olog.floating_info

import android.app.Notification
import android.app.Service
import android.content.Context
import android.content.Intent
import android.view.ContextThemeWrapper
import io.mattcarroll.hover.HoverMenu
import io.mattcarroll.hover.HoverView
import javax.inject.Inject

class FloatingInfoService : BaseService() {

    @Inject lateinit var hoverMenu: CustomHoverMenu
    @Inject lateinit var notification : InfoNotification

    companion object {
        const val TAG = "FloatingInfoService"
        const val ACTION_STOP = "$TAG.ACTION_STOP"
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {


        if (intent != null && intent.action != null){
            when (intent.action){
                ACTION_STOP -> {
                    stopSelf()
                    return Service.START_NOT_STICKY
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun getContextForHoverMenu(): Context {
        return ContextThemeWrapper(this, R.style.AppTheme)
    }

    override fun onHoverMenuLaunched(intent: Intent, hoverView: HoverView) {
        hoverView.setMenu(createHoverMenu())
        hoverView.collapse()

        hoverView.addOnExpandAndCollapseListener(onExpansionListener)
    }

    override fun onHoverMenuExitingByUserRequest() {
        super.onHoverMenuExitingByUserRequest()
        hoverView.removeOnExpandAndCollapseListener(onExpansionListener)
    }

    private val onExpansionListener = object : HoverView.Listener {
        override fun onCollapsing() {
            val section = hoverMenu.getSection(HoverMenu.SectionId("lyrics"))
            (section?.tabView as TabView).setIcon(R.drawable.vd_bird_singing)
        }

        override fun onExpanding() {
            val section = hoverMenu.getSection(HoverMenu.SectionId("lyrics"))
            (section?.tabView as TabView).setIcon(R.drawable.vd_lyrics)
        }

        override fun onClosing() {

        }

        override fun onClosed() {

        }

        override fun onExpanded() {

        }

        override fun onCollapsed() {

        }
    }

    override fun getForegroundNotificationId(): Int = InfoNotification.NOTIFICATION_ID

    override fun getForegroundNotification(): Notification? {
        return notification.buildNotification()
    }

    private fun createHoverMenu() : HoverMenu = hoverMenu

}