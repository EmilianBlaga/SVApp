package ro.ebsv.githubapp.settings

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_settings.*
import ro.ebsv.githubapp.R
import ro.ebsv.githubapp.data.Constants
import ro.ebsv.githubapp.settings.adapters.SettingsListAdapter
import ro.ebsv.githubapp.settings.listeners.OnFiltersSelectListener
import ro.ebsv.githubapp.settings.listeners.OnSettingClickListener
import ro.ebsv.githubapp.settings.models.Setting

class SettingsActivity : AppCompatActivity(), OnFiltersSelectListener {

    private var affiliationFilters = enumValues<Constants.Repository.Filters.Affiliation>().joinToString {it.name}
    private var sortCriteria = Constants.Repository.Sort.Criteria.full_name

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupData()

        setupRecyclerView()

        createSettingsArray()
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            android.R.id.home -> {
                finishActivity()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    private fun setupData() {
        affiliationFilters = intent?.getStringExtra(Constants.Repository.BundleKeys.DEFAULT_FILTER)!!
        sortCriteria = intent?.getSerializableExtra(Constants.Repository.BundleKeys.DEFAULT_SORT)!!
                as Constants.Repository.Sort.Criteria
    }

    private fun setupRecyclerView() {
        rvSettings.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rvSettings.adapter = SettingsListAdapter(object: OnSettingClickListener{
            override fun onSettingClicked(position: Int) {
                when (position) {
                    0 -> {
                        val filterFragment = FilterDialogFragment()

                        val args = Bundle()
                        args.putString(Constants.Repository.BundleKeys.DEFAULT_FILTER, affiliationFilters)
                        filterFragment.arguments = args

                        filterFragment.show(supportFragmentManager, "FilterDialogFragment")
                    }
                    1 -> {
                        val sortFragment = SortDialogFragment()

                        val args = Bundle()
                        args.putSerializable(Constants.Repository.BundleKeys.DEFAULT_SORT, sortCriteria)
                        sortFragment.arguments = args

                        sortFragment.show(supportFragmentManager, "SortDialogFragment")
                    }
                }
            }
        })
    }

    private fun createSettingsArray() {
        val affiliationSetting = Setting(0, getString(R.string.affiliation), getString(R.string.affiliation_desc))
        val sortSetting = Setting(1, getString(R.string.sort), getString(R.string.sort_desc))

        val settings = arrayListOf(affiliationSetting, sortSetting)

        (rvSettings.adapter as SettingsListAdapter).setSettings(settings)
    }

    override fun onFiltersSelected(filters: String) {
        if (filters.isEmpty())
            affiliationFilters = enumValues<Constants.Repository.Filters.Affiliation>().joinToString {it.name}
        else
            affiliationFilters = filters
    }

    override fun onSortCriteriaSelected(criteria: Constants.Repository.Sort.Criteria) {
        sortCriteria = criteria
    }

    private fun finishActivity() {
        val data = Intent()
        data.putExtra(Constants.Repository.BundleKeys.DEFAULT_FILTER, affiliationFilters)
        data.putExtra(Constants.Repository.BundleKeys.DEFAULT_SORT, sortCriteria)
        setResult(Activity.RESULT_OK, data)
        finish()
    }

}
