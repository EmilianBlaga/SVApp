package ro.ebsv.githubapp.settings

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.fragment_sort.*
import ro.ebsv.githubapp.R
import ro.ebsv.githubapp.data.Constants
import ro.ebsv.githubapp.settings.listeners.OnFiltersSelectListener

class SortDialogFragment: DialogFragment() {

    lateinit var filtersSelectListener: OnFiltersSelectListener

    override fun onAttach(context: Context) {
        super.onAttach(context)

        filtersSelectListener = context as OnFiltersSelectListener

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sort, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupLayout()
        setupActions()
    }

    override fun onStart() {
        super.onStart()
        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog?.window?.setLayout(width, height)
    }

    private fun setupLayout() {

        when (arguments?.getSerializable(Constants.Repository.BundleKeys.DEFAULT_SORT)
                as Constants.Repository.Sort.Criteria) {

            Constants.Repository.Sort.Criteria.full_name -> rgSort.check(R.id.mrbName)
            Constants.Repository.Sort.Criteria.created -> rgSort.check(R.id.mrbCreated)
            Constants.Repository.Sort.Criteria.pushed -> rgSort.check(R.id.mrbPushed)
            Constants.Repository.Sort.Criteria.updated -> rgSort.check(R.id.mrbUpdated)

        }
    }


    private fun setupActions() {
        mbCancel.setOnClickListener {
            dismiss()
        }

        rgSort.setOnCheckedChangeListener { _, i ->
            when (i) {
                R.id.mrbCreated -> {
                    filtersSelectListener.onSortCriteriaSelected(Constants.Repository.Sort.Criteria.created)
                }
                R.id.mrbPushed -> {
                    filtersSelectListener.onSortCriteriaSelected(Constants.Repository.Sort.Criteria.pushed)
                }
                R.id.mrbUpdated -> {
                    filtersSelectListener.onSortCriteriaSelected(Constants.Repository.Sort.Criteria.updated)
                }
                R.id.mrbName -> {
                    filtersSelectListener.onSortCriteriaSelected(Constants.Repository.Sort.Criteria.full_name)
                }
            }
            dismiss()
        }
    }


}