package ro.ebsv.githubapp.settings.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import ro.ebsv.githubapp.R
import ro.ebsv.githubapp.databinding.FilterItemBinding
import ro.ebsv.githubapp.settings.models.Filter

class FiltersListAdapter: RecyclerView.Adapter<FiltersListAdapter.FilterViewHolder>() {

    private val filters = ArrayList<Filter>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterViewHolder {
        val binder = DataBindingUtil.inflate<FilterItemBinding>(LayoutInflater.from(parent.context),
            R.layout.filter_item, parent, false)
        return FilterViewHolder(binder)
    }

    override fun getItemCount(): Int {
        return filters.size
    }

    override fun onBindViewHolder(holder: FilterViewHolder, position: Int) {
        holder.bindTo(filters[position])
    }

    fun setFilters(filters: ArrayList<Filter>) {
        this.filters.clear()
        this.filters.addAll(filters)
        notifyDataSetChanged()
    }

    fun getFilterFieldKey(): String {
        return filters.filter { it.selected }.joinToString { it.key.name }
    }

    inner class FilterViewHolder(private val binder: FilterItemBinding): RecyclerView.ViewHolder(binder.root) {

        fun bindTo(filter: Filter) {
            binder.filter = filter
            binder.executePendingBindings()
        }

    }
}