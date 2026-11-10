package com.rebuket.mvp.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.rebuket.mvp.data.local.dao.CartDao
import com.rebuket.mvp.data.local.dao.EcoDao
import com.rebuket.mvp.data.local.dao.OrderDao
import com.rebuket.mvp.data.local.dao.ProductDao
import com.rebuket.mvp.data.local.dao.ShopDao
import com.rebuket.mvp.data.local.dao.SellerReviewDao
import com.rebuket.mvp.data.local.entity.CartEntity
import com.rebuket.mvp.data.local.entity.EcoBouquetEntity
import com.rebuket.mvp.data.local.entity.OrderEntity
import com.rebuket.mvp.data.local.entity.ProductEntity
import com.rebuket.mvp.data.local.entity.ShopEntity
import com.rebuket.mvp.data.local.entity.SellerReviewEntity

@Database(
    entities = [ProductEntity::class, ShopEntity::class, CartEntity::class, OrderEntity::class, EcoBouquetEntity::class, SellerReviewEntity::class],
    version = 13,
    exportSchema = false
)
abstract class RebuketDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun shopDao(): ShopDao
    abstract fun cartDao(): CartDao
    abstract fun orderDao(): OrderDao
    abstract fun ecoDao(): EcoDao
    abstract fun sellerReviewDao(): SellerReviewDao

    companion object {
        @Volatile
        private var INSTANCE: RebuketDatabase? = null

        private const val SHOPS_SQL = "INSERT INTO shops (id, nameRu, nameTj, nameEn, address, logoRes, descriptionRu, descriptionTj, descriptionEn, isOpen, verified, deliveryTime, minOrder, deliveryFee, hours) VALUES " +
            "(1, 'Настроение', 'Кайфият', 'Mood', 'Душанбе, ул. Рудаки 45', 'logo1', 'Уютный цветочный магазин в центре Душанбе. Свежие цветы каждый день.', 'Дӯкони гулҳои бароҳат дар маркази Душанбе.', 'Cozy flower shop in the center of Dushanbe.', 1, 1, '2 часа', 300, 30, '09:00 – 21:00')," +
            "(2, 'Розы и Пионы', 'Садбарг ва Пионҳо', 'Roses and Peonies', 'Худжанд, ул. Ленина 12', 'logo2', 'Классические и авторские букеты из роз и пионов.', 'Букетҳои классикӣ ва муаллифӣ аз садбарг ва пионҳо.', 'Classic and custom bouquets of roses and peonies.', 1, 0, '3 часа', 250, 30, '10:00 – 20:00')," +
            "(3, 'Флёр', 'Флёр', 'Fleur', 'Душанбе, ул. Сомони 8', 'logo3', 'Авторские букеты и композиции. Работаем с частными флористами.', 'Букетҳои муаллифӣ ва композицияҳо.', 'Custom bouquets and compositions.', 0, 1, '2 часа', 400, 40, '09:00 – 22:00')," +
            "(4, 'Букет-Мастер', 'Букет-Мастер', 'Bouquet Master', 'Худжанд, ул. Сомони 5', 'logo4', 'Мастерская авторских букетов. Индивидуальный подход к каждому.', 'Коргоҳи букетҳои муаллифӣ.', 'Custom bouquet workshop.', 1, 0, '4 часа', 200, 30, '10:00 – 18:00')"

        private const val PRODUCTS_SQL = "INSERT INTO products (id, nameRu, nameTj, nameEn, price, shopId, imageUrl, description, isFavorite, oldPrice, rating, reviewCount, deliveryHours, inStock, category) VALUES " +
            "(1, 'Букет из 25 роз', 'Дастаи 25 садбарг', 'Bouquet of 25 roses', 420.0, 1, '', '25 красных роз в крафтовой упаковке. Свежие, ароматные, с открыткой в подарок.', 0, 520.0, 4.8, 127, 2, 1, 'Розы')," +
            "(2, 'Гортензии', 'Гортензияҳо', 'Hydrangeas', 500.0, 4, '', 'Пышные гортензии в шляпной коробке. Идеально для подарка близкому человеку.', 0, NULL, 4.5, 43, 2, 1, 'Гортензии')," +
            "(3, 'Композиция в коробке', 'Таркиб дар қуттӣ', 'Box composition', 560.0, 1, '', 'Сезонные цветы в шляпной коробке. Красиво и практично.', 0, NULL, 4.9, 89, 3, 1, 'Композиции')," +
            "(4, 'Пионы, 11 шт.', 'Пионҳо, 11 дона', 'Peonies, 11 pcs.', 380.0, 2, '', 'Букет из 11 пышных пионов. Сезонный хит.', 0, 450.0, 4.9, 156, 2, 1, 'Пионы')," +
            "(5, 'Тюльпаны, 25 шт.', 'Лолаҳо, 25 дона', 'Tulips, 25 pcs.', 310.0, 1, '', '25 ярких тюльпанов. Весеннее настроение.', 0, NULL, 4.7, 78, 2, 1, 'Тюльпаны')," +
            "(6, 'Букет «Проверка фото», 15 роз', 'Дастаи 15 садбарг', 'Bouquet 15 roses', 420.0, 4, '', '15 роз в красивой упаковке.', 0, NULL, 4.0, 12, 2, 0, 'Розы')"

        private val MIGRATION_12_13 = object : Migration(12, 13) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE products ADD COLUMN oldPrice REAL")
                db.execSQL("ALTER TABLE products ADD COLUMN rating REAL NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE products ADD COLUMN reviewCount INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE products ADD COLUMN deliveryHours INTEGER")
                db.execSQL("ALTER TABLE products ADD COLUMN inStock INTEGER NOT NULL DEFAULT 1")
                db.execSQL("ALTER TABLE products ADD COLUMN category TEXT NOT NULL DEFAULT 'Все'")
                db.execSQL("ALTER TABLE shops ADD COLUMN isOpen INTEGER NOT NULL DEFAULT 1")
                db.execSQL("ALTER TABLE shops ADD COLUMN verified INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE shops ADD COLUMN deliveryTime TEXT NOT NULL DEFAULT '2 часа'")
                db.execSQL("ALTER TABLE shops ADD COLUMN minOrder INTEGER NOT NULL DEFAULT 300")
                db.execSQL("ALTER TABLE shops ADD COLUMN deliveryFee INTEGER NOT NULL DEFAULT 30")
                db.execSQL("ALTER TABLE shops ADD COLUMN hours TEXT NOT NULL DEFAULT '09:00 – 21:00'")
                db.execSQL("DELETE FROM products")
                db.execSQL("DELETE FROM shops")
                db.execSQL("DELETE FROM cart")
                db.execSQL(SHOPS_SQL)
                db.execSQL(PRODUCTS_SQL)
            }
        }

        fun getDatabase(context: Context): RebuketDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RebuketDatabase::class.java,
                    "rebuket_database"
                )
                .addMigrations(MIGRATION_12_13)
                .addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        db.execSQL(SHOPS_SQL)
                        db.execSQL(PRODUCTS_SQL)
                    }
                }).build()
                INSTANCE = instance
                instance
            }
        }
    }
}