package edu.tjrac.swant.biubiu.view

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import edu.tjrac.swant.baselib.common.base.BaseActivity
import edu.tjrac.swant.baselib.util.SUtil
import edu.tjrac.swant.baselib.util.UiUtil
import edu.tjrac.swant.biubiu.R
import edu.tjrac.swant.biubiu.adapter.AlbumListAdapter
import edu.tjrac.swant.biubiu.bean.Album
import edu.tjrac.swant.biubiu.bean.Tags
import edu.tjrac.swant.biubiu.listener.AlbumListClickListener
import edu.tjrac.swant.biubiu.net.BR
import edu.tjrac.swant.biubiu.net.NESubscriber
import edu.tjrac.swant.biubiu.net.Net
import edu.tjrac.swant.biubiu.net.RxUtil
import kotlinx.android.synthetic.main.activity_meitu_search.*

class MeituSearchActivity : BaseActivity(), View.OnClickListener {
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_back -> {
                finish()
            }
            R.id.bt_search -> {
                pageNo=1
                search = et_search?.text.toString()
                if (!SUtil.isEmpty(search)) {
                    tag = null
                    search()
                }

            }
        }
    }

    var mInflater: LayoutInflater? = null
    var pageNo = 1
    var pageSize = 20

    var search: String? = null
    var tag: String? = null

    var adapter: AlbumListAdapter? = null
    var data: ArrayList<Album>? = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mInflater = LayoutInflater.from(this)
        setContentView(R.layout.activity_meitu_search)
        iv_back?.setOnClickListener(this)
        bt_search?.setOnClickListener(this)
        et_search?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (SUtil.isEmpty(s.toString())) {
                    data?.clear()
                    adapter?.notifyDataSetChanged()
                    recycler?.visibility = View.INVISIBLE
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })
        gethottags()
        adapter = AlbumListAdapter(R.layout.item_meitu_colum, data)
        adapter?.setOnItemClickListener(AlbumListClickListener(this))
        adapter?.setOnLoadMoreListener {
            pageNo++
            search()
        }
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter
    }

    private fun gethottags() {
        Net.instance.getApiService().getHotTags()
                .compose(RxUtil.applySchedulers())
                .subscribe(object : NESubscriber<BR<ArrayList<Tags>>>(this) {
                    override fun onSuccess(t: BR<ArrayList<Tags>>?) {
                        if (null != t?.data) {
                            onGetTagsSuccess(t?.data!!)
                        }
                    }
                })
    }


    private fun onGetTagsSuccess(data: ArrayList<Tags>) {
        var dp12 = UiUtil.dp2px(this!!, 12).toInt()
        var dp4 = UiUtil.dp2px(this!!, 4).toInt()
        tfl.removeAllViews()

        for (item in data) {
            var v = mInflater?.inflate(R.layout.search_page_flowlayout_tv, null) as TextView
            v.text = item?.name!!
            var layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT)
            layoutParams.setMargins(0, 0, dp12, dp4)
            v.layoutParams = layoutParams
            v.setOnClickListener {
                pageNo=1
                tag = item?.name!!
                search()
            }
            tfl.addView(v)
        }
    }

    private fun search() {
        if (pageNo == 1) {
//            data?.clear()
//            adapter?.notifyDataSetChanged()
            recycler.visibility = View.VISIBLE
        }
        Net.instance.getApiService().getColumList(pageSize, pageNo, tag, search)
                .compose(RxUtil.applySchedulers())
                .subscribe(object : NESubscriber<BR<ArrayList<Album>>>(this) {
                    override fun onSuccess(t: BR<ArrayList<Album>>?) {

                        if (null != t?.data) {
                            if (t?.data!!.size > 0) {
                                data?.addAll(t?.data!!)
                                adapter?.loadMoreComplete()
                            } else {
                                adapter?.loadMoreEnd()
                            }
                        }
                    }
                })
    }
}
