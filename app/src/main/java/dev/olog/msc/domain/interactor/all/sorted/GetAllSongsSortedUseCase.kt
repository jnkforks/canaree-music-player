package dev.olog.msc.domain.interactor.all.sorted

import dev.olog.msc.domain.entity.Song
import dev.olog.msc.domain.entity.SortArranging
import dev.olog.msc.domain.entity.SortType
import dev.olog.msc.domain.executors.IoScheduler
import dev.olog.msc.domain.gateway.prefs.AppPreferencesGateway
import dev.olog.msc.domain.interactor.all.GetAllSongsUseCase
import dev.olog.msc.domain.interactor.base.ObservableUseCase
import io.reactivex.Observable
import io.reactivex.rxkotlin.Observables
import java.text.Collator
import javax.inject.Inject

class GetAllSongsSortedUseCase @Inject constructor(
        schedulers: IoScheduler,
        private val getAllUseCase: GetAllSongsUseCase,
        private val appPrefsGateway: AppPreferencesGateway,
        private val collator: Collator


) : ObservableUseCase<List<Song>>(schedulers){

    override fun buildUseCaseObservable(): Observable<List<Song>> {
        return Observables.combineLatest(
                getAllUseCase.execute(),
                appPrefsGateway.observeAllTracksSortOrder()
            ) { tracks, order ->
                val (sort, arranging) = order

                if (arranging == SortArranging.ASCENDING){
                    tracks.sortedWith(getAscendingComparator(sort))
                } else {
                    tracks.sortedWith(getDescendingComparator(sort))
                }
            }
    }

    private fun getAscendingComparator(sortType: SortType): Comparator<Song> {
        return when (sortType){
            SortType.TITLE -> Comparator { o1, o2 -> collator.compare(o1.title, o2.title) }
            SortType.ARTIST -> Comparator { o1, o2 -> collator.compare(o1.artist, o2.artist) }
            SortType.ALBUM_ARTIST -> Comparator { o1, o2 -> collator.compare(o1.albumArtist, o2.albumArtist) }
            SortType.ALBUM -> Comparator { o1, o2 -> collator.compare(o1.album, o2.album) }
            SortType.DURATION -> compareBy { it.duration }
            SortType.RECENTLY_ADDED -> compareBy { it.dateAdded }
            else -> throw IllegalStateException("can't sort all tracks, invalid sort type $sortType")
        }
    }

    private fun getDescendingComparator(sortType: SortType): Comparator<Song> {
        return when (sortType){
            SortType.TITLE -> Comparator { o1, o2 -> collator.compare(o2.title, o1.title) }
            SortType.ARTIST -> Comparator { o1, o2 -> collator.compare(o2.artist, o1.artist) }
            SortType.ALBUM_ARTIST -> Comparator { o1, o2 -> collator.compare(o2.albumArtist, o1.albumArtist) }
            SortType.ALBUM -> Comparator { o1, o2 -> collator.compare(o2.album, o1.album) }
            SortType.DURATION -> compareByDescending { it.duration }
            SortType.RECENTLY_ADDED -> compareByDescending { it.dateAdded }
            else -> throw IllegalStateException("can't sort all tracks, invalid sort type $sortType")
        }
    }

}