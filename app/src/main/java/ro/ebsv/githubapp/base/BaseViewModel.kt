package ro.ebsv.githubapp.base

import androidx.lifecycle.ViewModel
import ro.ebsv.githubapp.application.GithubApp
import ro.ebsv.githubapp.login.mvvm.LoginViewModel
import ro.ebsv.githubapp.main.MainViewModel
import ro.ebsv.githubapp.repositories.RepositoryViewModel
import ro.ebsv.githubapp.splash.SplashViewModel

abstract class BaseViewModel: ViewModel() {

    init {
        inject()
    }

    private fun inject() {
        when (this) {
            is MainViewModel -> {
                GithubApp.dependencyInjector.inject(this)
            }
            is LoginViewModel -> {
                GithubApp.dependencyInjector.inject(this)
            }
            is SplashViewModel -> {
                GithubApp.dependencyInjector.inject(this)
            }
            is RepositoryViewModel -> {
                GithubApp.dependencyInjector.inject(this)
            }
        }
    }

}