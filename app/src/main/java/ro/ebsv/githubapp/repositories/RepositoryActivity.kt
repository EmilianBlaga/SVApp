package ro.ebsv.githubapp.repositories

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_repository.*
import ro.ebsv.githubapp.R
import ro.ebsv.githubapp.data.Constants
import ro.ebsv.githubapp.injection.ViewModelFactory
import ro.ebsv.githubapp.repositories.listeners.OnRepositorySelectListener
import ro.ebsv.githubapp.repositories.models.Repository
import ro.ebsv.githubapp.settings.SettingsActivity
import javax.inject.Inject

class RepositoryActivity : AppCompatActivity(), OnRepositorySelectListener {

    @Inject
    lateinit var viewModel: RepositoryViewModel

    private var twoPane = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repository)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (flRepositoryDetails != null) {
            twoPane = true
        }

        viewModel = ViewModelProviders.of(this, ViewModelFactory(this)).get(RepositoryViewModel::class.java)

        viewModel.getRepositories(intent.getSerializableExtra(Constants.Repository.BundleKeys.REPO_VISIBILITY)
                as Constants.Repository.Filters.Visibility)

        setupObservables()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.repos_menu, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.settings -> {
                val settingsIntent = Intent(this, SettingsActivity::class.java)
                val currentReposData = viewModel.getCurrentReposData()
                settingsIntent.putExtra(Constants.Repository.BundleKeys.DEFAULT_FILTER, currentReposData.first)
                settingsIntent.putExtra(Constants.Repository.BundleKeys.DEFAULT_SORT, currentReposData.second)
                startActivityForResult(settingsIntent, Constants.Repository.REQUEST_CODE)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    private fun setupObservables() {
        viewModel.repos().observe(this, Observer { repos ->
            setupLayout(repos)
        })
    }

    private fun setupLayout(repositories: List<Repository>) {

        val listFragment = RepositoryListFragment().apply {
            arguments = Bundle().apply {
                putSerializable(
                    Constants.Repository.BundleKeys.REPO_LIST,
                    arrayListOf(repositories)
                )
            }
        }

        if (twoPane) {

            supportFragmentManager.beginTransaction()
                .replace(R.id.flRepositoryList, listFragment)
                .commit()

        } else {

            supportFragmentManager.beginTransaction()
                .replace(R.id.flRepository, listFragment)
                .commit()

        }

    }

    override fun onRepositorySelected (repository: Repository) {

        val fragment = RepositoryDetailFragment().apply {
            arguments = Bundle().apply {
                putSerializable(
                    Constants.Repository.BundleKeys.REPOSITORY,
                    repository
                )
            }
        }

        if (twoPane) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.flRepositoryDetails, fragment)
                .commit()

        } else {
            supportFragmentManager.beginTransaction()
                .add(R.id.flRepository, fragment)
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            Constants.Repository.REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    viewModel.getRepositories(
                        (intent.getSerializableExtra(Constants.Repository.BundleKeys.REPO_VISIBILITY)
                                as Constants.Repository.Filters.Visibility),

                        data?.getStringExtra(Constants.Repository.BundleKeys.DEFAULT_FILTER)!!,

                        data.getSerializableExtra(Constants.Repository.BundleKeys.DEFAULT_SORT)
                                as Constants.Repository.Sort.Criteria)
                }
            }
        }
    }

}
