package com.bese.widget.inputview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.text.InputFilter
import android.text.TextUtils
import android.text.TextWatcher
import android.text.method.DigitsKeyListener
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.bese.R
import com.bese.util.StringUtil

import com.blankj.utilcode.util.SizeUtils

import java.io.UnsupportedEncodingException
import java.util.ArrayList
import java.util.Arrays

/**
 * 输入框
 */
class InputView @JvmOverloads constructor(private val mCtx: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(mCtx, attrs, defStyleAttr), View.OnClickListener {

    /**
     * 输入框
     */
    private var inputView: InputText? = null
    /**
     * 输入类型提示
     */
    private var tipTextView: TextView? = null
    /**
     * 删除按钮的字体
     */
    private var delTextView: TextView? = null
    /**
     * 只针对密码类型的密码可见开关
     */
    private var eyeTextView: TextView? = null
    /**
     * 输入框底部线
     */
    private var inputLine: View? = null

    /**
     * 密码可见性指示
     */
    private var isEyeOpen: Boolean = false
    /**
     * 是否单行展示
     */
    private var isSingleLine: Boolean = false
    /**
     * 是否允许长按事件
     */
    private var longClick: Boolean = false
    /**
     * 是否单行展示
     */
    private var tipIsIcon: Boolean = false
    /**
     * 是否展示底部线
     */
    private var isShowInputLine: Boolean = false
    /**
     * 是否可编辑
     */
    private var editable: Boolean = false
    /**
     * 输入文字的类型
     */
    private var inputType: Int = 0
    /**
     * 整个输入框底色
     */
    private var bgColor: Int = 0
    /**
     * hint文本颜色
     */
    private var hintColor: Int = 0
    /**
     * 用户自定义输入最大长度
     * 需要整合到空格过滤器中
     */
    private var userMaxLength: Int = 0
    /**
     * 整个输入框布局
     */
    private var inputLayout: LinearLayout? = null

    val editText: EditText?
        get() = inputView


    val inputText: String
        get() = inputView?.text.toString()

    /** Space过滤器 */
    private val spaceFilter = InputFilter { source, _, _, _, _, _ ->
        if (" " == source) { "" }
        null
    }

    /** 汉字符过滤器 */
    private val cnFilter = InputFilter { source, _, _, _, _, _ ->
        if (isCN(source)) { "" }
        null
    }

    private val textWatcher = object : TextWatcher {

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {
            if (s != null) {
                if (!TextUtils.isEmpty(s.toString())) {
                    setClearVisible(true)
                } else {
                    setClearVisible(false)
                }
            }
        }
    }

    private var inputListener: InputListener? = null

    init {
        init(mCtx, attrs)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun init(context: Context, attrs: AttributeSet?) {
        orientation = HORIZONTAL
        LayoutInflater.from(context).inflate(R.layout.layout_input_edit, this)
        inputLayout = findViewById(R.id.layout_input)
        inputView = findViewById(R.id.et_input)
        tipTextView = findViewById(R.id.tv_tip)
        delTextView = findViewById(R.id.tv_clear)
        eyeTextView = findViewById(R.id.tv_eye)
        inputLine = findViewById(R.id.input_line)
        inputView?.isFocusable = true

        DEFAULT_TEXT_SIZE = SizeUtils.sp2px(16f).toFloat()
        val params = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        inputLayout?.layoutParams = params

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.InputView)
        val tipText = typedArray.getString(R.styleable.InputView_inputTipText)
        val tipColor = typedArray.getColor(R.styleable.InputView_inputTipColor,
            DEFAULT_TEXT_COLOR
        )
        val tipWidth = typedArray.getDimension(R.styleable.InputView_inputTipWidth, 0f)
        val tipPaddingLeft = typedArray.getDimension(R.styleable.InputView_inputTipPaddingLeft, 0f)
        val tipPaddingRight =
            typedArray.getDimension(R.styleable.InputView_inputTipPaddingRight, 0f)
        tipIsIcon = typedArray.getBoolean(R.styleable.InputView_inputTipIsIcon, false)
        val showText = typedArray.getString(R.styleable.InputView_inputLainText)
        val showColor =
            typedArray.getColor(R.styleable.InputView_inputLainTextColor,
                DEFAULT_TEXT_COLOR
            )
        val hintText = typedArray.getString(R.styleable.InputView_inputHintText)
        hintColor = typedArray.getColor(R.styleable.InputView_inputHintColor,
            DEFAULT_HINT_COLOR
        )
        val txtSize =
            typedArray.getDimension(R.styleable.InputView_inputAllTextSize,
                DEFAULT_TEXT_SIZE
            )
        longClick = typedArray.getBoolean(R.styleable.InputView_inputLongClickable, true)
        isSingleLine = typedArray.getBoolean(R.styleable.InputView_inputIsSingleLine, true)
        val inputMinLine = typedArray.getInt(R.styleable.InputView_inputMinLine, 2)
        val inputMaxLine = typedArray.getInt(R.styleable.InputView_inputMaxLine, 5)
        isShowInputLine = typedArray.getBoolean(R.styleable.InputView_inputIsShowBottomLine, false)
        val bottomLineColor =
            typedArray.getColor(R.styleable.InputView_inputBottomLineColor,
                DEFAULT_HINT_COLOR
            )
        val bottomLineFocusedColor = typedArray.getColor(
            R.styleable.InputView_inputBottomLineFocusedColor,
            DEFAULT_FOCUS_COLOR
        )
        val lineMargin = typedArray.getDimension(R.styleable.InputView_inputBottomLineMargin, 0f)
        val inputAreaBgRes = typedArray.getResourceId(R.styleable.InputView_inputAreaBackground, 0)
        editable = typedArray.getBoolean(R.styleable.InputView_editEnable, true)
        inputType = typedArray.getInt(R.styleable.InputView_inputType,
            NON
        )
        bgColor = typedArray.getColor(R.styleable.InputView_inputBgColor, Color.TRANSPARENT)
        val maxLength = typedArray.getInt(R.styleable.InputView_inputMaxLength, -1)
        typedArray.recycle()


        inputLayout?.setBackgroundColor(bgColor)

        setTipText(tipText)

        setTipColor(tipColor)

        if (tipWidth != 0f) {
            setTipWidth(tipWidth)
        }

        if (tipPaddingLeft != 0f || tipPaddingRight != 0f) {
            setTipAreaPadding(tipPaddingLeft, tipPaddingRight)
        }

        setHintText(hintText, hintColor)

        setShowText(showText)
        setLainTextColor(showColor)

        setIsSingleLine(isSingleLine)

        setLineCount(inputMinLine, inputMaxLine)

        setAllTextSize(SizeUtils.px2sp(txtSize).toFloat(), tipIsIcon)

        setInputBottomLine(isShowInputLine, bottomLineColor, lineMargin)

        setInputEditable(editable)

        setInputAreaBackground(inputAreaBgRes)

        if (inputType != NON) {
            setInputType(inputType)
        }

        // 密码类型Eye可见，前提是可编辑
        setEyeVisible(editable)

        delTextView?.setOnClickListener(this)
        eyeTextView?.setOnClickListener(this)

        inputView?.addTextChangedListener(textWatcher)

        inputView?.setOnFocusChangeListener { v, hasFocus ->
            inputView?.isCursorVisible = hasFocus
            if (isShowInputLine) {
                inputLine?.setBackgroundColor(if (hasFocus) bottomLineFocusedColor else DEFAULT_HINT_COLOR)
            }
            if (inputListener != null) {
                inputListener?.focusListener(hasFocus)
            }
            setClearVisible(hasFocus)
        }

        if (!isSingleLine) {
            inputView?.setOnTouchListener { v, event ->
                if (event.action == MotionEvent.ACTION_UP) {
                    // 把滚动事件恢复给父Scrollview
                    parent.requestDisallowInterceptTouchEvent(false)
                } else if (event.action == MotionEvent.ACTION_MOVE) {
                    parent.requestDisallowInterceptTouchEvent(true)
                }
                false
            }
        }

        //        设置最大长度
        if (maxLength != -1) {
            setInputMaxLength(maxLength)
        }

        //        setInputFilter(StringUtil.emojiFilter);
        //        添加表情过滤
        val inputFilters = ArrayList(Arrays.asList(*inputView?.filters))
        inputFilters.add(StringUtil.emojiFilter)
        inputView?.filters = inputFilters.toTypedArray()
    }

    fun setInputFilter(vararg filter: InputFilter) {
        inputView?.filters = filter
    }

    fun setTextWatcher(watcher: TextWatcher?) {
        if (watcher != null) {
            inputView?.addTextChangedListener(watcher)
        }
    }

    /**
     * 设置提示文案
     *
     * @param txt 文案
     */
    fun setTipText(txt: String?) {
        if (TextUtils.isEmpty(txt)) {
            tipTextView?.text = ""
            tipTextView?.visibility = View.GONE
        } else {
            tipTextView?.text = txt
        }
    }

    /**
     * 设置提示文案颜色
     *
     * @param color 文案
     */
    fun setTipColor(color: Int) {
        if (color != 0) {
            tipTextView?.setTextColor(color)
        }
    }

    /**
     * 设置Hint提示
     *
     * @param hint 提示
     */
    fun setHintText(hint: String?, hintColor: Int) {
        var hint = hint
        hint = if (TextUtils.isEmpty(hint)) "" else hint
        inputView?.hint = hint
        inputView?.setHintTextColor(hintColor)
    }

    /**
     * 设置编辑文本框的初始内容
     *
     * @param txt 内容
     */
    fun setShowText(txt: String?) {
        inputView?.setText(txt)
    }

    /**
     * 设置编辑文本框的初始内容
     *
     * @param color 内容
     */
    fun setLainTextColor(color: Int) {
        inputView?.setTextColor(color)
    }

    /**
     * 设置编辑文本框的初始内容
     *
     * @param txt 内容
     */
    fun setTipWidth(txt: Float) {
        tipTextView?.width = txt.toInt()
    }

    /**
     * 设置编辑文本框的初始内容
     *
     * @param left  左内边距
     * @param right 右内边距
     */
    fun setTipAreaPadding(left: Float, right: Float) {
        tipTextView?.setPadding(left.toInt(), 0, right.toInt(), 0)
    }

    /**
     * 设置字号，图标字号1.16倍
     *
     * @param spValue 字号，sp值
     */
    fun setAllTextSize(spValue: Float, isIcon: Boolean) {
        inputView?.textSize = spValue
        delTextView?.textSize = spValue * 1.16f
        eyeTextView?.textSize = spValue * 1.16f
        if (isIcon) {
            tipTextView?.textSize = spValue * 1.16f
        } else {
            tipTextView?.textSize = spValue
        }
    }

    /**
     * 输入框是否单行展示：多行不展示清空按钮，不展示可见按钮
     */
    private fun setIsSingleLine(isSingleLine: Boolean) {
        inputView?.isSingleLine = isSingleLine
    }

    /**
     * 输入框是否单行展示：多行不展示清空按钮，不展示可见按钮
     */
    private fun setLineCount(minLine: Int, maxLine: Int) {
        if (!isSingleLine) {
            inputView?.minLines = minLine
            inputView?.maxLines = maxLine
        }
    }

    fun setInputBottomLine(isShow: Boolean, lineColor: Int, margin: Float) {
        if (isShow) {
            inputLine?.visibility = View.VISIBLE
            inputLine?.setBackgroundColor(lineColor)
            val params = RelativeLayout.LayoutParams(inputLine?.layoutParams)
            params.setMargins(0, margin.toInt(), 0, 0)
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
            inputLine?.layoutParams = params
            inputView?.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    inputLine?.setBackgroundColor(Color.parseColor("#0099ff"))
                } else {
                    inputLine?.setBackgroundColor(Color.parseColor("#ced3d9"))
                }
            }
        } else {
            inputLine?.visibility = View.GONE
        }
    }

    /**
     * 设置可编辑性：不可编辑不展示Eye和Clear
     *
     * @param editable 是否可编辑
     */
    private fun setInputEditable(editable: Boolean) {
        inputView?.isClickable = editable
        inputView?.isFocusable = editable
        setClearVisible(editable)
        setEyeVisible(editable)
    }

    private fun setInputAreaBackground(res: Int) {
        if (res != 0) {
            inputView?.setBackgroundResource(res)
        } else {
            inputView?.background = null
        }
    }

    /**
     * 设置输入长度限制
     */
    fun setInputMaxLength(length: Int) {
        userMaxLength = length
        setInputFilter(InputFilter.LengthFilter(userMaxLength))
    }

    /** 判断一个字符是否是汉字符 */
    private fun isCN(str: CharSequence): Boolean {
        try {
            val bytes = str.toString().toByteArray(charset("UTF-8"))
            return bytes.size != str.length
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        return false
    }

    /**
     * 设置输入类型：
     *
     * @param inputType 类型
     */
    private fun setInputType(inputType: Int) {
        when (inputType) {
            // 普通文本，输入无限制
            TEXT -> inputView?.inputType = EditorInfo.TYPE_CLASS_TEXT
            // 纯数字限制
            DIGITS -> {
                inputView?.inputType = EditorInfo.TYPE_CLASS_NUMBER
                // 设置EditText的监听规则，只允许输入0-9
                inputView?.keyListener = DigitsKeyListener.getInstance("0123456789")
            }
            // 金额样式，能输入数字加小数点，没有负号
            AMOUNT -> {
                inputView?.inputType = EditorInfo.TYPE_CLASS_NUMBER
                inputView?.keyListener = DigitsKeyListener.getInstance("0123456789.")
            }
            // 数字模式，数字键盘内的都可以输入，加减乘除斜杠符
            NUMBER -> inputView?.inputType = EditorInfo.TYPE_CLASS_NUMBER
            // 手机号模式，不限制是否以1开头，限制只能输入0-9，最多11位
            PHONE -> {
                inputView?.inputType = EditorInfo.TYPE_CLASS_NUMBER
                // 设置EditText的监听规则，只允许输入0-9
                inputView?.keyListener = DigitsKeyListener.getInstance("0123456789")
                inputView?.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(11))
            }
            // 密码模式，默认限制输入空格
            PASSWORD -> {
                // 设置密码属性，文字展示成密码类型，默认隐式密码
                inputView?.inputType = EditorInfo.TYPE_TEXT_VARIATION_PASSWORD
                inputView?.transformationMethod = PasswordTransformationMethod.getInstance()
                // 不可输入空格
                setInputFilter(
                    spaceFilter,
                    cnFilter,
                    InputFilter.LengthFilter(DEFAULT_PASSWORD_MAX_LENGTH)
                )
                isEyeOpen = false
                if (longClick) {
                    inputView?.setShouldCopyAndPaste(false)
                }
            }
            else -> {
            }
        }
    }

    /**
     * 清空输入框
     */
    fun clearInput() {
        inputView?.setText("")
    }

    /**
     * 改变密码可见性
     */
    fun changeEyeState() {
        if (isEyeOpen) {
            eyeTextView?.text = mCtx.resources.getString(R.string.icon_hide)
            inputView?.transformationMethod = PasswordTransformationMethod.getInstance()
            inputView?.setSelection(inputView?.length() ?: 0)
        } else {
            eyeTextView?.text = mCtx.resources.getString(R.string.icon_display)
            inputView?.transformationMethod = HideReturnsTransformationMethod.getInstance()
            inputView?.setSelection(inputView?.length() ?: 0)
        }
        isEyeOpen = !isEyeOpen
    }

    /**
     * 设置Clear按钮可见性 多行时不展示
     *
     * @param visible 是否可见
     */
    private fun setClearVisible(visible: Boolean) {
        val isEmpty = TextUtils.isEmpty(inputView?.text.toString())
        val bool = editable && !isEmpty && isSingleLine && visible && hasFocus()
        delTextView?.visibility = if (bool) View.VISIBLE else View.GONE
        delTextView?.isClickable = bool
    }

    /**
     * 设置Eye按钮可见性
     *
     * @param editable 是否可见
     */
    private fun setEyeVisible(editable: Boolean) {
        eyeTextView?.visibility =
            if (inputType == PASSWORD && editable) View.VISIBLE else View.GONE
    }

    override fun onClick(v: View) {
        val i = v.id
        if (i == R.id.tv_clear) {
            clearInput()
        } else if (i == R.id.tv_eye) {
            changeEyeState()
        }
    }

    fun setInputListener(listener: InputListener) {
        inputListener = listener
    }

    interface InputListener {
        /**
         * 焦点事件
         *
         * @param hasFocus 是否有焦点
         */
        fun focusListener(hasFocus: Boolean)
    }

    companion object {

        private val NON = -1
        private val TEXT = 0
        private val DIGITS = 1
        private val AMOUNT = 5
        private val NUMBER = 2
        private val PHONE = 3
        private val PASSWORD = 4

        private val DEFAULT_TEXT_COLOR = Color.parseColor("#333333")
        private val DEFAULT_HINT_COLOR = Color.parseColor("#C2C6CC")
        private val DEFAULT_FOCUS_COLOR = Color.parseColor("#0099ff")
        private var DEFAULT_TEXT_SIZE: Float = 0.toFloat()
        private val DEFAULT_PASSWORD_MAX_LENGTH = 16
    }

}
