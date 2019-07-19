package ro.ebsv.githubapp.injection.module

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module
class AppModule(private val appContext: Context) {

    @Provides
    @Singleton
    @Named("ApplicationContext")
    fun provideApplicationContext(): Context = appContext



}