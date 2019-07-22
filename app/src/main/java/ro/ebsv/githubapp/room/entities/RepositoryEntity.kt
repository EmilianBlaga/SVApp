package ro.ebsv.githubapp.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import ro.ebsv.githubapp.login.models.User
import ro.ebsv.githubapp.network.BaseResponse
import ro.ebsv.githubapp.repositories.models.Repository
import java.io.Serializable
import java.util.*
import kotlin.math.ceil

@Entity(tableName = "repositories")
data class RepositoryEntity (
    @PrimaryKey
    var id: Int,
    var name: String,
    var full_name: String,
    var description: String?,
    var created_at: Date,
    var updated_at: Date,
    var pushed_at: Date,
    var private_repo: Boolean,
    var owner_id: Int
) {
    fun toRepository(): Repository {
        return Repository(id, name, full_name, description, created_at, updated_at, pushed_at, private_repo, User(owner_id))
    }
}