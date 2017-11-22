package dev.olog.msc

import android.content.ComponentName
import android.content.Context
import android.support.v4.media.MediaBrowserCompat
import dagger.Module
import dagger.Provides
import dev.olog.music_service.MusicService
import dev.olog.music_service.interfaces.ActivityClass
import dev.olog.presentation.activity_main.MainActivity
import dev.olog.presentation.music_service.RxMusicServiceConnectionCallback
import dev.olog.shared.ApplicationContext
import javax.inject.Singleton

@Module
class MusicServiceBinderModule {

    @Provides
    @Singleton
    internal fun provideMediaBrowser(
            @ApplicationContext context: Context,
            rxConnectionCallback: RxMusicServiceConnectionCallback): MediaBrowserCompat {

        return MediaBrowserCompat(context,
                ComponentName(context, MusicService::class.java),
                rxConnectionCallback.get(), null)
    }

    @Provides
    internal fun provideActivityClass(): ActivityClass {
        return object : ActivityClass {
            override fun get(): Class<*> = MainActivity::class.java
        }
    }

}