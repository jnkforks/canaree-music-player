package dev.olog.presentation.offlinelyrics

import dev.olog.core.gateway.OfflineLyricsGateway
import dev.olog.core.prefs.TutorialPreferenceGateway
import dev.olog.core.schedulers.Schedulers
import dev.olog.intents.AppConstants
import dev.olog.offlinelyrics.BaseOfflineLyricsPresenter
import dev.olog.offlinelyrics.domain.InsertOfflineLyricsUseCase
import dev.olog.offlinelyrics.domain.ObserveOfflineLyricsUseCase
import javax.inject.Inject

class OfflineLyricsFragmentPresenter @Inject constructor(
    observeUseCase: ObserveOfflineLyricsUseCase,
    insertUseCase: InsertOfflineLyricsUseCase,
    private val tutorialPreferenceUseCase: TutorialPreferenceGateway,
    lyricsGateway: OfflineLyricsGateway,
    schedulers: Schedulers

) : BaseOfflineLyricsPresenter(
    lyricsGateway,
    observeUseCase,
    insertUseCase,
    schedulers
) {

    private var currentTitle: String = ""
    private var currentArtist: String = ""

    fun updateCurrentMetadata(title: String, artist: String) {
        this.currentTitle = title
        this.currentArtist = artist
    }

    fun getInfoMetadata(): String {
        var result = currentTitle
        if (currentArtist != AppConstants.UNKNOWN) {
            result += " $currentArtist"
        }
        result += " lyrics"
        return result
    }

    fun showAddLyricsIfNeverShown(): Boolean {
        return tutorialPreferenceUseCase.lyricsTutorial()
    }

}