package ro.ebsv.githubapp.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ro.ebsv.githubapp.room.converters.Converters
import ro.ebsv.githubapp.room.dao.RepoDao
import ro.ebsv.githubapp.room.dao.UserDao
import ro.ebsv.githubapp.room.entities.RepositoryEntity
import ro.ebsv.githubapp.room.entities.UserEntity

@Database(entities = [UserEntity::class, RepositoryEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class GithubDataBase: RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun reposDao(): RepoDao
}