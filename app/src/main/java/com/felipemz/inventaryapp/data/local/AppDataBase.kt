package com.felipemz.inventaryapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.felipemz.inventaryapp.core.DATABASE_NAME
import com.felipemz.inventaryapp.data.local.dao.CategoryDao
import com.felipemz.inventaryapp.data.local.dao.MovementDao
import com.felipemz.inventaryapp.data.local.dao.ProductCompositionDao
import com.felipemz.inventaryapp.data.local.dao.ProductDao
import com.felipemz.inventaryapp.data.local.entity.ProductCompositionEntity
import com.felipemz.inventaryapp.domain.model.CategoryModel
import com.felipemz.inventaryapp.domain.model.MovementItemModel
import com.felipemz.inventaryapp.domain.model.ProductModel

@Database(
    entities = [
        ProductModel::class,
        CategoryModel::class,
        MovementItemModel::class,
        ProductCompositionEntity::class,
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun categoryDao(): CategoryDao
    abstract fun movementDao(): MovementDao
    abstract fun productCompositionDao(): ProductCompositionDao

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