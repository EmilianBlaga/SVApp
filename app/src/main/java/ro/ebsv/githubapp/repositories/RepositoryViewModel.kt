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
    private val repositoryLiveData = MutableLiveData<Repository>()

    private var currentAffiliation = enumValues<Constants.Repository.Filters.Affiliation>().joinToString {it.name}
    private var currentSortCriteria = Constants.Repository.Sort.Criteria.full_name
    private lateinit var currentVisibility: Constants.Repository.Filters.Visibility

    private val compositeDisposable = CompositeDisposable()

    fun repositoriesLiveData(): LiveData<List<Repository>> = reposLiveData
    fun repositoryLiveData(): LiveData<Repository> = repositoryLiveData

    fun getCurrentReposData(): Pair<String, Constants.Repository.Sort.Criteria> {
        return Pair(currentAffiliation, currentSortCriteria)
    }

    fun setVisibility(visibility: Constants.Repository.Filters.Visibility) {
        currentVisibility = visibility
    }

    fun setAffiliation (affiliation: String) {
        currentAffiliation = affiliation
    }

    fun setSortCriteria(sortCriteria: Constants.Repository.Sort.Criteria) {
        currentSortCriteria = sortCriteria
    }

    fun setRepository(repository: Repository) {
        repositoryLiveData.value = repository
    }

    fun getRepositories(affiliation: String = currentAffiliation,
                        sort: Constants.Repository.Sort.Criteria = currentSortCriteria,
                        visibility: Constants.Repository.Filters.Visibility = currentVisibility,
                        direction: Constants.Repository.Sort.Direction = Constants.Repository.Sort.Direction.asc) {

        currentVisibility = visibility
        currentAffiliation = affiliation
        currentSortCriteria = sort

        val apiDisp = apiService.getRepositories(visibility, affiliation, sort, direction)
            .subscribe({repos ->
                val insertReposDisp = dataBase.reposDao().insertAll(repos).subscribe ({
                    reposLiveData.postValue(repos)
                }, {
                    reposLiveData.postValue(repos)
                })

                compositeDisposable.add(insertReposDisp)
            },{
                val dbDisp = dataBase.reposDao().getRepos().subscribe ({
                    reposLiveData.postValue(it)
                },{
                    //No repositoriesLiveData found
                })

                compositeDisposable.add(dbDisp)
            })

        compositeDisposable.add(apiDisp)

    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
        compositeDisposable.dispose()
    }

}