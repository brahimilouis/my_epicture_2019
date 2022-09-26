package activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.net.Uri
import com.example.myapplication.R
import api.ImgurData

/**
 * class loginActivity
 * activity to login to ingur
 */

class LoginActivity : AppCompatActivity() {
    lateinit var GoBack : Button
    private val clientId: String? = "e1e6ece1033cb58"
    private val responseType: String? = "token"
    private val redirectUrl: String? = "http://callback.local"
    private val baseUrl = "https://api.imgur.com/oauth2/authorize"

    /**
     * onCreate function
     * create intent to login imgur
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        ImgurData.initImgurData(this)

        GoBack = findViewById(R.id.ConnectButton)


        GoBack.setOnClickListener {
            val toSend = baseUrl + "?client_id=" + clientId + "&response_type=" + responseType
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(toSend))
            startActivity(intent)
        }
    }

    /**
     * override fun onResume
     * the responds of imgur to login
     */

    override fun onResume() {
        super.onResume()

        var uri = intent.data

        if (uri != null && uri.toString().startsWith(redirectUrl!!)) {
            if (uri.getQueryParameter("error") == null) {
                val newUri = Uri.parse(uri.toString().replace('#', '?'))
                val accessToken = newUri.getQueryParameter("access_token")
                val refreshToken = newUri.getQueryParameter("refresh_token")
                val accountUsername = newUri.getQueryParameter("account_username")
                var finish_in = (newUri.getQueryParameter("expires_in")?.toLong() ?: 0) * 1000
                ImgurData.set("finish_in", System.currentTimeMillis() + finish_in)
                if (accessToken != null && refreshToken != null && accountUsername != null)
                {
                    ImgurData.set("access_token", accessToken)
                    ImgurData.set("refresh_token", refreshToken)
                    ImgurData.set("account_username", accountUsername)
                }
                val IntentValide = Intent(this, MainActivity::class.java)
                startActivity(IntentValide)
                finish()

            }
        }
    }
}
