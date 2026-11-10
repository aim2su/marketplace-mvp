package com.rebuket.mvp.data.repository

import com.rebuket.mvp.data.local.dao.SellerReviewDao
import com.rebuket.mvp.data.local.entity.SellerReviewEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface SellerReviewRepository {
    fun getReviews(sellerId: Long): Flow<List<SellerReviewEntity>>
    suspend fun addReview(sellerId: Long, rating: Int, comment: String)
}

class SellerReviewRepositoryImpl @Inject constructor(
    private val dao: SellerReviewDao
) : SellerReviewRepository {
    override fun getReviews(sellerId: Long): Flow<List<SellerReviewEntity>> = dao.getBySeller(sellerId)
    override suspend fun addReview(sellerId: Long, rating: Int, comment: String) {
        dao.insert(SellerReviewEntity(sellerId = sellerId, rating = rating, comment = comment))
    }
}