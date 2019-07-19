package ro.ebsv.githubapp.repositories

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_repository_detail.*
import ro.ebsv.githubapp.R
import ro.ebsv.githubapp.data.Constants
import ro.ebsv.githubapp.repositories.models.Repository

class RepositoryDetailFragment: Fragment() {

    private lateinit var repository: Repository

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_repository_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {bundle ->
            if (bundle.containsKey(Constants.Repository.BundleKeys.REPOSITORY)) {
                repository = bundle.getSerializable(Constants.Repository.BundleKeys.REPOSITORY) as Repository
                tvRepoDetails.text = repository.full_name
            }
        }
    }
}
