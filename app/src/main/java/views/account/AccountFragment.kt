package views.account

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import api.ImgurData
import activity.LoginActivity
import android.widget.TextView
import com.example.myapplication.R


/**
 * class AccountFragment
 * display the account fragment
 */

class AccountFragment : Fragment() {

    /**
     * onCreateView
     * create view of account
     */

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        ImgurData.initImgurData(requireContext())
        val root = inflater.inflate(R.layout.fragment_account, container, false)
        val Bdeco : Button = root.findViewById(R.id.DiscoButton)
        val textDisplay : TextView = root.findViewById(R.id.textView2)

        textDisplay.text = "Hello " + ImgurData.get("account_username") + " !"
        Bdeco.setOnClickListener { deconnection() }
        return root
    }

    /**
     * function dennection
     * deconnect user
     */

    private fun deconnection()
    {
        ImgurData.set("finish_in", System.currentTimeMillis())
        val intent = Intent(getActivity(), LoginActivity::class.java)
        startActivity(intent)
    }
}