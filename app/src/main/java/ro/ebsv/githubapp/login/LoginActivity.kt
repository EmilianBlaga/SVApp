package ro.ebsv.githubapp.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_login.*
import ro.ebsv.githubapp.R
import ro.ebsv.githubapp.injection.ViewModelFactory
import ro.ebsv.githubapp.login.models.User
import ro.ebsv.githubapp.main.MainActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        viewModel = ViewModelProviders.of(this, ViewModelFactory(this)).get(LoginViewModel::class.java)

        setupActions()
        setupObservables()
    }

    private fun setupActions() {
        mbLogin.setOnClickListener {
            when {
                TextUtils.isEmpty(tietUsername.text) -> tilUsername.error = "Username must not be empty."
                TextUtils.isEmpty(tietPassword.text) -> tilPassword.error = "Password must not be empty."
                else -> viewModel.authenticateUser(tietUsername.text.toString(), tietPassword.text.toString())
            }
        }
    }

    private fun setupObservables() {
        viewModel.userObservable().observe(this, Observer { user ->
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
