package com.rebuket.mvp.data.local.entity
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "eco_bouquets")
data class EcoBouquetEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val titleRu: String,
    val titleTj: String,
    val titleEn: String,
    val price: Double,
    val contact: String
)