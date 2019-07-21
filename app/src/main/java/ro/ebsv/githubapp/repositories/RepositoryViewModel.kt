package ro.ebsv.githubapp.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.disposables.CompositeDisposable
import ro.ebsv.githubapp.base.BaseViewModel
import javax.inject.Inject
import javax.inject.Named
import ro.ebsv.githubapp.data.Constants
import ro.ebsv.githubapp.datasources.GitDataSource
import ro.ebsv.githubapp.network.ApiService
import ro.ebsv.githubapp.repositories.models.Repository
import ro.ebsv.githubapp.room.database.GithubDataBase

class RepositoryViewModel: BaseViewModel() {

    @Inject
    @field:Named("GithubApiService")
    lateinit var apiService: ApiService

    @Inject
    @field:Named("GithubDataBase")
    lateinit var dataBase: GithubDataBase

    private val reposLiveData = MutableLiveData<List<Repository>>()

    private var currentAffiliation = enumValues<Constants.Repository.Filters.Affiliation>().joinToString {it.name}
    private var currentSortCriteria = Constants.Repository.Sort.Criteria.full_name

    private val compositeDisposable = CompositeDisposable()

    fun repos(): LiveData<List<Repository>> = reposLiveData

    fun getRepositories(visibility: Constants.Repository.Filters.Visibility = Constants.Repository.Filters.Visibility.all,
                        affiliation: String = enumValues<Constants.Repository.Filters.Affiliation>().joinToString {it.name},
                        sort: Constants.Repository.Sort.Criteria = Constants.Repository.Sort.Criteria.full_name,
                        direction: Constants.Repository.Sort.Direction = Constants.Repository.Sort.Direction.asc) {


        currentAffiliation = affiliation
        currentSortCriteria = sort

        val apiDisp = apiService.getRepositories(visibility, affiliation, sort, direction)
            .subscribe({
                val insertReposDisp = dataBase.reposDao().insertAll(it).subscribe {
                    reposLiveData.postValue(it)
                }

                compositeDisposable.add(insertReposDisp)
            },{
                val dbDisp = dataBase.reposDao().getRepos().subscribe ({
                    reposLiveData.postValue(it)
                },{
                    //No repos found
                })

                compositeDisposable.add(dbDisp)
            })

        compositeDisposable.add(apiDisp)

    }

    fun getCurrentReposData(): Pair<String, Constants.Repository.Sort.Criteria> {
        return Pair(currentAffiliation, currentSortCriteria)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
        compositeDisposable.dispose()
    }

}