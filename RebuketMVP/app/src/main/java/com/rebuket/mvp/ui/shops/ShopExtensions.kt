package com.rebuket.mvp.ui.shops

import com.rebuket.mvp.R
import com.rebuket.mvp.data.local.entity.ShopEntity

fun ShopEntity.getLogoId(): Int = when (logoRes) {
    "logo1" -> R.drawable.logo1
    "logo2" -> R.drawable.logo2
    "logo3" -> R.drawable.logo3
    "logo4" -> R.drawable.logo4
    else -> R.drawable.ic_custom_flower
}