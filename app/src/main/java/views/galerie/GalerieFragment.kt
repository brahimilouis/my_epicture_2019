package views.galerie

import android.os.Bundle
import android.view.*

import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import model.ImageData
import com.example.myapplication.R
import api.Image
import api.ImgurData

import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit

import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import io.reactivex.android.schedulers.AndroidSchedulers
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.fragment_galerie.*


/**
 * class GalerieFragment
 */

class GalerieFragment : Fragment() {
    private var pages: Int = 0
    lateinit var imageAdapter : ImagesAdapter
    private val baseUrl : String = "https://api.imgur.com/"

    /**
     * function onCreateView
     * create the galerie view
     */
    override fun onCreateView (
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_galerie, container, false)
        return root
    }

    /**
     * function onViewCreated
     * after the createView
     */


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ImgurData.initImgurData(requireContext())
        imageAdapter = ImagesAdapter()
        println(galerie_list)

        galerie_list.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        galerie_list.layoutManager = LinearLayoutManager(activity)
        galerie_list.adapter = imageAdapter

        updateImage()
    }

    /**
     * function updateImage
     * load imgur favories image
     */
    private fun updateImage()
    {
        val retrofit : Retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
        val apiImages = retrofit.create(Image::class.java)

        apiImages.getImages(pages)
        .subscribeOn(Schedulers.io())
        .unsubscribeOn(Schedulers.computation())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({ imageAdapter.setImages(it.data) },
        {
            Toast.makeText(activity, it.message, Toast.LENGTH_SHORT).show()
        })
    }

    /**
     * class ImageAdapter
     * display images in a recyclerView
     */
    inner class ImagesAdapter : RecyclerView.Adapter<ImagesAdapter.ImageViewHolder>() {
        private val images: MutableList<ImageData> = mutableListOf()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
            return ImageViewHolder(
                layoutInflater.inflate(
                    R.layout.image_layout,
                    parent,
                    false
                )
            )
        }

        /**
         * getItemCount
         * get the image load number
         */
        override fun getItemCount(): Int {
            return images.size
        }

        /**
         * onBindViewHolder
         */
        override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
            if (position == itemCount - 1) {
                pages += 1
                updateImage()
            }
            holder.bindModel(images[position])
        }

        /**
         * function setImage
         * set image in data
         */
        fun setImages(data: List<ImageData>) {
            images.addAll(data)
            notifyDataSetChanged()
        }

        /**
         * classImageViewHolder
         * load image in layout
         */
        inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val imageTitle: TextView = itemView.findViewById(R.id.imageTitle)
            fun bindModel(movie: ImageData) {
                imageTitle.text = movie.title
                if (movie.images?.get(0) != null) {
                        Glide
                            .with(requireContext())
                            .load(movie.images.get(0).link)
                            .apply(RequestOptions().override(300, 300))
                            .into(itemView.findViewById(R.id.imageAvatar))
                }
            }
        }
    }
}