package ro.ebsv.githubapp.room.dao

import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import io.reactivex.Completable
import io.reactivex.Single
import ro.ebsv.githubapp.room.entities.RepositoryEntity

@Dao
interface RepoDao {

    @Query("SELECT * FROM repositories WHERE 1")
    fun getRepos(): Single<List<RepositoryEntity>>

    @RawQuery
    fun getRepos(query: SupportSQLiteQuery): Single<List<RepositoryEntity>>

    @Query("DELETE FROM repositories WHERE 1")
    fun deleteAll(): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(repos: List<RepositoryEntity>): Single<List<Long>>

}