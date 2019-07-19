package ro.ebsv.githubapp.settings.models

import ro.ebsv.githubapp.data.Constants

data class Filter (
    var name: String,
    var key: Constants.Repository.Filters.Affiliation,
    var selected: Boolean = false
)