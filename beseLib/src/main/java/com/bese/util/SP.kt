package com.bese.util

import com.blankj.utilcode.util.SPUtils

/**
 * 通过SharedPreferences来存储数据，自定义类型
 */
object SP {

    private const val SP_NAME = "sp_config"

    /**
     * 保存整数SP
     * @param key Key
     * @param value 整型值
     */
    fun save(key: String, value: Int) {
        SPUtils.getInstance(SP_NAME).put(key, value)
    }

    /**保存Long型值 */
    fun save(key: String, value: Long) {
        SPUtils.getInstance(SP_NAME).put(key, value)
    }

    /**保存布尔型值 */
    fun save(key: String, value: Boolean) {
        SPUtils.getInstance(SP_NAME).put(key, value)
    }

    /**保存String型值 */
    fun save(key: String, value: String?) {
        SPUtils.getInstance(SP_NAME).put(key, value)
    }

    /**获取int型值 */
    fun getInt(key: String): Int? {
        return SPUtils.getInstance(SP_NAME).getInt(key)
    }
    fun getInt(key: String, def: Int): Int {
        return SPUtils.getInstance(SP_NAME).getInt(key, def)
    }

    /**获取Long型值 */
    fun getLong(key: String): Long? {
        return SPUtils.getInstance(SP_NAME).getLong(key)
    }
    fun getLong(key: String, def: Long): Long {
        return SPUtils.getInstance(SP_NAME).getLong(key, def)
    }

    /**获取String型值 */
    fun getString(key: String): String? {
        return SPUtils.getInstance(SP_NAME).getString(key)
    }
    fun getString(key: String, def: String): String? {
        return SPUtils.getInstance(SP_NAME).getString(key, def)
    }

    /**获取布尔型值 */
    fun getBoolean(key: String): Boolean? {
        return SPUtils.getInstance(SP_NAME).getBoolean(key)
    }
    fun getBoolean(key: String, def: Boolean): Boolean {
        return SPUtils.getInstance(SP_NAME).getBoolean(key, def)
    }

    /**删除一个Key */
    fun deleteKey(key: String) {
        SPUtils.getInstance(SP_NAME).remove(key)
    }

    /**判断是否包含Key */
    fun containsKey(key: String): Boolean {
        return SPUtils.getInstance(SP_NAME).contains(key)
    }

}
