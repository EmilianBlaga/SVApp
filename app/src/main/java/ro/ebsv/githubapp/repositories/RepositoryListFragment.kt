package ro.ebsv.githubapp.repositories

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_repository_list.*
import ro.ebsv.githubapp.R
import ro.ebsv.githubapp.repositories.adapters.RepositoryListAdapter
import ro.ebsv.githubapp.repositories.listeners.OnRepositoryClickListener
import ro.ebsv.githubapp.room.entities.RepositoryEntity
import javax.inject.Inject

class RepositoryListFragment: Fragment(), OnRepositoryClickListener {

    @Inject
    lateinit var viewModel: RepositoryViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        activity?.let {
            viewModel = ViewModelProviders.of(it).get(RepositoryViewModel::class.java)
        }
        return inflater.inflate(R.layout.fragment_repository_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        setupObservables()

        viewModel.getRepositories()
    }

    private fun setupRecyclerView() {
        rvRepositories.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rvRepositories.adapter = RepositoryListAdapter(this)
    }

    private fun setupObservables() {
        viewModel.repositoriesLiveData().observe(this, Observer { repositories ->
            (rvRepositories.adapter as RepositoryListAdapter).setRepositories(repositories as ArrayList<RepositoryEntity>)
        })
    }

    override fun onRepositoryClicked(repository: RepositoryEntity) {
        viewModel.setRepository(repository)
    }
}
