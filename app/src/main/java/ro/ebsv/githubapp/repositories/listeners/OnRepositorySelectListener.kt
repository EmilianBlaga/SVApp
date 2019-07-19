package ro.ebsv.githubapp.repositories.listeners

import ro.ebsv.githubapp.repositories.models.Repository

interface OnRepositorySelectListener {

    fun onRepositorySelected(repository: Repository)

}