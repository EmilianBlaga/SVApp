package ro.ebsv.githubapp.login.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import ro.ebsv.githubapp.network.BaseResponse

@Entity(tableName = "user")
open class User (
    @PrimaryKey
    var id: Int,
    var avatar_url: String,
    var bio: String?,
    var created_at: String,
    var email: String?,
    var location: String?,
    var public_repos: Int,
    var total_private_repos: Int,
    var two_factor_authentication: Boolean,
    var updated_at: String
): BaseResponse() {

    constructor (user: User) : this(user.id, user.avatar_url, user.bio, user.created_at, user.email, user.location,
        user.public_repos, user.total_private_repos, user.two_factor_authentication, user.updated_at) {
        super.documentation_url = ""
        super.message = ""
    }

    constructor(): this(0, "", "", "", "", "", 0, 0, false, "")

    constructor (id: Int,
                 avatar_url: String,
                 bio: String?,
                 created_at: String,
                 email: String?,
                 location: String?,
                 public_repos: Int,
                 total_private_repos: Int,
                 two_factor_authentication: Boolean,
                 updated_at: String, message: String) :
    this(id,
        avatar_url,
        bio,
        created_at,
        email,
        location,
        public_repos,
        total_private_repos,
        two_factor_authentication,
        updated_at) {
        super.documentation_url = ""
        super.message = message
    }

    class Success(user: User): User(user)

    class Error(message: String): User( 0,"", "", "", "", "", 0, 0, false, "", message)
}