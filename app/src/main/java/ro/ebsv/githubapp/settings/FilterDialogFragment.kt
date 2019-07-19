package ro.ebsv.githubapp.settings

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_filter.*
import ro.ebsv.githubapp.R
import ro.ebsv.githubapp.data.Constants
import ro.ebsv.githubapp.settings.adapters.FiltersListAdapter
import ro.ebsv.githubapp.settings.listeners.OnFiltersSelectListener
import ro.ebsv.githubapp.settings.models.Filter

class FilterDialogFragment: DialogFragment() {

    lateinit var filtersSelectListener: OnFiltersSelectListener

    override fun onAttach(context: Context) {
        super.onAttach(context)

        filtersSelectListener = context as OnFiltersSelectListener

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_filter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        getFilters()

        setupActions()
    }

    override fun onStart() {
        super.onStart()
        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog?.window?.setLayout(width, height)
    }

    private fun setupRecyclerView() {
        rvFilters.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rvFilters.adapter = FiltersListAdapter()
    }

    private fun getFilters() {

        val selectedFiltersString= arguments?.getString(Constants.Repository.BundleKeys.DEFAULT_FILTER)
        val selectedFilters = selectedFiltersString?.split(",")?.map { it.replace(" ", "") }

        val filtersName = resources.getStringArray(R.array.filters)

        val filters = ArrayList<Filter>()

        filters.add(Filter(filtersName[0], Constants.Repository.Filters.Affiliation.owner,
            selectedFilters!!.any { it == filtersName[0].toLowerCase() }))

        filters.add(Filter(filtersName[1], Constants.Repository.Filters.Affiliation.collaborator,
            selectedFilters.any { it == filtersName[1].toLowerCase() }))

        filters.add(Filter(filtersName[2], Constants.Repository.Filters.Affiliation.organization_member,
            selectedFilters.any { it == filtersName[2].toLowerCase().replace(" ", "_") }))


        (rvFilters.adapter as FiltersListAdapter).setFilters(filters)
    }

    private fun setupActions() {
        mbCancel.setOnClickListener {
            dismiss()
        }

        mbOK.setOnClickListener {
            filtersSelectListener.onFiltersSelected((rvFilters.adapter as FiltersListAdapter).getFilterFieldKey())
            dismiss()
        }
    }


}