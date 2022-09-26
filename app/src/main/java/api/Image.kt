package api

import io.reactivex.Observable
import model.ImageRegister
import retrofit2.http.*

/**
 * Image interface
 * get image galery
 */
interface Image {
    @Headers("Authorization: Client-ID e1e6ece1033cb58")
    @GET("3/gallery/hot/viral/{pages}.json")
    fun getImages(@Path("pages") page: Int) : Observable<ImageRegister>
}

/**
 * Favori interface
 * get my favory
 */

interface Favori {
    @GET("3/account/{id}/gallery_favorites/{pages}")

    fun getFavories(@Path("id") id: String, @Path("pages") pages: Int): Observable<ImageRegister>
}
