package ro.ebsv.githubapp.login

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ro.ebsv.githubapp.base.BaseViewModel
import ro.ebsv.githubapp.data.Constants
import ro.ebsv.githubapp.datasources.GitDataSource
import ro.ebsv.githubapp.login.models.User
import ro.ebsv.githubapp.managers.UserManager
import ro.ebsv.githubapp.network.AuthInterceptor
import javax.inject.Inject
import javax.inject.Named

class LoginViewModel: BaseViewModel() {

    @Inject
    @field:Named("GithubDataSource")
    lateinit var gitDataSource: GitDataSource

    @Inject
    lateinit var authInterceptor: AuthInterceptor

    @Inject
    @field:Named("SharedPreferences")
    lateinit var sharedPreferences: SharedPreferences

    private val messageLiveData = MutableLiveData<String>()
    private val successAuthLiveData = MutableLiveData<Boolean>()

    fun message(): LiveData<String> = messageLiveData
    fun auth(): LiveData<Boolean> = successAuthLiveData

    fun authenticateUser(username: String, password: String) {

        authInterceptor.setCredentials(username, password)

        gitDataSource.getUser().observeForever { user ->
            when(user) {
                is User.Success -> {
                    saveUserCredentials(username, password)
                    UserManager.user = user
                    successAuthLiveData.value = true
                }
                is User.Error -> {
                    messageLiveData.value = user.message
                }
            }
        }

    }

    private fun saveUserCredentials(username: String, password: String) {
        val sharedPrefsEditor = sharedPreferences.edit()
        sharedPrefsEditor.putString(Constants.SharedPrefsKeys.USER_NAME, username)
        sharedPrefsEditor.putString(Constants.SharedPrefsKeys.USER_PASS, password)
        sharedPrefsEditor.apply()
    }

}