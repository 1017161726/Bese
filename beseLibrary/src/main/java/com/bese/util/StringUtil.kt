package com.bese.util

import android.text.InputFilter
import android.text.TextUtils

import java.io.UnsupportedEncodingException
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.regex.Pattern

/**
 * String处理的工具类
 */
object StringUtil {

    var MONEY_ZERO = "¥0.00"
    var AMOUNT_ZERO = "0.00"

    var RMB_CHINESE = "￥"
    var RMB_NUMBER = "¥"
    var DOLLAR = "$"

    var SPACE_CHINESE = "　"

    /**
     * 手机号正则
     */
    private val MOBILE_PATTERN = Pattern.compile("^[1][3-9][0-9]{9}$")

    /**
     * 价格正则
     */
    private val PRICE_PATTERN = Pattern.compile("^\\d+.?\\d*$")

    /**
     * emoji表情正则
     */
    private val EMOJI_PATTERN = Pattern.compile("[^\\u0000-\\uFFFF]")

    /**
     * 验证邮箱 的正则
     */
    private val EMAIL_PATTERN = Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$")

    /**
     * 特殊字符集
     */

    var FILTER_SPECIAL = "` ~!@#$%^&*()_+=|{}':;,\\[\\].<>/~！×@#￥%……&*（）——+|{}【】‘；：”“’。，、？?\"\\\\-》《"

    var emojiFilter = InputFilter { source, _, _, _, _, _ ->
        if (isEmoji(source.toString())) {
            ""
        } else {
            source
        }
    }

    /**
     * 验证手机号
     */
    fun isMobile(str: String): Boolean {
        if (TextUtils.isEmpty(str)) {
            return false
        }
        val matcher = MOBILE_PATTERN.matcher(str)
        return matcher.matches()
    }

    fun checkingEmail(email: String): Boolean {
        if (TextUtils.isEmpty(email)) {
            return false
        }
        val matcher = EMAIL_PATTERN.matcher(email)
        return matcher.matches()
    }

    /**
     * 判断是否是emoji表情
     *
     * @param codePoint 比较的单个字符
     * @return
     */
    fun isEmojiCharacter(codePoint: Char): Boolean {
        return codePoint.toInt() == 0x0 || codePoint.toInt() == 0x9 || codePoint.toInt() == 0xA || codePoint.toInt() == 0xD
                || codePoint.toInt() in 0x20..0xD7FF || codePoint.toInt() in 0xE000..0xFFFD || codePoint.toInt() in 0x10000..0x10FFFF
    }

    /**
     * 检测String是否包含中文
     *
     * @param name
     * @return
     */
    fun containsChinese(name: String): Boolean {
        if (TextUtils.isEmpty(name)) {
            return false
        }
        val cTemp = name.toCharArray()
        if (cTemp.isEmpty()) {
            return false
        }
        for (element in name) {
            if (isChineseChar(element)) {
                return true
            }
        }
        return false
    }

    fun filterSpecialChar(key: String): String {
        val str = StringBuilder()
        if (!TextUtils.isEmpty(key)) {
            for (i in key.indices) {
                if (!FILTER_SPECIAL.contains(key[i].toString())) {
                    str.append(key[i])
                }
            }
        }
        return str.toString()
    }


    /**
     * 判定输入汉字
     *
     * @param c
     * @return
     */
    private fun isChineseChar(c: Char): Boolean {
        val ub = Character.UnicodeBlock.of(c)
        val isChineseCharFlag: Boolean
        isChineseCharFlag = when {
            ub === Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS -> true
            ub === Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS -> true
            ub === Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A -> true
            ub === Character.UnicodeBlock.GENERAL_PUNCTUATION -> true
            ub === Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION -> true
            else -> ub === Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
        }
        return isChineseCharFlag
    }

    fun trim(key: String): String {
        var str = ""
        if (!TextUtils.isEmpty(key)) {
            str = key
            while (str.startsWith(" ")) {
                str = str.substring(1)
            }
            while (str.endsWith(" ")) {
                str = str.substring(0, str.length - 1)
            }
            return str
        }
        return str
    }

    /**
     * 获取输入数字格式化后的价格
     * 纯价格展示，两位小数，不带人民币符号
     */
    fun getFormatPrice(p: String): String {
        var price = p
        if (TextUtils.isEmpty(price) || "NULL".equals(price, ignoreCase = true)) {
            return AMOUNT_ZERO
        }
        price = price.trim { it <= ' ' }
        if (price.startsWith(RMB_CHINESE) || price.startsWith(RMB_NUMBER) || price.startsWith(
                DOLLAR
            )) {
            price = price.substring(1)
        }

        return if (isDouble(price)) {
            val df = DecimalFormat("##0.00")
            df.roundingMode = RoundingMode.FLOOR
            df.format(java.lang.Double.parseDouble(price))
        } else {
            // 价格不符合价格规则，可以收集日志
            price
        }
    }


    /**
     * 获取输入数字格式化后的价格: 长度很大时格式化以万计或亿计
     */
    fun getFormatPriceWithRMB(price: String, isFormat: Boolean = false): String {
        val amount = getFormatPrice(price)
        var priceUnit = AMOUNT_ZERO
        if (isFormat) {
            if (!TextUtils.isEmpty(amount) && amount.contains(".")) {
                val pre =
                    amount.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
                when {
                    pre.length <= 4 -> priceUnit = amount
                    pre.length <= 8 -> {
                        // 以万为单位处理
                        var p = java.lang.Double.parseDouble(pre)
                        p /= 10000
                        val df = DecimalFormat("##0.00")
                        df.roundingMode = RoundingMode.FLOOR
                        priceUnit = df.format(p) + "万"
                    }
                    else -> {
                        // 以亿为单位处理，包括大于万亿以外的情况
                        var p = java.lang.Double.parseDouble(pre)
                        p /= 100000000
                        val df = DecimalFormat("##0.00")
                        df.roundingMode = RoundingMode.FLOOR
                        priceUnit = df.format(p) + "亿"
                    }
                }
            }
        } else {
            priceUnit = amount
        }
        return RMB_NUMBER + priceUnit
    }

    /**
     * 判断是否为数字：防止字符串转double出错
     *
     * @param s 字符串
     * @return 是否是double
     */
    private fun isDouble(s: String): Boolean {
        val matcher = PRICE_PATTERN.matcher(s)
        return matcher.find()
    }


    /**
     * 手机号隐藏部分字符工具
     * 输入内容最好是标准手机号格式，如出错，可能返回格式不对，也可能返回 "****"
     */
    fun phoneHideCenter(phone: String): String {
        return if (!TextUtils.isEmpty(phone) && phone.length > 4) {
            phone.substring(0, 3) + "****" + phone.substring(phone.length - 4)
        } else {
            "****"
        }
    }

    /**
     * 手机号以344格式插入空格
     */
    fun phoneWithSpace(phone: String): String? {
        return if (!TextUtils.isEmpty(phone) && phone.length > 7) {
            phone.substring(0, 3) + " " + phone.substring(3, 7) + " " + phone.substring(phone.length - 4)
        } else {
            phone
        }
    }

    /**
     * 是否是中文字符
     *
     * @param str Char
     * @return 是否中文
     */
    fun containsCN(str: CharSequence): Boolean {
        try {
            val bytes = str.toString().toByteArray(charset("UTF-8"))
            return bytes.size != str.length
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }

        return false
    }

    /**
     * Emoji表情校验
     *
     * @param string
     * @return
     */
    fun isEmoji(string: String?): Boolean {
        return if (string != null) {
            //过滤Emoji表情
            val p = EMOJI_PATTERN
            //过滤Emoji表情和颜文字
            val m = p.matcher(string)
            m.find()
        } else {
            false
        }
    }
}