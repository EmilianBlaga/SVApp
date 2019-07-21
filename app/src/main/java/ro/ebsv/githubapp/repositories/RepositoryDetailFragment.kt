package ro.ebsv.githubapp.repositories

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_repository_detail.*
import ro.ebsv.githubapp.R
import ro.ebsv.githubapp.data.Constants
import ro.ebsv.githubapp.injection.ViewModelFactory
import ro.ebsv.githubapp.repositories.models.Repository
import javax.inject.Inject

class RepositoryDetailFragment: Fragment() {

    @Inject
    lateinit var viewModel: RepositoryViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        activity?.let {
            viewModel = ViewModelProviders.of(it).get(RepositoryViewModel::class.java)
        }
        return inflater.inflate(R.layout.fragment_repository_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvRepoDetails.text = viewModel.repositoryLiveData().value?.full_name
    }
}
