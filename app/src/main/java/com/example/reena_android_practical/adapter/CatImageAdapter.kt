import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.reena_android_practical.model.CatImageModel
import com.example.reena_android_practical.databinding.ItemCatImageBinding


class CatImageAdapter(private val catImages: ArrayList<CatImageModel>) :
    RecyclerView.Adapter<CatImageAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemCatImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val catImage = catImages[position]
        Glide.with(holder.itemView.context)
            .load(catImage.url)
            .into(holder.binding.catImageView)

        holder.binding.txtImageName.text = getImageNameFromUrl(catImage.url)
    }

    fun getImageNameFromUrl(url: String): String? {
        val parts = url.split("/")
        if (parts.isNotEmpty()) {
            val fileName = parts.last()
            return fileName
        }
        return null
    }


    override fun getItemCount(): Int {
        return catImages.size
    }

    class ViewHolder(val binding: ItemCatImageBinding) : RecyclerView.ViewHolder(binding.root)
}
