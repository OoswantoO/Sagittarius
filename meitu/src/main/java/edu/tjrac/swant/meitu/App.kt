package edu.tjrac.swant.meitu

import android.content.Context
import android.content.SharedPreferences
import android.support.v7.app.AppCompatDelegate
import edu.tjrac.swant.baselib.common.base.BaseApplication
import edu.tjrac.swant.baselib.util.StringUtils
import edu.tjrac.swant.meitu.bean.User

/**
 * Created by wpc on 2019-11-20.
 */
open class App : BaseApplication() {
    companion object {
        var context: Context? = null
        var needReStart = false
        var sp: SharedPreferences? = null
            get() {
                if (field == null) {
                    field = context?.getSharedPreferences(Config.SP.CACHE, Context.MODE_PRIVATE)
                }
                return field
            }
        var token: String? = ""
            set(value) {
                sp?.edit()?.putString(Config.SP.TOKEN, value)?.commit()
                field = token
            }
            get() {
                if(StringUtils.isEmpty(field)){
                    field = sp?.getString(Config.SP.TOKEN, "")
                }
                return field
            }
        var isNightMode: Boolean? = null
            get() {
                if (null == field) {
                    field = sp?.getBoolean(Config.SP.ISNIGHT_MODE, true)
                }
                return field
            }
            set(value) {
                field = value
                needReStart = true
                sp?.edit()?.putBoolean(Config.SP.ISNIGHT_MODE, value!!)?.commit()
            }
        var followSystem: Boolean? = null
            get() {
                if (null == field) {
                    field = sp?.getBoolean(Config.SP.LANGUAGE_FOLLOW_SYSTEM, false)
                }
                return field
            }
            set(value) {
                field = value
                needReStart = true
                sp?.edit()?.putBoolean(Config.SP.LANGUAGE_FOLLOW_SYSTEM, value!!)?.commit()
            }
        var languageSetting: Int? = 0
            get() {
                if (null == field) {
                    field = sp?.getInt(Config.SP.LANGUAGE_SETTING, 0)
                }
                return field
            }
            set(value) {
                field = value
                needReStart = true
                sp?.edit()?.putInt(Config.SP.LANGUAGE_SETTING, value!!)?.commit()
            }
        var loged: User? = User(1)
    }


    override fun onCreate() {
        context = this
        super.onCreate()
        if (isNightMode!!) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}