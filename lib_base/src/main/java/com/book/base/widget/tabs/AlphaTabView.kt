package com.book.base.widget.tabs

import android.content.Context
import android.graphics.*
import android.graphics.Paint.FontMetricsInt
import android.graphics.drawable.BitmapDrawable
import android.os.Looper
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import com.book.base.R

class AlphaTabView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private var mContext //上下文
            : Context? = null
    private var mIconNormal //默认图标
            : Bitmap? = null
    private var mIconSelected //选中的图标
            : Bitmap? = null
    private var mText //描述文本
            : String? = null
    private var mTextColorNormal = -0x666667 //描述文本的默认显示颜色

    private var mTextColorSelected = -0xb93fe5 //述文本的默认选中显示颜色

    private var mTextSize = 12 //描述文本的默认字体大小 12sp

    private var mPadding = 5 //文字和图片之间的距离 5dp


    private var mAlpha //当前的透明度
            = 0f
    private val mSelectedPaint = Paint() //背景的画笔

    private val mIconAvailableRect = Rect() //图标可用的绘制区域

    private val mIconDrawRect = Rect() //图标真正的绘制区域

    private var mTextPaint //描述文本的画笔
            : Paint? = null
    private var mTextBound //描述文本矩形测量大小
            : Rect? = null
    private var mFmi //用于获取字体的各种属性
            : FontMetricsInt? = null

    private var isShowRemove //是否移除当前角标
            = false
    private var isShowPoint //是否显示圆点
            = false
    private var mBadgeNumber //角标数
            = 0
    private var mBadgeBackgroundColor = -0x10000 //默认红颜色


    constructor(context: Context) : this(context, null)

    init {
        mContext = context
        mTextSize = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            mTextSize.toFloat(),
            resources.displayMetrics
        ).toInt()
        mPadding = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            mPadding.toFloat(),
            resources.displayMetrics
        ).toInt()
        //获取所有的自定义属性
        val a = context.obtainStyledAttributes(attrs, R.styleable.AlphaTabView)
        val iconNormal =
            a.getDrawable(R.styleable.AlphaTabView_tabIconNormal) as BitmapDrawable?
        if (iconNormal != null) {
            mIconNormal = iconNormal.bitmap
        }
        val iconSelected =
            a.getDrawable(R.styleable.AlphaTabView_tabIconSelected) as BitmapDrawable?
        if (iconSelected != null) {
            mIconSelected = iconSelected.bitmap
        }
        if (null != mIconNormal) {
            mIconSelected = if (null == mIconSelected) mIconNormal else mIconSelected
        } else {
            mIconNormal = if (null == mIconSelected) mIconNormal else mIconSelected
        }
        mText = a.getString(R.styleable.AlphaTabView_tabText)
        mTextSize = a.getDimensionPixelSize(R.styleable.AlphaTabView_tabTextSize, mTextSize)
        mTextColorNormal = a.getColor(R.styleable.AlphaTabView_textColorNormal, mTextColorNormal)
        mTextColorSelected =
            a.getColor(R.styleable.AlphaTabView_textColorSelected, mTextColorSelected)
        mBadgeBackgroundColor =
            a.getColor(R.styleable.AlphaTabView_badgeBackgroundColor, mBadgeBackgroundColor)
        mPadding =
            a.getDimension(R.styleable.AlphaTabView_paddingTexwithIcon, mPadding.toFloat()).toInt()
        a.recycle()
        initText()
    }

    /**
     * 如果有设置文字就获取文字的区域大小
     */
    private fun initText() {
        if (mText != null) {
            mTextBound = Rect()
            mTextPaint = Paint()
            mTextPaint!!.textSize = mTextSize.toFloat()
            mTextPaint!!.isAntiAlias = true
            mTextPaint!!.isDither = true
            mTextPaint!!.getTextBounds(mText, 0, mText!!.length, mTextBound)
            mFmi = mTextPaint!!.fontMetricsInt
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        require(!(mText == null && (mIconNormal == null || mIconSelected == null))) { "" }
        val paddingLeft = paddingLeft
        val paddingTop = paddingTop
        val paddingRight = paddingRight
        val paddingBottom = paddingBottom
        val measuredWidth = measuredWidth
        val measuredHeight = measuredHeight

        //计算出可用绘图的区域
        val availableWidth = measuredWidth - paddingLeft - paddingRight
        var availableHeight = measuredHeight - paddingTop - paddingBottom
        if (mText != null && mIconNormal != null) {
            availableHeight -= mTextBound!!.height() + mPadding
            //计算出图标可以绘制的画布大小
            mIconAvailableRect[paddingLeft, paddingTop, paddingLeft + availableWidth] =
                paddingTop + availableHeight
            //计算文字的绘图区域
            val textLeft = paddingLeft + (availableWidth - mTextBound!!.width()) / 2
            val textTop = mIconAvailableRect.bottom + mPadding
            mTextBound!![textLeft, textTop, textLeft + mTextBound!!.width()] =
                textTop + mTextBound!!.height()
        } else if (mText == null) {
            //计算出图标可以绘制的画布大小
            mIconAvailableRect[paddingLeft, paddingTop, paddingLeft + availableWidth] =
                paddingTop + availableHeight
        } else if (mIconNormal == null) {
            //计算文字的绘图区域
            val textLeft = paddingLeft + (availableWidth - mTextBound!!.width()) / 2
            val textTop = paddingTop + (availableHeight - mTextBound!!.height()) / 2
            mTextBound!![textLeft, textTop, textLeft + mTextBound!!.width()] =
                textTop + mTextBound!!.height()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val alpha = Math.ceil(mAlpha * 255.toDouble()).toInt()
        if (mIconNormal != null && mIconSelected != null) {
            val drawRect =
                availableToDrawRect(mIconAvailableRect, mIconNormal!!)
            mSelectedPaint.reset()
            mSelectedPaint.isAntiAlias = true //设置抗锯齿
            mSelectedPaint.isFilterBitmap = true //抗锯齿
            mSelectedPaint.alpha = 255 - alpha
            canvas.drawBitmap(mIconNormal!!, null, drawRect, mSelectedPaint)
            mSelectedPaint.reset()
            mSelectedPaint.isAntiAlias = true //设置抗锯齿
            mSelectedPaint.isFilterBitmap = true //抗锯齿
            mSelectedPaint.alpha = alpha //setAlpha必须放在paint的属性最后设置，否则不起作用
            canvas.drawBitmap(mIconSelected!!, null, drawRect, mSelectedPaint)
        }
        if (mText != null) {
            //绘制原始文字,setAlpha必须放在paint的属性最后设置，否则不起作用
            mTextPaint!!.color = mTextColorNormal
            mTextPaint!!.alpha = 255 - alpha
            //由于在该方法中，y轴坐标代表的是baseLine的值，经测试，mTextBound.height() + mFmi.bottom 就是字体的高
            //所以在最后绘制前，修正偏移量，将文字向上修正 mFmi.bottom / 2 即可实现垂直居中
            canvas.drawText(
                mText!!,
                mTextBound!!.left.toFloat(),
                mTextBound!!.bottom - mFmi!!.bottom / 2.toFloat(),
                mTextPaint!!
            )
            //绘制变色文字，setAlpha必须放在paint的属性最后设置，否则不起作用
            mTextPaint!!.color = mTextColorSelected
            mTextPaint!!.alpha = alpha
            canvas.drawText(
                mText!!,
                mTextBound!!.left.toFloat(),
                mTextBound!!.bottom - mFmi!!.bottom / 2.toFloat(),
                mTextPaint!!
            )
        }

        //绘制角标
        if (!isShowRemove) {
            drawBadge(canvas)
        }
    }

    /**
     * badge
     */
    private fun drawBadge(canvas: Canvas) {
        var i = measuredWidth / 14
        val j = measuredHeight / 9
        i = if (i >= j) j else i
        if (mBadgeNumber > 0) {
            val backgroundPaint = Paint()
            backgroundPaint.color = mBadgeBackgroundColor
            backgroundPaint.isAntiAlias = true
            val number = if (mBadgeNumber > 99) "99+" else mBadgeNumber.toString()
            val textSize: Float = (if (i / 1.5f == 0f) 5 else i / 1.5f) as Float
            val width: Int
            val hight = dp2px(mContext, i.toFloat()).toInt()
            val bitmap: Bitmap
            if (number.length == 1) {
                width = dp2px(mContext, i.toFloat()).toInt()
                bitmap = Bitmap.createBitmap(width, hight, Bitmap.Config.ARGB_8888)
            } else if (number.length == 2) {
                width = dp2px(mContext, i + 5.toFloat()).toInt()
                bitmap = Bitmap.createBitmap(width, hight, Bitmap.Config.ARGB_8888)
            } else {
                width = dp2px(mContext, i + 8.toFloat()).toInt()
                bitmap = Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_8888)
            }
            val canvasMessages = Canvas(bitmap)
            val messageRectF = RectF(0F, 0F, width.toFloat(), hight.toFloat())
            canvasMessages.drawRoundRect(messageRectF, 50f, 50f, backgroundPaint) //画椭圆
            val numberPaint = Paint()
            numberPaint.color = Color.WHITE
            numberPaint.textSize = dp2px(mContext, textSize)
            numberPaint.isAntiAlias = true
            numberPaint.textAlign = Paint.Align.CENTER
            numberPaint.typeface = Typeface.DEFAULT_BOLD
            val fontMetrics = numberPaint.fontMetrics
            val x = width / 2f
            val y =
                hight / 2f - fontMetrics.descent + (fontMetrics.descent - fontMetrics.ascent) / 2
            canvasMessages.drawText(number, x, y, numberPaint)
            val left = measuredWidth / 10 * 6f
            val top = dp2px(mContext, 5f)
            canvas.drawBitmap(bitmap, left, top, null)
            bitmap.recycle()
        } else if (mBadgeNumber == 0) {
        } else {
            if (isShowPoint) {
                val paint = Paint()
                paint.color = mBadgeBackgroundColor
                paint.isAntiAlias = true
                val left = measuredWidth / 10 * 6f
                val top = dp2px(context, 5f)
                i = if (i > 10) 10 else i
                val width = dp2px(context, i.toFloat())
                val messageRectF = RectF(left, top, left + width, top + width)
                canvas.drawOval(messageRectF, paint)
            }
        }
    }

    fun showPoint() {
        isShowRemove = false
        mBadgeNumber = -1
        isShowPoint = true
        invalidate()
    }

    fun showNumber(badgeNum: Int) {
        isShowRemove = false
        isShowPoint = false
        mBadgeNumber = badgeNum
        if (badgeNum > 0) {
            invalidate()
        } else {
            isShowRemove = true
            invalidate()
        }
    }

    fun removeShow() {
        mBadgeNumber = 0
        isShowPoint = false
        isShowRemove = true
        invalidate()
    }

    fun getBadgeNumber(): Int {
        return mBadgeNumber
    }

    fun isShowPoint(): Boolean {
        return isShowPoint
    }

    private fun availableToDrawRect(
        availableRect: Rect,
        bitmap: Bitmap
    ): Rect {
        var dx = 0f
        var dy = 0f
        val wRatio = availableRect.width() * 1.0f / bitmap.width
        val hRatio = availableRect.height() * 1.0f / bitmap.height
        if (wRatio > hRatio) {
            dx = (availableRect.width() - hRatio * bitmap.width) / 2
        } else {
            dy = (availableRect.height() - wRatio * bitmap.height) / 2
        }
        val left = (availableRect.left + dx + 0.5f).toInt()
        val top = (availableRect.top + dy + 0.5f).toInt()
        val right = (availableRect.right - dx + 0.5f).toInt()
        val bottom = (availableRect.bottom - dy + 0.5f).toInt()
        mIconDrawRect[left, top, right] = bottom
        return mIconDrawRect
    }

    /**
     * @param alpha 对外提供的设置透明度的方法，取值 0.0 ~ 1.0
     */
    fun setIconAlpha(alpha: Float) {
        require(!(alpha < 0 || alpha > 1)) { "" }
        mAlpha = alpha
        invalidateView()
    }

    /**
     * 根据当前所在线程更新界面
     */
    private fun invalidateView() {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            invalidate()
        } else {
            postInvalidate()
        }
    }

    private fun dp2px(
        context: Context?,
        dipValue: Float
    ): Float {
        val scale = context!!.resources.displayMetrics.density
        return (dipValue * scale)
    }
}