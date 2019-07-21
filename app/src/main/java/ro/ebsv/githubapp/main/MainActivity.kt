package ro.ebsv.githubapp.main

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import ro.ebsv.githubapp.R
import ro.ebsv.githubapp.data.Constants
import ro.ebsv.githubapp.injection.ViewModelFactory
import ro.ebsv.githubapp.managers.UserManager
import ro.ebsv.githubapp.repositories.RepositoryActivity
import ro.ebsv.githubapp.splash.SplashActivity

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this, ViewModelFactory(this)).get(MainViewModel::class.java)

        setupObservables()
        setupLayout()
        setupActions()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.logout -> {
                viewModel.clearData()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }

    private fun setupObservables() {
        viewModel.onClearData().observe(this, Observer {
            goToSplash()
        })
    }

    private fun goToSplash() {
        val intent = Intent(this, SplashActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun setupLayout() {
        Picasso.get().load(UserManager.user?.avatar_url).placeholder(R.drawable.ic_person_grey_80dp).into(ivAvatar)
        tvBio.text = UserManager.user?.bio
        tvLocation.text = UserManager.user?.location
        tvEmail.text = UserManager.user?.email
        tvCreated.text = UserManager.user?.created_at
        tvUpdated.text = UserManager.user?.updated_at
        tvPublicRepos.text = UserManager.user?.public_repos.toString()
        tvPrivateRepos.text = UserManager.user?.total_private_repos.toString()
    }

    private fun setupActions() {
        mbEmail.setOnClickListener {
            if (!UserManager.user?.email.isNullOrEmpty()) {
                val emailIntent = Intent (Intent.ACTION_SENDTO, Uri.fromParts("mailto", UserManager.user?.email, null))
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject))
                startActivity(emailIntent)
            } else {
                showMessage(R.string.missing_email)
            }
        }

        mbRepos.setOnClickListener {
            openReposActivity(Constants.Repository.Filters.Visibility.all)
        }

        tvPublicRepos.setOnClickListener {
            openReposActivity(Constants.Repository.Filters.Visibility.public)
        }

        tvPrivateRepos.setOnClickListener {
            openReposActivity(Constants.Repository.Filters.Visibility.private)
        }
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun showMessage(message: Int) {
        Toast.makeText(this, getString(message), Toast.LENGTH_LONG).show()
    }

    private fun openReposActivity(visibility: Constants.Repository.Filters.Visibility) {
        val reposIntent = Intent(this, RepositoryActivity::class.java)
        reposIntent.putExtra(Constants.Repository.BundleKeys.REPO_VISIBILITY, visibility)
        startActivity(reposIntent)
    }
}
