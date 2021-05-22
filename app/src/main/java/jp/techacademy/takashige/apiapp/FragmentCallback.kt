package jp.techacademy.takashige.apiapp

interface FragmentCallback {

    fun onClickItem(shop: Shop)

    fun onClickItem(favoriteShop: FavoriteShop)

    fun onAddFavorite(shop: Shop)

    fun onAddFavorite(favoriteShop: FavoriteShop)

    fun onDeleteFavorite(shop: Shop)

    fun onDeleteFavorite(favoriteShop: FavoriteShop)
}