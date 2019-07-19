package ro.ebsv.githubapp.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ro.ebsv.githubapp.login.models.User
import ro.ebsv.githubapp.repositories.models.Repository
import ro.ebsv.githubapp.room.dao.RepoDao
import ro.ebsv.githubapp.room.dao.UserDao

@Database(entities = [User::class, Repository::class], version = 1)
abstract class GithubDataBase: RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun reposDao(): RepoDao
}