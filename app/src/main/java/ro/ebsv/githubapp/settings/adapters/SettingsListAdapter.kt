package ro.ebsv.githubapp.settings.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.setting_item.view.*
import ro.ebsv.githubapp.R
import ro.ebsv.githubapp.settings.listeners.OnSettingClickListener
import ro.ebsv.githubapp.settings.models.Setting

class SettingsListAdapter(private val listener: OnSettingClickListener):
    RecyclerView.Adapter<SettingsListAdapter.SettingViewHolder>() {

    private val settings = ArrayList<Setting>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SettingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.setting_item, parent, false)
        return SettingViewHolder(view)
    }

    override fun getItemCount(): Int = settings.size

    override fun onBindViewHolder(holder: SettingViewHolder, position: Int) {
        holder.bindTo(settings[position])
        holder.itemView.setOnClickListener {
            listener.onSettingClicked(position)
        }
    }

    fun setSettings(settings: ArrayList<Setting>) {
        this.settings.clear()
        this.settings.addAll(settings)
        notifyDataSetChanged()
    }

    inner class SettingViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private val tvSettingName = view.tvSettingName
        private val tvSettingDesc = view.tvSettingDesc

        fun bindTo(setting: Setting) {
            tvSettingName.text = setting.name
            tvSettingDesc.text = setting.description
        }
    }
}