package ro.ebsv.githubapp.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.sqlite.db.SimpleSQLiteQuery
import io.reactivex.disposables.CompositeDisposable
import ro.ebsv.githubapp.base.BaseViewModel
import javax.inject.Inject
import javax.inject.Named
import ro.ebsv.githubapp.data.Constants
import ro.ebsv.githubapp.managers.UserManager
import ro.ebsv.githubapp.network.ApiService
import ro.ebsv.githubapp.room.database.GithubDataBase
import ro.ebsv.githubapp.room.entities.RepositoryEntity

class RepositoryViewModel: BaseViewModel() {

    @Inject
    @field:Named("GithubApiService")
    lateinit var apiService: ApiService

    @Inject
    @field:Named("GithubDataBase")
    lateinit var dataBase: GithubDataBase

    private val reposLiveData = MutableLiveData<List<RepositoryEntity>>()
    private val repositoryLiveData = MutableLiveData<RepositoryEntity>()

    private var currentAffiliation = enumValues<Constants.Repository.Filters.Affiliation>().joinToString {it.name}
    private var currentSortCriteria = Constants.Repository.Sort.Criteria.full_name
    private lateinit var currentVisibility: Constants.Repository.Filters.Visibility

    private val compositeDisposable = CompositeDisposable()

    fun repositoriesLiveData(): LiveData<List<RepositoryEntity>> = reposLiveData
    fun repositoryLiveData(): LiveData<RepositoryEntity> = repositoryLiveData

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

    fun setRepository(repository: RepositoryEntity) {
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
                val entityReposList = repos.map { it.toRepositoryEntity() }
                //Save them only if there are all repos ELSE just show them
                if (visibility == Constants.Repository.Filters.Visibility.all &&
                        affiliation == enumValues<Constants.Repository.Filters.Affiliation>().joinToString {it.name}) {
                    val insertReposDisp = dataBase.reposDao().insertAll(entityReposList).subscribe({
                        reposLiveData.postValue(entityReposList)
                    }, {
                        reposLiveData.postValue(entityReposList)
                    })

                    compositeDisposable.add(insertReposDisp)
                } else {

                    reposLiveData.postValue(entityReposList)

                }

            },{

                var queryString = "SELECT * FROM repositories WHERE "

                if (visibility != Constants.Repository.Filters.Visibility.all)
                    queryString += "private_repo = ${(visibility == Constants.Repository.Filters.Visibility.private)} "

                val affiliationsRequested = affiliation.split(",").map { it.replace(" ", "") }


                when (affiliationsRequested.size) {
                    3 -> {
                        queryString += "1 "
                    }
                    2 -> {
                        if (!affiliationsRequested.contains(Constants.Repository.Filters.Affiliation.owner.name)) {
                            queryString += "owner_id <> ${UserManager.user?.id} "
                        }
                        else if(!affiliationsRequested.contains(Constants.Repository.Filters.Affiliation.collaborator.name)) {
                            queryString += "owner_id = ${UserManager.user?.id} "
                        }
                        else if(!affiliationsRequested.contains(Constants.Repository.Filters.Affiliation.organization_member.name)) {
                            queryString += "owner_id = -1 "
                        }
                    }
                    1 -> {
                        when (affiliationsRequested[0]) {
                            Constants.Repository.Filters.Affiliation.owner.name -> {
                                queryString += "owner_id = ${UserManager.user?.id} "
                            }
                            Constants.Repository.Filters.Affiliation.collaborator.name -> {
                                queryString += "owner_id <> ${UserManager.user?.id} "
                            }
                            Constants.Repository.Filters.Affiliation.organization_member.name -> {
                                queryString += "owner_id = -1 "
                            }
                        }
                    }
                }

                queryString += when(sort) {
                    Constants.Repository.Sort.Criteria.full_name -> {
                        "ORDER BY full_name ASC"
                    }
                    Constants.Repository.Sort.Criteria.updated -> {
                        "ORDER BY updated_at ASC"
                    }
                    Constants.Repository.Sort.Criteria.created -> {
                        "ORDER BY created_at ASC"
                    }
                    Constants.Repository.Sort.Criteria.pushed -> {
                        "ORDER BY pushed_at ASC"
                    }
                }

                val query = SimpleSQLiteQuery(queryString)
                val dbDisp = dataBase.reposDao().getRepos(query).subscribe ({
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