package com.example.user.firstapplication


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import okhttp3.*
import java.io.IOException
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.example.user.firstapplication.model.Rank
import com.google.gson.Gson
import kotlinx.android.synthetic.main.block_rank.view.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.top_section.view.*

/**
 * Created by User on 2018/1/25.
 */
class HomeFragment : Fragment() {
    private lateinit var rv_home_rank: RecyclerView
    private lateinit var mMainActivity: MainActivity
    var rankData: Rank? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        mMainActivity = activity as MainActivity
        initViews(view)
        return view
    }

    fun initViews(view: View) {
        view.tv_toolBar.text = "首頁"
        rv_home_rank = view.rv_home_rank

        view.srl_home_rank.setOnRefreshListener {
            refresh()
        }
        view.srl_home_rank.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(mMainActivity, android.R.color.transparent))

        view.srl_home_rank.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light)

        view.srl_home_rank.setDistanceToTriggerSync(400)

        view.srl_home_rank.setSize(SwipeRefreshLayout.DEFAULT)
    }

    fun refresh() {
        getRank()
    }

    fun getRank() {
        val request = Request.Builder()
                .url("http://beauty.southeastasia.cloudapp.azure.com/api/getRank")
                .build()

        OkHttpClient().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call?, e: IOException?) {

            }

            override fun onResponse(call: Call?, response: Response?) {
                val StatusCode = response?.code()
                val ResMessage = response?.body()?.string()
                mMainActivity.runOnUiThread {
                    srl_home_rank.isRefreshing = false
                    if (StatusCode == 200) {
                        rankData = Gson().fromJson(ResMessage, Rank::class.java)
                        rv_home_rank.layoutManager = LinearLayoutManager(mMainActivity, LinearLayoutManager.VERTICAL, false)
                        val rankAdapter = RankAdapter()
                        rv_home_rank.adapter = rankAdapter
                    }
                }
            }


        })
    }


    inner class RankAdapter : RecyclerView.Adapter<RankAdapter.ViewHolder>() {
        override fun getItemCount(): Int {
            return 10
        }

        override fun onBindViewHolder(holder: ViewHolder?, position: Int) {

            holder!!.tv_block_topic.text = "可愛 No. ${position}"
            holder!!.tv_block_count.text = "票數差：${rankData?.images?.feature_1!![position].real_count}"
            holder!!.tv_block_fileName.text = rankData?.images?.feature_1!![position].image_name

        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
            val context = parent?.context

            val view = LayoutInflater.from(context).inflate(R.layout.block_rank, parent, false)
            return ViewHolder(view)
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val tv_block_topic = itemView.tv_block_topic
            val tv_block_count = itemView.tv_block_count
            val tv_block_fileName = itemView.tv_block_fileName
        }

    }


}