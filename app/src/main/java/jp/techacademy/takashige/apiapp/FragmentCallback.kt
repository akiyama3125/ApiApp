package jp.techacademy.takashige.apiapp

interface FragmentCallback {

    fun onClickItem(shop: Shop)

    fun onClickItem(favoriteShop: FavoriteShop)

    fun onAddFavorite(shop: Shop)

    fun onDeleteFavorite(id: String)
}