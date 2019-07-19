package ro.ebsv.githubapp.splash

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import ro.ebsv.githubapp.base.BaseViewModel
import ro.ebsv.githubapp.data.Constants
import ro.ebsv.githubapp.datasources.GitDataSource
import ro.ebsv.githubapp.login.models.User
import ro.ebsv.githubapp.network.AuthInterceptor
import javax.inject.Inject
import javax.inject.Named

class SplashViewModel: BaseViewModel() {

    @Inject
    @field:Named("SharedPreferences")
    lateinit var sharedPreferences: SharedPreferences

    @Inject
    @field:Named("GithubDataSource")
    lateinit var gitDataSource: GitDataSource

    @Inject
    lateinit var authInterceptor: AuthInterceptor

    init {
        if (sharedPreferences.contains(Constants.SharedPrefsKeys.USER_NAME) &&
            sharedPreferences.contains(Constants.SharedPrefsKeys.USER_PASS)) {

            val username = sharedPreferences.getString(Constants.SharedPrefsKeys.USER_NAME, null)
            val password = sharedPreferences.getString(Constants.SharedPrefsKeys.USER_PASS, null)

            authInterceptor.setCredentials(username!!, password!!)
        }
    }

    fun userObservable(): LiveData<User> = gitDataSource.getUser()

}