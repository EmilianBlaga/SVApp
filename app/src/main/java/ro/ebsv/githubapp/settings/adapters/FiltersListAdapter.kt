package ro.ebsv.githubapp.settings.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.filter_item.view.*
import ro.ebsv.githubapp.R
import ro.ebsv.githubapp.settings.models.Filter

class FiltersListAdapter: RecyclerView.Adapter<FiltersListAdapter.FilterViewHolder>() {

    private val filters = ArrayList<Filter>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.filter_item, parent, false)
        return FilterViewHolder(view)
    }

    override fun getItemCount(): Int {
        return filters.size
    }

    override fun onBindViewHolder(holder: FilterViewHolder, position: Int) {
        holder.bindTo(filters[position])

        holder.itemView.setOnClickListener {
            filters[position].selected = !filters[position].selected
        }
    }

    fun setFilters(filters: ArrayList<Filter>) {
        this.filters.clear()
        this.filters.addAll(filters)
        notifyDataSetChanged()
    }

    fun getFilterFieldKey(): String {
        return filters.filter { it.selected }.joinToString { it.key.name }
    }

    inner class FilterViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private val mcbFilterItem = view.mcbFilterItem

        fun bindTo(filter: Filter) {
            mcbFilterItem.text = filter.name
            mcbFilterItem.isChecked = filter.selected
        }

    }
}