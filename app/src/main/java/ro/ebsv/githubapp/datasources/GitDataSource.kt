package ro.ebsv.githubapp.datasources

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.adapter.rxjava2.HttpException
import ro.ebsv.githubapp.data.Constants
import ro.ebsv.githubapp.login.models.User
import ro.ebsv.githubapp.network.ApiService
import ro.ebsv.githubapp.repositories.models.Repository
import ro.ebsv.githubapp.room.database.GithubDataBase
import java.net.SocketTimeoutException

class GitDataSource(private val apiService: ApiService,
                    private val database: GithubDataBase): DataSource {

    private val compositeDisposable = CompositeDisposable()
    private val userMediator = MediatorLiveData<User>()
    private val reposMediator = MediatorLiveData<List<Repository>>()

    override fun getUser(): LiveData<User> {
        val disp = apiService.getAuthenticatedUser().observeOn(Schedulers.io()).subscribe({
            database.userDao().deleteAll().subscribe {
                database.userDao().insert(it)
            }
        },{
            //userLiveData.postValue(User.Error(manageError(it)))
        })

        compositeDisposable.add(disp)

        val databaseSource = database.userDao().fetchUser()

        userMediator.addSource(databaseSource) {
            userMediator.postValue(
                if (it != null)
                    User.Success(it)
                else
                    User.Error("User not found")
            )

        }

        return userMediator
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

    override fun getRepositories(
        visibility: Constants.Repository.Filters.Visibility,
        affiliation: String,
        sort: Constants.Repository.Sort.Criteria,
        direction: Constants.Repository.Sort.Direction): LiveData<List<Repository>> {

        val disp = apiService.getRepositories(visibility, affiliation, sort, direction).observeOn(Schedulers.io())
            .subscribe({
                database.reposDao().deleteAll().subscribe {
                    database.reposDao().insertAll(it)
                }
            },{})

        compositeDisposable.add(disp)

        val databaseSource = database.reposDao().getRepos()

        reposMediator.addSource(databaseSource) {
            reposMediator.postValue(it)
        }

        return reposMediator
    }

    override fun disposeObservables() {
        compositeDisposable.clear()
        compositeDisposable.dispose()
    }
}