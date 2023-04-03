package com.simple.book.main

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.animation.LinearInterpolator
import com.book.base.ui.BaseActivity
import com.book.base.utils.DisplayUtil
import com.book.base.utils.HandlerUtil
import com.simple.book.R
import com.simple.book.databinding.ActivitySplashBinding


class SplashActivity : BaseActivity() {

    private lateinit var mBinding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        val screenHeight = DisplayUtil.getScreenHeight(this)

        mBinding.ivLogo.post {
            val logoAnimator: ObjectAnimator =
                ObjectAnimator.ofFloat(
                    mBinding.ivLogo,
                    "translationY",
                    -mBinding.ivLogo.y,
                    0f
                )
            mBinding.tvName.post {
                val nameAnimator: ObjectAnimator =
                    ObjectAnimator.ofFloat(
                        mBinding.tvName,
                        "translationY",
                        screenHeight - mBinding.tvName.y - mBinding.tvName.height,
                        0f
                    )
                AnimatorSet().apply {
                    playTogether(logoAnimator, nameAnimator)
                    duration = 300
                    interpolator = LinearInterpolator()
                    addListener(object : Animator.AnimatorListener {
                        override fun onAnimationStart(p0: Animator?) {

                        }

                        override fun onAnimationEnd(p0: Animator?) {
                            startMainActivity()
                        }

                        override fun onAnimationCancel(p0: Animator?) {

                        }

                        override fun onAnimationRepeat(p0: Animator?) {

                        }
                    })
                    start()
                }
            }
        }
    }

    private fun startMainActivity() {
        HandlerUtil.postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            overridePendingTransition(
                R.anim.activity_in,
                R.anim.activity_out
            )
        }, 500)
    }

    override fun onDestroy() {
        super.onDestroy()
        HandlerUtil.removeAll()
    }
}