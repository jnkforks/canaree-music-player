package dev.olog.data

import android.arch.persistence.room.Room
import android.content.Context
import com.squareup.sqlbrite2.BriteContentResolver
import com.squareup.sqlbrite2.SqlBrite
import dagger.Module
import dagger.Provides
import dev.olog.data.db.AppDatabase
import dev.olog.shared.ApplicationContext
import io.reactivex.schedulers.Schedulers
import javax.inject.Singleton

@Module
class RepositoryHelperModule {

    @Provides
    @Singleton
    fun provideSqlBrite(): SqlBrite = SqlBrite.Builder().build()

    @Provides
    @Singleton
    fun provideBriteContentResolver(@ApplicationContext context: Context,
                                    sqlBrite: SqlBrite) : BriteContentResolver {

        return sqlBrite.wrapContentProvider(context.contentResolver, Schedulers.io())
    }

    @Provides
    @Singleton
    fun provideRoomDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "db")
                .build()
    }

}