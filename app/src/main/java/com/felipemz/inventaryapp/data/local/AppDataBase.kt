package com.felipemz.inventaryapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.felipemz.inventaryapp.core.DATABASE_NAME
import com.felipemz.inventaryapp.data.local.dao.CategoryDao
import com.felipemz.inventaryapp.data.local.dao.MovementDao
import com.felipemz.inventaryapp.data.local.dao.ProductPackageDao
import com.felipemz.inventaryapp.data.local.dao.ProductDao
import com.felipemz.inventaryapp.data.local.entity.CategoryEntity
import com.felipemz.inventaryapp.data.local.entity.MovementItemEntity
import com.felipemz.inventaryapp.data.local.entity.ProductPackageEntity
import com.felipemz.inventaryapp.data.local.entity.ProductEntity

@Database(
    entities = [
        ProductEntity::class,
        CategoryEntity::class,
        MovementItemEntity::class,
        ProductPackageEntity::class,
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun categoryDao(): CategoryDao
    abstract fun movementDao(): MovementDao
    abstract fun productCompositionDao(): ProductPackageDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}