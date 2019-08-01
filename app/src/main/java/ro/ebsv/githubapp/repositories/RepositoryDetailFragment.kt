package ro.ebsv.githubapp.repositories

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_repository_detail.*
import ro.ebsv.githubapp.R
import ro.ebsv.githubapp.databinding.FragmentRepositoryDetailBinding
import javax.inject.Inject

class RepositoryDetailFragment: Fragment() {

    private lateinit var binder: FragmentRepositoryDetailBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binder = DataBindingUtil.inflate(inflater, R.layout.fragment_repository_detail, container, false)
        activity?.let {
            binder.viewModel = ViewModelProviders.of(it).get(RepositoryViewModel::class.java)
        }
        return binder.root
    }

}
