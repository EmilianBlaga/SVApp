package ro.ebsv.githubapp.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.sqlite.db.SimpleSQLiteQuery
import io.reactivex.disposables.CompositeDisposable
import ro.ebsv.githubapp.BR
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

    private var currentAffiliation = enumValues<Constants.Repository.Filters.Affiliation>().joinToString {it.name}
    private var currentSortCriteria = Constants.Repository.Sort.Criteria.full_name
    private lateinit var currentVisibility: Constants.Repository.Filters.Visibility

    private val compositeDisposable = CompositeDisposable()

    val reposLiveData = MutableLiveData<List<RepositoryEntity>>()
    fun repositoriesLiveData(): LiveData<List<RepositoryEntity>> = reposLiveData

    val repositoryLiveData = MutableLiveData<RepositoryEntity>()
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

        val apiDisp = apiService.getRepositories(visibility.getValue(), affiliation, sort, direction)
            .subscribe({repos ->
                val entityReposList = repos.map { it.toRepositoryEntity() }

                val insertReposDisp = dataBase.reposDao().insertAll(entityReposList).subscribe({
                    reposLiveData.postValue(entityReposList)
                }, {
                    reposLiveData.postValue(entityReposList)
                })

                compositeDisposable.add(insertReposDisp)

            },{

                var queryString = "SELECT * FROM repositories WHERE "

                queryString += when (visibility) {
                    Constants.Repository.Filters.Visibility.all_repos -> {
                        "private_repo IN (0, 1) AND "
                    }
                    Constants.Repository.Filters.Visibility.private_repos -> {
                        "private_repo = 1 AND "
                    }
                    Constants.Repository.Filters.Visibility.public_repos -> {
                        "private_repo = 0 AND "
                    }
                }

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