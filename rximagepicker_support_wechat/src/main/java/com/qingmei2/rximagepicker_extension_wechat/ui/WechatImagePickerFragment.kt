package com.qingmei2.rximagepicker_extension_wechat.ui

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.qingmei2.rximagepicker.entity.Result
import com.qingmei2.rximagepicker.ui.ActivityPickerViewController
import com.qingmei2.rximagepicker.ui.ICustomPickerConfiguration
import com.qingmei2.rximagepicker.ui.gallery.IGalleryCustomPickerView
import com.qingmei2.rximagepicker_extension.entity.Album
import com.qingmei2.rximagepicker_extension.entity.Item
import com.qingmei2.rximagepicker_extension.entity.SelectionSpec
import com.qingmei2.rximagepicker_extension.model.AlbumCollection
import com.qingmei2.rximagepicker_extension.model.SelectedItemCollection
import com.qingmei2.rximagepicker_extension.ui.AlbumPreviewActivity
import com.qingmei2.rximagepicker_extension.ui.BasePreviewActivity
import com.qingmei2.rximagepicker_extension.ui.adapter.AlbumMediaAdapter
import com.qingmei2.rximagepicker_extension.ui.widget.AlbumsSpinner
import com.qingmei2.rximagepicker_extension_wechat.R
import com.qingmei2.rximagepicker_extension_wechat.ui.adapter.WechatAlbumsAdapter
import com.qingmei2.rximagepicker_extension_wechat.ui.widget.WechatAlbumsSpinner
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import java.util.*

class WechatImagePickerFragment : Fragment(), IGalleryCustomPickerView,
        AlbumCollection.AlbumCallbacks, AdapterView.OnItemSelectedListener,
        View.OnClickListener, WechatImageListGridFragment.SelectionProvider,
        AlbumMediaAdapter.OnMediaClickListener, AlbumMediaAdapter.CheckStateListener {

    private val mAlbumCollection = AlbumCollection()

    private lateinit var mAlbumsSpinner: AlbumsSpinner
    private lateinit var mAlbumsAdapter: CursorAdapter

    private var publishSubject: PublishSubject<Result> = PublishSubject.create()

    private var mSelectedCollection: SelectedItemCollection? = null

    private lateinit var mButtonPreview: TextView
    private lateinit var mButtonApply: TextView
    private lateinit var mRadioButton: RadioButton

    private lateinit var mContainer: View
    private lateinit var mEmptyView: View

    private var imageOriginalMode = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val contextThemeWrapper = ContextThemeWrapper(activity, SelectionSpec.instance!!.themeId)
        val localInflater = inflater
                .cloneInContext(contextThemeWrapper)
        return localInflater.inflate(R.layout.fragment_picker_wechat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mSelectedCollection = SelectedItemCollection(context!!)

        mButtonPreview = view.findViewById(R.id.button_preview)
        mButtonApply = view.findViewById(R.id.button_apply)
        mRadioButton = view.findViewById(R.id.rb_original)
        mButtonPreview.setOnClickListener(this)
        mButtonApply.setOnClickListener(this)
        mRadioButton.setOnClickListener(this)

        mContainer = view.findViewById(R.id.container)
        mEmptyView = view.findViewById(R.id.empty_view)

        val mButtonBack = view.findViewById<ImageView>(R.id.button_back)
        mButtonBack.setOnClickListener(this)

        mSelectedCollection!!.onCreate(savedInstanceState)
        updateBottomToolbar()

        mAlbumsAdapter = WechatAlbumsAdapter(context!!, null, false)
        mAlbumsSpinner = WechatAlbumsSpinner(context!!)
        mAlbumsSpinner.setOnItemSelectedListener(this)
        mAlbumsSpinner.setSelectedTextView(view.findViewById(R.id.selected_album))
        mAlbumsSpinner.setPopupAnchorView(view.findViewById(R.id.bottom_toolbar))
        mAlbumsSpinner.setAdapter(mAlbumsAdapter)
        mAlbumCollection.onCreate(activity!!, this)
        mAlbumCollection.onRestoreInstanceState(savedInstanceState)
        mAlbumCollection.loadAlbums()
    }

    override fun display(fragmentActivity: androidx.fragment.app.FragmentActivity,
                         viewContainer: Int,
                         configuration: ICustomPickerConfiguration?) {
        val fragmentManager = fragmentActivity.supportFragmentManager
        val fragment = fragmentManager.findFragmentByTag(tag)
        if (fragment == null) {
            if (viewContainer != 0)
                fragmentManager.beginTransaction().add(viewContainer, this, tag).commit()
            else
                throw IllegalArgumentException(
                        "the viewContainer == 0, please configrate the containerViewId in the @Gallery annotation."
                )
        }
    }

    override fun pickImage(): Observable<Result> {
        publishSubject = PublishSubject.create()
        return publishSubject
    }

    fun closure() {
        if (activity is WechatImagePickerActivity) {
            (activity as WechatImagePickerActivity).closure()
        } else {
            val fragmentManager = fragmentManager
            val fragmentTransaction = fragmentManager!!.beginTransaction()
            fragmentTransaction.remove(this)
            fragmentTransaction.commit()
        }
        SelectionSpec.instance.onFinished()
    }

    override fun onAlbumLoad(cursor: Cursor) {
        mAlbumsAdapter.swapCursor(cursor)
        // select default album.
        val handler = Handler(Looper.getMainLooper())
        handler.post {
            cursor.moveToPosition(mAlbumCollection.currentSelection)
            mAlbumsSpinner.setSelection(context!!,
                    mAlbumCollection.currentSelection)
            val album = Album.valueOf(cursor)
            if (album.isAll) {
                album.addCaptureCount()
            }
            onAlbumSelected(album)
        }
    }

    override fun onAlbumReset() {
        mAlbumsAdapter.swapCursor(null)
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        mAlbumCollection.setStateCurrentSelection(position)
        mAlbumsAdapter.cursor.moveToPosition(position)
        val album = Album.valueOf(mAlbumsAdapter.cursor)
        if (album.isAll) {
            album.addCaptureCount()
        }
        onAlbumSelected(album)
    }

    override fun onNothingSelected(parent: AdapterView<*>) {

    }

    private fun updateBottomToolbar() {
        val selectedCount = mSelectedCollection!!.count()
        if (selectedCount == 0) {
            mButtonPreview.isEnabled = false
            mButtonApply.isEnabled = false
            mButtonApply.text = getString(R.string.button_apply_default)
        } else if (selectedCount == 1 && SelectionSpec.instance.singleSelectionModeEnabled()) {
            mButtonPreview.isEnabled = true
            mButtonApply.setText(R.string.button_apply_default)
            mButtonApply.isEnabled = true
        } else {
            mButtonPreview.isEnabled = true
            mButtonApply.isEnabled = true
            mButtonApply.text = getString(R.string.button_apply, selectedCount)
        }
    }

    private fun onAlbumSelected(album: Album) {
        if (album.isAll && album.isEmpty) {
            mContainer.visibility = View.GONE
            mEmptyView.visibility = View.VISIBLE
        } else {
            mContainer.visibility = View.VISIBLE
            mEmptyView.visibility = View.GONE
            val fragment = WechatImageListGridFragment.instance(album)
            fragment.injectDependencies(this, this, this)
            childFragmentManager
                    .beginTransaction()
                    .replace(R.id.container, fragment, WechatImageListGridFragment::class.java.simpleName)
                    .commitAllowingStateLoss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mAlbumCollection.onDestroy()
    }

    override fun onMediaClick(album: Album?, item: Item, adapterPosition: Int) {
        val intent = Intent(context, WechatAlbumPreviewActivity::class.java)
        intent.putExtra(AlbumPreviewActivity.EXTRA_ALBUM, album)
        intent.putExtra(AlbumPreviewActivity.EXTRA_ITEM, item)
        intent.putExtra(BasePreviewActivity.EXTRA_DEFAULT_BUNDLE, mSelectedCollection!!.dataWithBundle)
        startActivityForResult(intent, WechatImagePickerActivity.REQUEST_CODE_PREVIEW)
    }

    override fun onUpdate() {
        updateBottomToolbar()
    }

    override fun provideSelectedItemCollection(): SelectedItemCollection {
        return mSelectedCollection!!
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.button_preview -> {
                val intent = Intent(context, WechatSelectedPreviewActivity::class.java)
                intent.putExtra(BasePreviewActivity.EXTRA_DEFAULT_BUNDLE, mSelectedCollection!!.dataWithBundle)
                startActivityForResult(intent, WechatImagePickerActivity.REQUEST_CODE_PREVIEW)
            }
            R.id.button_apply -> emitSelectUri()
            R.id.button_back -> activity!!.onBackPressed()
            R.id.rb_original -> switchImageOriginalMode()
        }
    }

    private fun switchImageOriginalMode() {
        val original = !imageOriginalMode
        this.imageOriginalMode = original
        this.mRadioButton.isChecked = original
    }

    private fun emitSelectUri() {
        val selected = mSelectedCollection!!.asList() as ArrayList<Item>
        for (item in selected) {
            publishSubject.onNext(instanceResult(item))
        }
        endPickImage()
    }

    private fun endPickImage() {
        publishSubject.onComplete()
        closure()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != RESULT_OK)
            return

        if (requestCode == WechatImagePickerActivity.REQUEST_CODE_PREVIEW) {
            val resultBundle = data!!.getBundleExtra(BasePreviewActivity.EXTRA_RESULT_BUNDLE)
            val selected = resultBundle.getParcelableArrayList<Item>(SelectedItemCollection.STATE_SELECTION)
            val collectionType = resultBundle.getInt(SelectedItemCollection.STATE_COLLECTION_TYPE,
                    SelectedItemCollection.COLLECTION_UNDEFINED)
            if (data.getBooleanExtra(BasePreviewActivity.EXTRA_RESULT_APPLY, false)) {  // apply event
                if (selected != null) {
                    for (item in selected) {
                        if (activity is WechatImagePickerActivity) {
                            ActivityPickerViewController.instance.emitResult(instanceResult(item))
                        } else {
                            publishSubject.onNext(instanceResult(item))
                        }
                    }
                }
                closure()
            } else {         // back event
                mSelectedCollection!!.overwrite(selected!!, collectionType)
                val weChatListFragment = childFragmentManager.findFragmentByTag(
                        WechatImageListGridFragment::class.java.simpleName)
                if (weChatListFragment is WechatImageListGridFragment) {
                    weChatListFragment.refreshMediaGrid()
                }
                updateBottomToolbar()
            }
        }
    }

    private fun instanceResult(item: Item): Result {
        return Result.Builder(item.contentUri)
                .putBooleanExtra(EXTRA_ORIGINAL_IMAGE, imageOriginalMode)
                .putStringExtra(EXTRA_OPTIONAL_MIME_TYPE, item.mimeType ?: "")
                .build()
    }

    companion object {

        // Whether to display the original image.
        const val EXTRA_ORIGINAL_IMAGE = "EXTRA_ORIGINAL_IMAGE"

        // The Uri's mime type.
        const val EXTRA_OPTIONAL_MIME_TYPE = "EXTRA_OPTIONAL_MIME_TYPE"
    }
}
