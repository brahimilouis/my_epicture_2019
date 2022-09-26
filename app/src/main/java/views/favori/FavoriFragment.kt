package views.favori

import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import model.ImageData
import api.ImgurData
import com.example.myapplication.R
import api.Favori
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_favori.*
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException


/**
 * class FavoriFragment
 */

class FavoriFragment : Fragment() {
    lateinit var imageAdapter : FavoriFragment.ImagesAdapter
    private var pages : Int = 0
    private val baseUrl : String = "https://api.imgur.com/"

    /**
     * function onCreateView
     * create the favori view
     */

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_favori, container, false)
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

        favories_list.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        favories_list.layoutManager = LinearLayoutManager(activity)
        favories_list.adapter = imageAdapter
        updateImage()
    }

    /**
     * function updateImage
     * load imgur image
     */
    private fun updateImage()
    {
        val client = OkHttpClient.Builder()
            .addInterceptor(object : Interceptor {
                @Throws(IOException::class)
                override fun intercept(chain: Interceptor.Chain): Response {
                    val authed = chain.request()
                        .newBuilder()
                        .addHeader("Authorization", "Bearer " + ImgurData.get("access_token"))
                        .build()
                    return chain.proceed(authed)
                }
            }).build()

        val retrofit : Retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(client)
            .build()
        val apiImages = retrofit.create(Favori::class.java)

        apiImages.getFavories("me", pages)
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

        /**
         * onCreateViewHolder
         * load layout
         */
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
         * class ImageViewHolder
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