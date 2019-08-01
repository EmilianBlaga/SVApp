package ro.ebsv.githubapp.settings.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import ro.ebsv.githubapp.R
import ro.ebsv.githubapp.databinding.SettingItemBinding
import ro.ebsv.githubapp.settings.listeners.OnSettingClickListener
import ro.ebsv.githubapp.settings.models.Setting

class SettingsListAdapter(private val listener: OnSettingClickListener):
    RecyclerView.Adapter<SettingsListAdapter.SettingViewHolder>() {

    private val settings = ArrayList<Setting>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SettingViewHolder {
        val binder = DataBindingUtil.inflate<SettingItemBinding>(LayoutInflater.from(parent.context),
            R.layout.setting_item, parent, false)
        return SettingViewHolder(binder)
    }

    override fun getItemCount(): Int = settings.size

    override fun onBindViewHolder(holder: SettingViewHolder, position: Int) {
        holder.bindTo(settings[position])
    }

    fun setSettings(settings: ArrayList<Setting>) {
        this.settings.clear()
        this.settings.addAll(settings)
        notifyDataSetChanged()
    }

    inner class SettingViewHolder(private val binder: SettingItemBinding): RecyclerView.ViewHolder(binder.root) {

        fun bindTo(setting: Setting) {
            binder.setting = setting
            binder.clickListener = listener
            binder.executePendingBindings()
        }

    }
}