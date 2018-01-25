package com.example.user.firstapplication

/**
 * Created by User on 2018/1/25.
 */
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_login.view.*

/**
 * Created by fsmytsai on 2018/1/24.
 */
class LoginFragment : Fragment() {
    private lateinit var mMainActivity: MainActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        mMainActivity = activity as MainActivity
        initViews(view)
        return view
    }

    private fun initViews(view: View) {
        view.bt_login.setOnClickListener {
            if (et_account.text.toString() == "op" && et_password.text.toString() == "123") {
                mMainActivity. sharedPreferences.edit().putBoolean("IsLogin",true).apply()
                mMainActivity.supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.fl_main_container, HomeFragment(), "HomeFragment")
                        .commit()
            } else {
                Toast.makeText(mMainActivity, "帳號或密碼錯誤", Toast.LENGTH_SHORT).show()
            }
        }


    }
}

