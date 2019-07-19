package ro.ebsv.githubapp.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import io.reactivex.Completable
import ro.ebsv.githubapp.login.models.User

@Dao
interface UserDao {

    @Query("SELECT * FROM user WHERE 1 LIMIT 1")
    fun fetchUser(): LiveData<User>

    @Insert
    fun insert(user: User)

    @Update
    fun update(user: User)

    @Delete
    fun delete(user: User)

    @Query("DELETE FROM user WHERE 1")
    fun deleteAll(): Completable

}