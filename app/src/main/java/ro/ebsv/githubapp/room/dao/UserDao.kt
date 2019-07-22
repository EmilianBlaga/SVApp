package ro.ebsv.githubapp.room.dao

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Single
import ro.ebsv.githubapp.login.models.User
import ro.ebsv.githubapp.room.entities.UserEntity

@Dao
interface UserDao {

    @Query("SELECT * FROM user WHERE 1 LIMIT 1")
    fun fetchUser(): Single<UserEntity>

    @Insert
    fun insert(user: UserEntity): Single<Long>

    @Update
    fun update(user: UserEntity): Completable

    @Delete
    fun delete(user: UserEntity): Completable

    @Query("DELETE FROM user WHERE 1")
    fun deleteAll(): Completable

}