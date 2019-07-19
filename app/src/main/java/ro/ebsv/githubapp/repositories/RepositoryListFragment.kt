package ro.ebsv.githubapp.repositories

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_repository_list.*
import ro.ebsv.githubapp.R
import ro.ebsv.githubapp.data.Constants
import ro.ebsv.githubapp.repositories.adapters.RepositoryListAdapter
import ro.ebsv.githubapp.repositories.listeners.OnRepositoryClickListener
import ro.ebsv.githubapp.repositories.listeners.OnRepositorySelectListener
import ro.ebsv.githubapp.repositories.models.Repository

class RepositoryListFragment: Fragment(), OnRepositoryClickListener {

    private lateinit var repositorySelectListener: OnRepositorySelectListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        repositorySelectListener = context as OnRepositorySelectListener
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_repository_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        showRepos()
    }

    private fun setupRecyclerView() {
        rvRepositories.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rvRepositories.adapter = RepositoryListAdapter(this)
    }

    private fun showRepos() {
        arguments?.let {bundle ->
            if (bundle.containsKey(Constants.Repository.BundleKeys.REPO_LIST)) {
                val repositories = bundle.getSerializable(Constants.Repository.BundleKeys.REPO_LIST) as ArrayList<*>

                (rvRepositories.adapter as RepositoryListAdapter).setRepositories(repositories as ArrayList<Repository>)
            }
        }
    }

    override fun onRepositoryClicked(repository: Repository) {
        repositorySelectListener.onRepositorySelected(repository)
    }
}
