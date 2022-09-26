package api

import android.content.Context
import android.content.SharedPreferences

/**
 * Object ImgurData
 * conteners of imgur data
 */

object ImgurData {

    private var data: SharedPreferences? = null

    /**
     * isConnected
     * check if we are connect to imgur
     */

    val isConnected: Boolean
        get() = get("access_token") != null && data!!.getLong("finish_in", -1) > System.currentTimeMillis()

    /**
     * function initImgurData
     * init sharedPreferences
     */

    fun initImgurData(context: Context) {
        data = context.getSharedPreferences("oauth", Context.MODE_PRIVATE)
    }

    /**
     * function get
     * get an element with its key
     */

    operator fun get(key: String): String? {
        return data?.getString(key, null)
    }

    /**
     * function set
     * set an data of type String
     */

    operator fun set(key: String, value: String) {
        data!!.edit().putString(key, value).commit()
    }

    /**
     * function set
     * set an data of type Long?
     */
    operator fun set(key: String, value: Long?) {
        data!!.edit().putLong(key, value!!).commit()
    }
}