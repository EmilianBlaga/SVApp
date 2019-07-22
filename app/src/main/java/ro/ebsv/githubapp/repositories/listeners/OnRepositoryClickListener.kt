package ro.ebsv.githubapp.repositories.listeners

import ro.ebsv.githubapp.room.entities.RepositoryEntity

interface OnRepositoryClickListener {

    fun onRepositoryClicked(repository: RepositoryEntity)

}