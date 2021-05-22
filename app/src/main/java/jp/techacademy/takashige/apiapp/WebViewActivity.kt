package jp.techacademy.takashige.apiapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_web_view.*


class WebViewActivity: AppCompatActivity() {

    private val viewPagerAdapter by lazy { ViewPagerAdapter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)
        val state = intent.getSerializableExtra(KEY_URL)

        var isFavorite = true

        if (state is Shop) {
            isFavorite = FavoriteShop.findBy(state.id) != null
            if (state.couponUrls.sp.isNotEmpty()) {
                webView.loadUrl(state.couponUrls.sp)
            } else {
                webView.loadUrl(state.couponUrls.pc)
            }
        } else if (state is FavoriteShop) {
            webView.loadUrl(state.url)
        }

        button1.setOnClickListener{
            if (isFavorite) {
                if (state is Shop) {
                    onDeleteFavorite(state)
                } else if (state is FavoriteShop){
                    onDeleteFavorite(state)
                }
            } else {
                if (state is Shop) {
                    onAddFavorite(state)
                } else if (state is FavoriteShop){
                    onAddFavorite(state)
                }
            }
        }
    }

    private fun onAddFavorite(shop: Shop) {
        FavoriteShop.insert(FavoriteShop().apply {
            id = shop.id
            name = shop.name
            imageUrl = shop.logoImage
            url = if (shop.couponUrls.sp.isNotEmpty()) shop.couponUrls.sp else shop.couponUrls.pc
        })
        (viewPagerAdapter.fragments[VIEW_PAGER_POSITION_FAVORITE] as FavoriteFragment).updateData()
    }

    private fun onAddFavorite(favoriteShop: FavoriteShop) {
        FavoriteShop.insert(FavoriteShop().apply {
            id = favoriteShop.id
            name = favoriteShop.name
            imageUrl = favoriteShop.imageUrl
            url = favoriteShop.url
        })
        (viewPagerAdapter.fragments[VIEW_PAGER_POSITION_FAVORITE] as FavoriteFragment).updateData()
    }

    private fun onDeleteFavorite(shop: Shop) {
        showConfirmDeleteFavoriteDialog(shop)
    }
    private fun onDeleteFavorite(favoriteShop: FavoriteShop) {
        showConfirmDeleteFavoriteDialog(favoriteShop)
    }

    private fun showConfirmDeleteFavoriteDialog(shop: Shop) {
        AlertDialog.Builder(this)
            .setTitle(R.string.delete_favorite_dialog_title)
            .setMessage(R.string.delete_favorite_dialog_message)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                deleteFavorite(shop)
            }
            .setNegativeButton(android.R.string.cancel) { _, _ ->}
            .create()
            .show()
    }
    private fun showConfirmDeleteFavoriteDialog(favoriteShop: FavoriteShop) {
        AlertDialog.Builder(this)
            .setTitle(R.string.delete_favorite_dialog_title)
            .setMessage(R.string.delete_favorite_dialog_message)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                deleteFavorite(favoriteShop)
            }
            .setNegativeButton(android.R.string.cancel) { _, _ ->}
            .create()
            .show()
    }

    private fun deleteFavorite(shop: Shop) {
        FavoriteShop.delete(shop)
        (viewPagerAdapter.fragments[VIEW_PAGER_POSITION_API] as ApiFragment).updateView()
        (viewPagerAdapter.fragments[VIEW_PAGER_POSITION_FAVORITE] as FavoriteFragment).updateData()
    }

    private fun deleteFavorite(favoriteShop: FavoriteShop) {
        FavoriteShop.delete(favoriteShop)
        (viewPagerAdapter.fragments[VIEW_PAGER_POSITION_API] as ApiFragment).updateView()
        (viewPagerAdapter.fragments[VIEW_PAGER_POSITION_FAVORITE] as FavoriteFragment).updateData()
    }


    companion object {
        private const val KEY_URL = "key_url"
        fun start(activity: Activity, shop: Shop) {
            activity.startActivity(Intent(activity, WebViewActivity::class.java).putExtra(KEY_URL, shop))
        }
        fun start(activity: Activity, favoriteShop: FavoriteShop) {
            activity.startActivity(Intent(activity, WebViewActivity::class.java).putExtra(KEY_URL, favoriteShop))
        }
        private const val VIEW_PAGER_POSITION_API = 0
        private const val VIEW_PAGER_POSITION_FAVORITE = 1
    }
}

