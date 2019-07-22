package ro.ebsv.githubapp.application

import android.app.Application
import ro.ebsv.githubapp.injection.component.DaggerDependencyInjector
import ro.ebsv.githubapp.injection.component.DependencyInjector
import ro.ebsv.githubapp.injection.module.AppModule
import ro.ebsv.githubapp.injection.module.NetworkModule
import ro.ebsv.githubapp.injection.module.StorageModule

/**
 * The application component of the app. Used for dependency injection.
 */
class GithubApp: Application() {

    companion object {
        lateinit var dependencyInjector: DependencyInjector
    }

    override fun onCreate() {
        super.onCreate()

        dependencyInjector = DaggerDependencyInjector.builder()
            .appModule(AppModule(applicationContext))
            .networkModule(NetworkModule)
            .storageModule(StorageModule)
            .build()

    }

}