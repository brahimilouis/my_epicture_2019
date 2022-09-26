package model


/**
 * class ImgData
 * contain link
 */

class ImgData {
    var link : String?= null
}

/**
 * class ImageData
 * contain data of one image
 */
class ImageData {

    var id : String? = null
    var title : String? = null
    var link : String? = null
    var width: Int = 0
    var height: Int = 0
    val images: ArrayList<ImgData>? = null
    var name: String? = null
}

/**
 * ImageRegister
 * contain a list of images
 */

class ImageRegister {
    lateinit var data : List<ImageData>
}