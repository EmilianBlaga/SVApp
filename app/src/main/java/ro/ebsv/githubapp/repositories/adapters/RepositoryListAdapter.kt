package ro.ebsv.githubapp.repositories.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.repository_list_item.view.*
import ro.ebsv.githubapp.R
import ro.ebsv.githubapp.repositories.listeners.OnRepositoryClickListener
import ro.ebsv.githubapp.repositories.models.Repository

class RepositoryListAdapter(private val repositoryClickListener: OnRepositoryClickListener):
    RecyclerView.Adapter<RepositoryListAdapter.RepositoryViewHolder>() {

    private val repositories = ArrayList<Repository>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.repository_list_item, parent, false)
        return RepositoryViewHolder(view)
    }

    override fun getItemCount(): Int = repositories.size

    override fun onBindViewHolder(holder: RepositoryViewHolder, position: Int) {
        holder.bindTo(repositories[position])
    }

    fun setRepositories(repos: ArrayList<Repository>) {
        repositories.clear()
        repositories.addAll(repos)
        notifyDataSetChanged()
    }

    inner class RepositoryViewHolder(view: View): RecyclerView.ViewHolder(view) {

        private val tvRepoName = view.tvRepoName

        fun bindTo(repository: Repository) {
            tvRepoName.text = repository.name

            itemView.setOnClickListener {
                repositoryClickListener.onRepositoryClicked(repository)
            }
        }

    }

}