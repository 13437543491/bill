package com.book.base.widget.tabs

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.viewpager.widget.ViewPager
import java.util.*

class AlphaTabsIndicator(context: Context, attrs: AttributeSet?) : LinearLayout(context, attrs) {

    constructor(context: Context) : this(context, null)


    private var mViewPager: ViewPager? = null
    private var mListner: OnTabChangedListener? = null
    private var mTabViews: MutableList<AlphaTabView>? = null
    private var ISINIT = false

    /**
     * 子View的数量
     */
    private var mChildCounts = 0

    /**
     * 当前的条目索引
     */
    private var mCurrentItem = 0

    fun setViewPager(mViewPager: ViewPager?) {
        this.mViewPager = mViewPager
        init()
    }

    fun setOnTabChangedListener(listner: OnTabChangedListener?) {
        mListner = listner
        isInit()
    }

    fun getCurrentItemView(): AlphaTabView? {
        isInit()
        return mTabViews!![mCurrentItem]
    }

    fun getTabView(tabIndex: Int): AlphaTabView? {
        isInit()
        return mTabViews!![tabIndex]
    }

    fun removeAllBadge() {
        isInit()
        for (alphaTabView in mTabViews!!) {
            alphaTabView.removeShow()
        }
    }

    fun setTabCurrenItem(tabIndex: Int) {
        if (tabIndex < mChildCounts && tabIndex > -1) {
            mTabViews!![tabIndex].performClick()
        } else {
            throw IllegalArgumentException("IndexOutOfBoundsException")
        }
    }

    private fun isInit() {
        if (!ISINIT) {
            init()
        }
    }

    private fun init() {
        ISINIT = true
        mTabViews = ArrayList<AlphaTabView>()
        mChildCounts = childCount
        if (null != mViewPager) {
            if (null == mViewPager!!.adapter) {
                throw NullPointerException()
            }
            require(mViewPager!!.adapter!!.count == mChildCounts) { "" }
            //对ViewPager添加监听
            mViewPager!!.addOnPageChangeListener(MyOnPageChangeListener())
        }
        for (i in 0 until mChildCounts) {
            if (getChildAt(i) is AlphaTabView) {
                val tabView = getChildAt(i) as AlphaTabView
                mTabViews!!.add(tabView)
                //设置点击监听
                tabView.setOnClickListener(MyOnClickListener(i))
            } else {
                throw IllegalArgumentException("")
            }
        }
        mTabViews!!.get(mCurrentItem).setIconAlpha(1.0f)
    }

    private inner class MyOnPageChangeListener : ViewPager.SimpleOnPageChangeListener() {
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
            //滑动时的透明度动画
            if (positionOffset > 0) {
                mTabViews!!.get(position).setIconAlpha(1 - positionOffset)
                mTabViews!!.get(position + 1).setIconAlpha(positionOffset)
            }
            //滑动时保存当前按钮索引
            mCurrentItem = position
        }

        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            resetState()
            mTabViews!!.get(position).setIconAlpha(1.0f)
            mCurrentItem = position
        }
    }

    private inner class MyOnClickListener(private val currentIndex: Int) : OnClickListener {
        override fun onClick(v: View) {
            //点击前先重置所有按钮的状态
            resetState()
            mTabViews!!.get(currentIndex).setIconAlpha(1.0f)
            if (null != mListner) {
                mListner!!.onTabSelected(currentIndex)
            }
            if (null != mViewPager) {
                //不能使用平滑滚动，否者颜色改变会乱
                mViewPager!!.setCurrentItem(currentIndex, false)
            }
            //点击是保存当前按钮索引
            mCurrentItem = currentIndex
        }

    }

    /**
     * 重置所有按钮的状态
     */
    private fun resetState() {
        for (i in 0 until mChildCounts) {
            mTabViews!![i].setIconAlpha(0F)
        }
    }

    private val STATE_INSTANCE = "instance_state"
    private val STATE_ITEM = "state_item"

    /**
     * @return 当View被销毁的时候，保存数据
     */
    override fun onSaveInstanceState(): Parcelable? {
        val bundle = Bundle()
        bundle.putParcelable(STATE_INSTANCE, super.onSaveInstanceState())
        bundle.putInt(STATE_ITEM, mCurrentItem)
        return bundle
    }

    /**
     * @param state 用于恢复数据使用
     */
    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state is Bundle) {
            val bundle = state
            mCurrentItem = bundle.getInt(STATE_ITEM)
            if (null == mTabViews || mTabViews!!.isEmpty()) {
                super.onRestoreInstanceState(bundle.getParcelable(STATE_INSTANCE))
                return
            }
            //重置所有按钮状态
            resetState()
            //恢复点击的条目颜色
            mTabViews!![mCurrentItem].setIconAlpha(1.0f)
            super.onRestoreInstanceState(bundle.getParcelable(STATE_INSTANCE))
        } else {
            super.onRestoreInstanceState(state)
        }
    }
}