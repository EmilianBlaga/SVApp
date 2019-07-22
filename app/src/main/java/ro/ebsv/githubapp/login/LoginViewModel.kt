package ro.ebsv.githubapp.login

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.disposables.CompositeDisposable
import retrofit2.adapter.rxjava2.HttpException
import ro.ebsv.githubapp.base.BaseViewModel
import ro.ebsv.githubapp.data.Constants
import ro.ebsv.githubapp.login.models.User
import ro.ebsv.githubapp.managers.UserManager
import ro.ebsv.githubapp.network.ApiService
import ro.ebsv.githubapp.network.AuthInterceptor
import ro.ebsv.githubapp.room.database.GithubDataBase
import java.net.SocketTimeoutException
import javax.inject.Inject
import javax.inject.Named

class LoginViewModel: BaseViewModel() {

    @Inject
    @field:Named("GithubApiService")
    lateinit var apiService: ApiService

    @Inject
    lateinit var authInterceptor: AuthInterceptor

    @Inject
    @field:Named("SharedPreferences")
    lateinit var sharedPreferences: SharedPreferences

    @Inject
    @field:Named("GithubDataBase")
    lateinit var dataBase: GithubDataBase

    private val userLiveData = MutableLiveData<User>()

    fun userObservable(): LiveData<User> = userLiveData

    private val compositeDisposable = CompositeDisposable()

    fun authenticateUser(username: String, password: String) {

        authInterceptor.setCredentials(username, password)

        val apiDisp = apiService.getAuthenticatedUser().subscribe({user ->
            val insertUserDisp = dataBase.userDao().insert(user.toUserEntity()).subscribe ({
                saveUserCredentials(username, password)
                UserManager.user = user.toUserEntity()
                userLiveData.postValue(User.Success(user))
            }, {
                it
            })

            compositeDisposable.add(insertUserDisp)
        }, {
            userLiveData.postValue(User.Error(manageError(it)))
        })

        compositeDisposable.add(apiDisp)

    }

    private fun manageError(throwable: Throwable): String = when (throwable) {
        is SocketTimeoutException -> {
            "No Internet Connection"
        }
        is HttpException -> {

            when (throwable.code()) {
                401 -> "Username or password are incorrect"
                403, 404 -> "Not authorized"
                else -> "Something went wrong"
            }

        }
        else -> "Something went wrong"
    }

    private fun saveUserCredentials(username: String, password: String) {
        val sharedPrefsEditor = sharedPreferences.edit()
        sharedPrefsEditor.putString(Constants.SharedPrefsKeys.USER_NAME, username)
        sharedPrefsEditor.putString(Constants.SharedPrefsKeys.USER_PASS, password)
        sharedPrefsEditor.apply()
    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
        compositeDisposable.dispose()
    }
}