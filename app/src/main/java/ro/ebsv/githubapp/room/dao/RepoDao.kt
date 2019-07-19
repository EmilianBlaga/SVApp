package ro.ebsv.githubapp.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Completable
import ro.ebsv.githubapp.repositories.models.Repository

@Dao
interface RepoDao {

    @Query("SELECT * FROM repos WHERE 1")
    fun getRepos(): LiveData<List<Repository>>

    @Query("DELETE FROM repos WHERE 1")
    fun deleteAll(): Completable

    @Insert
    fun insertAll(repos: ArrayList<Repository>)
}