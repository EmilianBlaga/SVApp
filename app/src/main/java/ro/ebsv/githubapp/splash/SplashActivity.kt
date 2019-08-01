package ro.ebsv.githubapp.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import ro.ebsv.githubapp.R
import ro.ebsv.githubapp.injection.ViewModelFactory
import ro.ebsv.githubapp.login.mvvm.LoginActivity
import ro.ebsv.githubapp.login.models.User
import ro.ebsv.githubapp.main.MainActivity
import ro.ebsv.githubapp.managers.UserManager

class SplashActivity : AppCompatActivity() {

    lateinit var viewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        viewModel = ViewModelProviders.of(this, ViewModelFactory(this)).get(SplashViewModel::class.java)


        setupObservables()

        viewModel.checkUser()
    }

    private fun setupObservables() {
        viewModel.userObservable().observe(this, Observer {user ->
            when (user) {
                is User.Success -> {
                    UserManager.user = user.toUserEntity()
                    goToMain()
                }
                is User.Error -> {
                    goToLogin()
                }
            }
        })
    }


    private fun goToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun goToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }


}
