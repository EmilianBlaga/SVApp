package ro.ebsv.githubapp.main

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import ro.ebsv.githubapp.R
import ro.ebsv.githubapp.data.Constants
import ro.ebsv.githubapp.injection.ViewModelFactory
import ro.ebsv.githubapp.repositories.RepositoryActivity
import ro.ebsv.githubapp.splash.SplashActivity
import ro.ebsv.githubapp.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binder: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binder = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binder.viewModel = ViewModelProviders.of(this, ViewModelFactory(this)).get(MainViewModel::class.java)
        binder.lifecycleOwner = this

        setupObservers()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.logout -> {
                binder.viewModel?.clearData()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }

    private fun setupObservers() {
        binder.viewModel?.onClearData()?.observe(this, Observer {
            goToSplash()
        })

        binder.viewModel?.getOpenReposLiveData()?.observe( this, Observer { visibility ->
            openReposActivity(visibility)
        })

        binder.viewModel?.getEmailLiveData()?.observe( this, Observer { email ->
            openEmailClient(email)
        })
    }

    private fun openEmailClient(email: String?) {
        if (!email.isNullOrEmpty()) {
            val emailIntent = Intent (Intent.ACTION_SENDTO, Uri.fromParts("mailto", email, null))
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject))
            startActivity(emailIntent)
        } else {
            showMessage(R.string.missing_email)
        }
    }

    private fun goToSplash() {
        val intent = Intent(this, SplashActivity::class.java)
        startActivity(intent)
        finish()
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
