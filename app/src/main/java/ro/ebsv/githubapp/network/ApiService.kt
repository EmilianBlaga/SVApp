package ro.ebsv.githubapp.network

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ro.ebsv.githubapp.login.models.User
import ro.ebsv.githubapp.data.Constants.Repository.Filters
import ro.ebsv.githubapp.data.Constants.Repository.Sort
import ro.ebsv.githubapp.repositories.models.Repository

interface ApiService {

    @GET("/users/{username}")
    fun getUser(@Path("username") username: String): Single<User>

    @GET("/user")
    fun getAuthenticatedUser(): Single<User>

    @GET("/user/repos")
    fun getRepositories(@Query("visibility") visibility: Filters.Visibility = Filters.Visibility.all,
                        @Query("affiliation") affiliation: String = enumValues<Filters.Affiliation>().joinToString {it.name},
                        @Query("sort") sort: Sort.Criteria = Sort.Criteria.full_name,
                        @Query("direction") direction: Sort.Direction = Sort.Direction.asc): Single<ArrayList<Repository>>
}