package ro.ebsv.githubapp.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import ro.ebsv.githubapp.login.models.User
import ro.ebsv.githubapp.network.BaseResponse
import java.io.Serializable

@Entity(tableName = "user")
data class UserEntity (
    @PrimaryKey
    var id: Int,
    var avatar_url: String?,
    var bio: String?,
    var created_at: String?,
    var email: String?,
    var location: String?,
    var public_repos: Int?,
    var total_private_repos: Int?,
    var two_factor_authentication: Boolean,
    var updated_at: String?
): Serializable {
    fun toUser(): User {
        return User(id, avatar_url, bio, created_at, email, location, public_repos, total_private_repos,
            two_factor_authentication, updated_at)
    }
}