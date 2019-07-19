package ro.ebsv.githubapp.settings.listeners

import ro.ebsv.githubapp.data.Constants

interface OnFiltersSelectListener {

    fun onFiltersSelected(filters: String)
    fun onSortCriteriaSelected(criteria: Constants.Repository.Sort.Criteria)

}