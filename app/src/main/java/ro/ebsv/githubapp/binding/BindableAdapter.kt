package ro.ebsv.githubapp.binding

interface BindableAdapter<T> {
    fun setData(data: List<T>?)
}