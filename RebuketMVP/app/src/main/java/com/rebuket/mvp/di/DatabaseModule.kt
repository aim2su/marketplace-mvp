package com.rebuket.mvp.di

import android.content.Context
import com.rebuket.mvp.data.local.dao.CartDao
import com.rebuket.mvp.data.local.dao.EcoDao
import com.rebuket.mvp.data.local.dao.OrderDao
import com.rebuket.mvp.data.local.dao.ProductDao
import com.rebuket.mvp.data.local.dao.ShopDao
import com.rebuket.mvp.data.local.dao.SellerReviewDao
import com.rebuket.mvp.data.local.database.RebuketDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): RebuketDatabase {
        return RebuketDatabase.getDatabase(context)
    }

    @Provides
    fun provideProductDao(db: RebuketDatabase): ProductDao = db.productDao()

    @Provides
    fun provideShopDao(db: RebuketDatabase): ShopDao = db.shopDao()

    @Provides
    fun provideCartDao(db: RebuketDatabase): CartDao = db.cartDao()

    @Provides
    fun provideOrderDao(db: RebuketDatabase): OrderDao = db.orderDao()

    @Provides
    fun provideEcoDao(db: RebuketDatabase): EcoDao = db.ecoDao()

    @Provides
    fun provideSellerReviewDao(db: RebuketDatabase): SellerReviewDao = db.sellerReviewDao()
}