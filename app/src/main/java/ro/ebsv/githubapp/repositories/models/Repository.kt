package ro.ebsv.githubapp.repositories.models

import ro.ebsv.githubapp.login.models.User
import ro.ebsv.githubapp.network.BaseResponse
import ro.ebsv.githubapp.room.entities.RepositoryEntity
import java.io.Serializable
import java.util.*

data class Repository (
    var id: Int,
    var name: String,
    var full_name: String,
    var description: String?,
    var created_at: Date,
    var updated_at: Date,
    var pushed_at: Date,
    var private: Boolean,
    var owner: User
): BaseResponse(), Serializable {

    fun toRepositoryEntity(): RepositoryEntity {
        return RepositoryEntity(id, name, full_name, description, created_at, updated_at, pushed_at, private, owner.id)
    }

}