package ro.ebsv.githubapp.room.dao

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Single
import ro.ebsv.githubapp.login.models.User

@Dao
interface UserDao {

    @Query("SELECT * FROM user WHERE 1 LIMIT 1")
    fun fetchUser(): Single<User>

    @Insert
    fun insert(user: User): Single<Long>

    @Update
    fun update(user: User): Completable

    @Delete
    fun delete(user: User): Completable

    @Query("DELETE FROM user WHERE 1")
    fun deleteAll(): Completable

}