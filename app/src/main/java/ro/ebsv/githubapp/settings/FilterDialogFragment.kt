package ro.ebsv.githubapp.settings

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ro.ebsv.githubapp.R
import ro.ebsv.githubapp.data.Constants
import ro.ebsv.githubapp.databinding.FragmentFilterBinding
import ro.ebsv.githubapp.settings.adapters.FiltersListAdapter
import ro.ebsv.githubapp.settings.listeners.OnFiltersSelectListener
import ro.ebsv.githubapp.settings.models.Filter

class FilterDialogFragment: DialogFragment() {

    private lateinit var filtersSelectListener: OnFiltersSelectListener
    private lateinit var binder: FragmentFilterBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)

        filtersSelectListener = context as OnFiltersSelectListener

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binder = DataBindingUtil.inflate(inflater, R.layout.fragment_filter, container, false)
        binder.fragment = this
        return binder.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        getFilters()

    }

    override fun onStart() {
        super.onStart()
        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog?.window?.setLayout(width, height)
    }

    private fun setupRecyclerView() {
        binder.filtersLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binder.filtersAdapter = FiltersListAdapter()
    }

    private fun getFilters() {

        val selectedFiltersString = arguments?.getString(Constants.Repository.BundleKeys.DEFAULT_FILTER)
        val selectedFilters = selectedFiltersString?.split(",")?.map { it.replace(" ", "") }

        val filtersName = resources.getStringArray(R.array.filters)

        val filters = ArrayList<Filter>()

        filters.add(Filter(filtersName[0], Constants.Repository.Filters.Affiliation.owner,
            selectedFilters!!.any { it == filtersName[0].toLowerCase() }))

        filters.add(Filter(filtersName[1], Constants.Repository.Filters.Affiliation.collaborator,
            selectedFilters.any { it == filtersName[1].toLowerCase() }))

        filters.add(Filter(filtersName[2], Constants.Repository.Filters.Affiliation.organization_member,
            selectedFilters.any { it == filtersName[2].toLowerCase().replace(" ", "_") }))


        binder.filtersAdapter?.setFilters(filters)
    }

    fun onCancelClicked() {
        dismiss()
    }

    fun onOkClicked() {
        filtersSelectListener.onFiltersSelected(binder.filtersAdapter?.getFilterFieldKey() ?: "")
        dismiss()
    }

}