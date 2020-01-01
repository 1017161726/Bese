package com.bese.ui.base

import android.app.Application


/**
 * <> >
 *
 * @author Fires
 * @date 2019/5/9
 */
class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        ctx = this

    }

    companion object {
        var ctx: BaseApplication? = null
            private set
    }
}
