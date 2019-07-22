package ro.ebsv.githubapp.splash

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.disposables.CompositeDisposable
import retrofit2.adapter.rxjava2.HttpException
import ro.ebsv.githubapp.base.BaseViewModel
import ro.ebsv.githubapp.data.Constants
import ro.ebsv.githubapp.login.models.User
import ro.ebsv.githubapp.network.ApiService
import ro.ebsv.githubapp.network.AuthInterceptor
import ro.ebsv.githubapp.room.database.GithubDataBase
import java.net.SocketTimeoutException
import javax.inject.Inject
import javax.inject.Named

class SplashViewModel: BaseViewModel() {

    @Inject
    @field:Named("SharedPreferences")
    lateinit var sharedPreferences: SharedPreferences

    @Inject
    @field:Named("GithubApiService")
    lateinit var apiService: ApiService

    @Inject
    @field:Named("GithubDataBase")
    lateinit var dataBase: GithubDataBase

    @Inject
    lateinit var authInterceptor: AuthInterceptor

    private val userLiveData = MutableLiveData<User>()

    private val compositeDisposable = CompositeDisposable()

    init {
        if (sharedPreferences.contains(Constants.SharedPrefsKeys.USER_NAME) &&
            sharedPreferences.contains(Constants.SharedPrefsKeys.USER_PASS)) {

            val username = sharedPreferences.getString(Constants.SharedPrefsKeys.USER_NAME, null)
            val password = sharedPreferences.getString(Constants.SharedPrefsKeys.USER_PASS, null)

            authInterceptor.setCredentials(username!!, password!!)
        } else {
            authInterceptor.setCredentials("", "")
        }
    }

    fun checkUser() {
        val apiDisp = apiService.getAuthenticatedUser().subscribe({
            userLiveData.postValue(User.Success(it))
        }, {
            val dbDisp = dataBase.userDao().fetchUser().subscribe ({
                userLiveData.postValue(User.Success(it.toUser()))
            }, {
                userLiveData.postValue(User.Error(manageError(it)))
            })

            compositeDisposable.add(dbDisp)
        })

        compositeDisposable.add(apiDisp)
    }

    private fun manageError(throwable: Throwable): String {

        when (throwable) {
            is SocketTimeoutException -> {
                return "No Internet Connection"
            }
            is HttpException -> {

                when (throwable.code()) {
                    401 -> return "Username or password are incorrect"
                    403, 404 -> return "Not authorized"
                    else -> return "Something went wrong"
                }

            }
            else -> return "Something went wrong"
        }
    }

    fun userObservable(): LiveData<User> = userLiveData

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
        compositeDisposable.dispose()
    }

}