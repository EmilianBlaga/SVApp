package ro.ebsv.githubapp.login.mvvm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import ro.ebsv.githubapp.R
import ro.ebsv.githubapp.databinding.ActivityLoginBinding
import ro.ebsv.githubapp.injection.ViewModelFactory
import ro.ebsv.githubapp.login.models.User
import ro.ebsv.githubapp.main.MainActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binder: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        binder = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binder.lifecycleOwner = this
        binder.viewModel = ViewModelProviders.of(this, ViewModelFactory(this)).get(LoginViewModel::class.java)

        setupObservers()

    }

    private fun setupObservers() {
        binder.viewModel?.userObservable()?.observe(this, Observer { user ->
            when (user) {
                is User.Success -> {
                    goToMain()
                }
                is User.Error -> {
                    Toast.makeText(this, user.message, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun goToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
