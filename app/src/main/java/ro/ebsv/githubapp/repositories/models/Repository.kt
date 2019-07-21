package ro.ebsv.githubapp.repositories.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import ro.ebsv.githubapp.network.BaseResponse
import java.io.Serializable

@Entity(tableName = "repositoriesLiveData")
data class Repository (
    @PrimaryKey
    var id: Int,
    var name: String,
    var full_name: String,
    var description: String
): BaseResponse(), Serializable