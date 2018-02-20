package dev.olog.msc.presentation

import android.databinding.BindingAdapter
import android.graphics.Typeface
import android.net.Uri
import android.support.v4.content.ContextCompat
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import dev.olog.msc.app.GlideApp
import dev.olog.msc.presentation.model.DisplayableItem
import dev.olog.msc.presentation.special.thanks.SpecialThanksModel
import dev.olog.msc.presentation.widget.QuickActionView
import dev.olog.msc.utils.MediaId
import dev.olog.msc.utils.img.CoverUtils
import java.io.File

object BindingsAdapter {

    const val OVERRIDE_SMALL = 150
    const val OVERRIDE_MID = 300
    const val OVERRIDE_BIG = 600

    private fun loadImageImpl(
            view: ImageView,
            item: DisplayableItem,
            override: Int,
            priority: Priority = Priority.HIGH,
            asPlaceholder: Boolean = false,
            rounded: Boolean = false){

        val mediaId = item.mediaId

        val context = view.context

        GlideApp.with(context).clear(view)

        val source = mediaId.source
        val id = resolveId(mediaId)

        var request = GlideApp.with(context)
                .asBitmap()
                .load(resolveUri(item.image))
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .override(override)
                .priority(priority)
                .thumbnail(.4f)

        if (rounded){
            request = request.circleCrop()
        }

        if (asPlaceholder){
            request = request.placeholder(CoverUtils.getGradient(context, position = id))
        } else {
            request = request.error(CoverUtils.getGradient(context, position = id, source = source))
        }

        request.into(view)
    }

    @BindingAdapter("imageSong", "rounded", requireAll = false)
    @JvmStatic
    fun loadSongImage(view: ImageView, item: DisplayableItem, rounded: Boolean = false) {
        loadImageImpl(view, item, OVERRIDE_SMALL, asPlaceholder = true, rounded = rounded)
    }

    @BindingAdapter("imageAlbum", "rounded", requireAll = false)
    @JvmStatic
    fun loadAlbumImage(view: ImageView, item: DisplayableItem, rounded: Boolean = false) {
        loadImageImpl(view, item, OVERRIDE_MID, Priority.HIGH, rounded = rounded)
    }

    @BindingAdapter("imageBigAlbum", "rounded", requireAll = false)
    @JvmStatic
    fun loadBigAlbumImage(view: ImageView, item: DisplayableItem, rounded: Boolean = false) {
        loadImageImpl(view, item, OVERRIDE_BIG, Priority.IMMEDIATE, rounded = rounded)
    }

    @BindingAdapter("imageSpecialThanks")
    @JvmStatic
    fun loadSongImage(view: ImageView, item: SpecialThanksModel) {
        val context = view.context

        GlideApp.with(context).clear(view)

        GlideApp.with(context)
                .load(Uri.EMPTY)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .override(OVERRIDE_SMALL)
                .placeholder(ContextCompat.getDrawable(view.context, item.image))
                .into(view)
    }

    @BindingAdapter("setBoldIfTrue")
    @JvmStatic
    fun setBoldIfTrue(view: TextView, setBold: Boolean){
        val style = if (setBold) Typeface.BOLD else Typeface.NORMAL
        view.setTypeface(null, style)
    }

    private fun resolveUri(imageAsString: String): Uri {
        val file = File(imageAsString)
        return if (file.exists()){
            Uri.fromFile(file)
        } else {
            Uri.parse(imageAsString)
        }
    }

    private fun resolveId(mediaId: MediaId): Int {
        if (mediaId.isLeaf){
            return mediaId.leaf!!.toInt()
        }
        if (mediaId.isFolder){
            return mediaId.categoryValue.hashCode()
        }
        return mediaId.categoryValue.toInt()
    }

    @BindingAdapter("quickActionItem")
    @JvmStatic
    fun quickActionItem(view: QuickActionView, item: DisplayableItem){
        view.setId(item.mediaId)
    }

}