package ro.ebsv.githubapp.repositories.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import ro.ebsv.githubapp.R
import ro.ebsv.githubapp.binding.BindableAdapter
import ro.ebsv.githubapp.databinding.RepositoryListItemBinding
import ro.ebsv.githubapp.repositories.listeners.OnRepositoryClickListener
import ro.ebsv.githubapp.room.entities.RepositoryEntity

class RepositoryListAdapter(private val repositoryClickListener: OnRepositoryClickListener):
    RecyclerView.Adapter<RepositoryListAdapter.RepositoryViewHolder>(), BindableAdapter<RepositoryEntity> {

    private val repositories = ArrayList<RepositoryEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryViewHolder {
        val binder = DataBindingUtil.inflate<RepositoryListItemBinding>(LayoutInflater.from(parent.context),
            R.layout.repository_list_item, parent, false)
        return RepositoryViewHolder(binder)
    }

    override fun getItemCount(): Int = repositories.size

    override fun onBindViewHolder(holder: RepositoryViewHolder, position: Int) {
        holder.bindTo(repositories[position])
    }

    override fun setData(data: List<RepositoryEntity>?) {
        data?.let {
            repositories.clear()
            repositories.addAll(it)
            notifyDataSetChanged()
        }
    }

    inner class RepositoryViewHolder(private val binder: RepositoryListItemBinding)
        : RecyclerView.ViewHolder(binder.root) {

        fun bindTo(repository: RepositoryEntity) {
            binder.repository = repository
            binder.clickListener = repositoryClickListener
            binder.executePendingBindings()
        }

    }

}