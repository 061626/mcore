package com.wanger.mcore

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.ViewFlipper

/**

 * @author wanger
 * @date 2019/4/17 9:47
 * @email xxx@gmail.com
 * @desc doc
 */
public abstract class AppBaseActivity : MBaseActivity() {

    protected var mHeadLayout: FrameLayout? = null
    protected var mContentView: ViewFlipper? = null
    protected var mTitle: TextView? = null
    protected var mBtnLeft: Button? = null
    protected var mBtnRight: Button? = null
    protected var mHeadRightText: TextView? = null
    private var mBtnBackDrawable: Drawable? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.setContentView(R.layout.activity_base)
        mHeadLayout = super.findViewById(R.id.layout_head)
        mTitle = findViewById(R.id.tv_title)
        mHeadRightText = findViewById(R.id.text_right)
        mBtnLeft = super.findViewById(R.id.btn_left)
        mBtnRight = super.findViewById(R.id.btn_right)
        mContentView = findViewById(R.id.layout_container)
        mBtnBackDrawable = resources.getDrawable(R.drawable.ic_arrow_left_black_24dp)
        mBtnBackDrawable?.setBounds(0, 0, mBtnBackDrawable!!.minimumWidth,
                mBtnBackDrawable!!.minimumHeight)
    }

    override fun setContentView(view: View?) {
        mContentView?.addView(view)
    }

    override fun setContentView(layoutResID: Int) {
        val view = LayoutInflater.from(this).inflate(layoutResID, null)
        mContentView?.addView(view)
    }

    /**设置标题是否可见*/
    public fun setHeadTitleVisibility(visibility: Int) {
        mTitle?.visibility = visibility
    }

    /**
     * 设置标题
     */
    fun setTitle(title: String) {
        setTitle(title, false)
    }

    /**
     * 设置标题
     *
     * @param title
     */
    fun setTitle(title: String, flag: Boolean) {
        mTitle?.text = title
        if (flag) {
            mBtnLeft?.setCompoundDrawables(null, null, null, null)
        } else {
            mBtnLeft?.setCompoundDrawables(mBtnBackDrawable, null, null, null)
        }
    }
    /**
     * 设置头部是否可见
     *
     * @param visibility
     */
    fun setHeadVisibility(visibility: Int) {
        mHeadLayout?.visibility = visibility
    }

    /**
     * 设置左边是否可见
     *
     * @param visibility
     */
    fun setHeadLeftButtonVisibility(visibility: Int) {
        mBtnLeft?.visibility = visibility
    }

    /**
     * 设置右边是否可见
     *
     * @param visibility
     */
    fun setHeadRightButtonVisibility(visibility: Int) {
        mBtnRight?.visibility = visibility
    }

    /**
     * 点击左按钮
     */
    public open fun onHeadLeftButtonClick(v: View) {
        onCloseSelf()
    }

    /**
     * 点击右按钮
     */
    public open fun onHeadRightButtonClick(v: View) {

    }

}