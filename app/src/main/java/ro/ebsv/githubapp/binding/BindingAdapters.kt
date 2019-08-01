package ro.ebsv.githubapp.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputLayout
import com.squareup.picasso.Picasso
import ro.ebsv.githubapp.R

class BindingAdapters {

    class TextInputLayoutBinder {
        companion object {
            @JvmStatic
            @BindingAdapter("app:errorText")
            fun setErrorText(layout: TextInputLayout, text: String?) {
                layout.error = text
            }

            @JvmStatic
            @BindingAdapter("imageUrl")
            fun setImageFromUrl(view: ImageView, url: String) {
                Picasso.get().load(url).placeholder(R.drawable.ic_person_grey_80dp).into(view)
            }
        }
    }

    class RecyclerViewBinder {
        companion object {
            @JvmStatic
            @BindingAdapter("data")
            fun <T> setRecyclerViewData(recyclerView: RecyclerView, data: List<T>?) {
                if (recyclerView.adapter is BindableAdapter<*>) {
                    (recyclerView.adapter as? BindableAdapter<T>)?.setData(data)
                }
            }
        }
    }

}