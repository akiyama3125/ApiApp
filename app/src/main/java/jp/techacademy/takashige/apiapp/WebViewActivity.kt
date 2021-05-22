package jp.techacademy.takashige.apiapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle

import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_web_view.*


class WebViewActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)
        val state = intent.getSerializableExtra(KEY_URL)

        var isFavorite = true

        if (state is Shop) {
            isFavorite = FavoriteShop.findBy(state.id) != null
            if (isFavorite == true) {
                button1.text = "削除"
            } else {
                button1.text = "登録"
            }
            if (state.couponUrls.sp.isNotEmpty()) {
                webView.loadUrl(state.couponUrls.sp)
            } else {
                webView.loadUrl(state.couponUrls.pc)
            }
        } else if (state is FavoriteShop) {
            button1.text = "削除"
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
    }

    private fun onAddFavorite(favoriteShop: FavoriteShop) {
        FavoriteShop.insert(FavoriteShop().apply {
            id = favoriteShop.id
            name = favoriteShop.name
            imageUrl = favoriteShop.imageUrl
            url = favoriteShop.url
        })
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
    }

    private fun deleteFavorite(favoriteShop: FavoriteShop) {
        FavoriteShop.delete(favoriteShop)
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

