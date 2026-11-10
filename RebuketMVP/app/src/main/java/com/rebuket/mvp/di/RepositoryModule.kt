package com.rebuket.mvp.di

import com.rebuket.mvp.data.repository.CartRepository
import com.rebuket.mvp.data.repository.CartRepositoryImpl
import com.rebuket.mvp.data.repository.CatalogRepository
import com.rebuket.mvp.data.repository.CatalogRepositoryImpl
import com.rebuket.mvp.data.repository.EcoRepository
import com.rebuket.mvp.data.repository.EcoRepositoryImpl
import com.rebuket.mvp.data.repository.OrderRepository
import com.rebuket.mvp.data.repository.OrderRepositoryImpl
import com.rebuket.mvp.data.repository.SellerReviewRepository
import com.rebuket.mvp.data.repository.SellerReviewRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds abstract fun bindCatalogRepository(impl: CatalogRepositoryImpl): CatalogRepository
    @Binds abstract fun bindCartRepository(impl: CartRepositoryImpl): CartRepository
    @Binds abstract fun bindOrderRepository(impl: OrderRepositoryImpl): OrderRepository
    @Binds abstract fun bindEcoRepository(impl: EcoRepositoryImpl): EcoRepository
    @Binds abstract fun bindSellerReviewRepository(impl: SellerReviewRepositoryImpl): SellerReviewRepository
}