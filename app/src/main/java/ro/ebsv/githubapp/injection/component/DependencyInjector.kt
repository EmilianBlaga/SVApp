package ro.ebsv.githubapp.injection.component

import dagger.Component
import ro.ebsv.githubapp.injection.module.AppModule
import ro.ebsv.githubapp.injection.module.NetworkModule
import ro.ebsv.githubapp.injection.module.StorageModule
import ro.ebsv.githubapp.login.LoginViewModel
import ro.ebsv.githubapp.main.MainViewModel
import ro.ebsv.githubapp.repositories.RepositoryViewModel
import ro.ebsv.githubapp.splash.SplashActivity
import ro.ebsv.githubapp.splash.SplashViewModel
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, StorageModule::class, AppModule::class])
interface DependencyInjector {

    fun inject(mainViewModel: MainViewModel)
    fun inject(splashActivity: SplashViewModel)
    fun inject(loginViewModel: LoginViewModel)
    fun inject(repositoryViewModel: RepositoryViewModel)

    @Component.Builder
    interface Builder {
        fun build(): DependencyInjector

        fun networkModule(networkModule: NetworkModule): Builder
        fun storageModule(storageModule: StorageModule): Builder
        fun appModule(appModule: AppModule): Builder
    }
}