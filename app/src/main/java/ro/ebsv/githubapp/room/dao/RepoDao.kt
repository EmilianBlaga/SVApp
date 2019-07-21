package ro.ebsv.githubapp.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Single
import ro.ebsv.githubapp.repositories.models.Repository

@Dao
interface RepoDao {

    @Query("SELECT * FROM repos WHERE 1")
    fun getRepos(): Single<List<Repository>>

    @Query("DELETE FROM repos WHERE 1")
    fun deleteAll(): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(repos: ArrayList<Repository>): Completable
}