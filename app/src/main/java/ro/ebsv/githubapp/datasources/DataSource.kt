package ro.ebsv.githubapp.datasources

import androidx.lifecycle.LiveData
import ro.ebsv.githubapp.data.Constants
import ro.ebsv.githubapp.login.models.User
import ro.ebsv.githubapp.repositories.models.Repository

interface DataSource {
    fun getUser(): LiveData<User>

    fun getRepositories(
        visibility: Constants.Repository.Filters.Visibility,
        affiliation: String,
        sort: Constants.Repository.Sort.Criteria,
        direction: Constants.Repository.Sort.Direction
    ): LiveData<List<Repository>>

    fun disposeObservables()
}