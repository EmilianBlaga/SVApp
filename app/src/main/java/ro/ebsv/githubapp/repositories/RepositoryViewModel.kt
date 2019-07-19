package ro.ebsv.githubapp.repositories

import androidx.lifecycle.LiveData
import ro.ebsv.githubapp.base.BaseViewModel
import javax.inject.Inject
import javax.inject.Named
import ro.ebsv.githubapp.data.Constants
import ro.ebsv.githubapp.datasources.GitDataSource
import ro.ebsv.githubapp.repositories.models.Repository

class RepositoryViewModel: BaseViewModel() {

    @Inject
    @field:Named("GithubDataSource")
    lateinit var gitDataSource: GitDataSource

    private lateinit var reposLiveData: LiveData<List<Repository>>

    private var currentAffiliation = enumValues<Constants.Repository.Filters.Affiliation>().joinToString {it.name}
    private var currentSortCriteria = Constants.Repository.Sort.Criteria.full_name

    fun repos(): LiveData<List<Repository>> = reposLiveData

    fun getRepositories(visibility: Constants.Repository.Filters.Visibility = Constants.Repository.Filters.Visibility.all,
                        affiliation: String = enumValues<Constants.Repository.Filters.Affiliation>().joinToString {it.name},
                        sort: Constants.Repository.Sort.Criteria = Constants.Repository.Sort.Criteria.full_name,
                        direction: Constants.Repository.Sort.Direction = Constants.Repository.Sort.Direction.asc) {


        currentAffiliation = affiliation
        currentSortCriteria = sort

        reposLiveData = gitDataSource.getRepositories(visibility, affiliation, sort, direction)

    }

    fun getCurrentReposData(): Pair<String, Constants.Repository.Sort.Criteria> {
        return Pair(currentAffiliation, currentSortCriteria)
    }



}