package ro.ebsv.githubapp.repositories

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ro.ebsv.githubapp.R
import ro.ebsv.githubapp.databinding.FragmentRepositoryListBinding
import ro.ebsv.githubapp.repositories.adapters.RepositoryListAdapter
import ro.ebsv.githubapp.repositories.listeners.OnRepositoryClickListener
import ro.ebsv.githubapp.room.entities.RepositoryEntity

class RepositoryListFragment: Fragment(), OnRepositoryClickListener {

    private lateinit var binder: FragmentRepositoryListBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binder = DataBindingUtil.inflate(inflater, R.layout.fragment_repository_list, container, false)
        activity?.let {
            binder.viewModel = ViewModelProviders.of(it).get(RepositoryViewModel::class.java)
        }

        return binder.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        binder.viewModel?.getRepositories()

        binder.viewModel?.repositoriesLiveData()?.observe(this, Observer {
            binder.repositoryAdapter?.setData(it)
        })

    }

    private fun setupRecyclerView() {
        binder.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binder.repositoryAdapter = RepositoryListAdapter(this)
    }

    override fun onRepositoryClicked(repository: RepositoryEntity) {
        binder.viewModel?.setRepository(repository)
    }
}
