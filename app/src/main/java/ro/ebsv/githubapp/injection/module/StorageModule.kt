package ro.ebsv.githubapp.injection.module

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import dagger.Module
import dagger.Provides
import ro.ebsv.githubapp.data.Constants
import ro.ebsv.githubapp.room.database.GithubDataBase
import javax.inject.Named
import javax.inject.Singleton

@Module
object StorageModule {

    @Provides
    @Singleton
    @Named("SharedPreferences")
    fun provideSharedPreferences(@Named("ApplicationContext") appContext: Context): SharedPreferences {
        return appContext.getSharedPreferences(Constants.SharedPrefsKeys.USER_PREFS, Context.MODE_PRIVATE)
    }


    @Provides
    @Singleton
    @Named("GithubDataBase")
    fun provideGithubDataBase(@Named("ApplicationContext") appContext: Context): GithubDataBase {
        return Room.databaseBuilder(appContext, GithubDataBase::class.java, "github.db").build()
    }
}