package com.ved.ui.base

import android.app.Application

/**
 * <>
 *
 * @author Fires
 * @date 2019/5/9
 */
class BaseApplication : Application() {

    companion object {
        var ctx: BaseApplication? = null
            private set
    }

    override fun onCreate() {
        super.onCreate()
        ctx = this
    }

}
