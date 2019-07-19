package ro.ebsv.githubapp.repositories.listeners

import ro.ebsv.githubapp.repositories.models.Repository

interface OnRepositoryClickListener {

    fun onRepositoryClicked(repository: Repository)

}