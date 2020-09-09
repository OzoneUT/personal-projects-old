package com.example.moneymoney

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.cardview.widget.CardView
import androidx.core.animation.addListener
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.animation.ArgbEvaluatorCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.fragment_init.*
import kotlinx.android.synthetic.main.snippet_sign_in.*
import kotlinx.android.synthetic.main.snippet_sign_in.view.*
import kotlinx.android.synthetic.main.snippet_sign_up.*
import kotlinx.android.synthetic.main.snippet_sign_up.view.*


class InitFragment : Fragment() {

    private val TAG = "InitFragment"

    private var mediumAnimationDuration: Int = 0
    private var shortAnimationDuration: Int = 0
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<CardView>
    private lateinit var loginView: View
    private lateinit var signupView: View

    companion object {
        fun newInstance() = InitFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_init, container, false)
        mediumAnimationDuration = resources.getInteger(android.R.integer.config_mediumAnimTime)
        shortAnimationDuration = resources.getInteger(android.R.integer.config_shortAnimTime)
        loginView = inflater.inflate(R.layout.snippet_sign_in, snippet_layout)
        signupView = inflater.inflate(R.layout.snippet_sign_up, snippet_layout2)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomSheetBehavior = BottomSheetBehavior.from(bottom_modal)
        if (savedInstanceState == null) {
            bottom_modal.addView(loginView)
        }
        loginView.sign_up_button_extra.setOnClickListener {
            Handler().postDelayed({
                bottomSheetBehavior.isHideable = true
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                bottom_modal.removeAllViews()
                bottom_modal.addView(signupView)
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }, 100)
        }
        signupView.sign_in_button_extra.setOnClickListener {
            Handler().postDelayed({
                bottomSheetBehavior.isHideable = true
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                bottom_modal.removeAllViews()
                bottom_modal.addView(loginView)
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }, 100)
        }
        loginView.login_button.setOnClickListener {
            setLoginUIEnabled(false)
            Handler().postDelayed({
                handleLogin()
            }, 100)
        }
    }

    private fun setLoginUIEnabled(value: Boolean) {
        email_login_edittext.isEnabled = value
        password_login_edittext.isEnabled = value
        login_button.isEnabled = value
        forgot_password_button.isEnabled = value
        sign_up_button_extra.isEnabled = value
    }

    private fun convertToPx(dp: Int): Int {
        return (dp * resources.displayMetrics.density).toInt()
    }

    private fun handleLogin() {
        animateButtonToLoading()
        // login logic
        // if error, animateErrorLoadingButton()
        Handler().postDelayed({
            animateButtonToDefault()
            setLoginUIEnabled(true)
        }, 2000)
    }

    private fun animateButtonToDefault() {
        ValueAnimator.ofInt(
            loginView.login_relative_layout.width,
            sign_up_button_extra.width
        ).apply {
            duration = shortAnimationDuration.toLong()
            addUpdateListener { animator ->
                val layoutParams = login_relative_layout.layoutParams;
                layoutParams.width = animator.animatedValue as Int
                login_relative_layout.layoutParams = layoutParams
            }
            doOnStart {
                login_progress.visibility = View.GONE
            }
            doOnEnd {
                loginView.login_button.text = "Login"
                loginView.login_button.setTextColor(
                    ContextCompat.getColor(
                        context!!,
                        R.color.white
                    )
                )
            }
            start()
        }
    }

    private fun animateButtonToLoading() {
        val textColorAnimator = ValueAnimator.ofInt(loginView.login_button.currentTextColor,
            ContextCompat.getColor(context!!, R.color.transparent)).apply {
            duration = 100
            addUpdateListener { animator ->
                loginView.login_button.setTextColor(animator.animatedValue as Int)
            }
            doOnEnd {
                loginView.login_button.text = ""
            }
        }
        val widthAnimator = ValueAnimator.ofInt(loginView.login_relative_layout.width,
            convertToPx(44)).apply {
            duration = shortAnimationDuration.toLong()
            addUpdateListener { animator ->
                val layoutParams = login_relative_layout.layoutParams;
                layoutParams.width = animator.animatedValue as Int
                login_relative_layout.layoutParams = layoutParams
            }
            doOnEnd {
                login_progress.visibility = View.VISIBLE
            }
        }
        AnimatorSet().apply {
            play(textColorAnimator).before(widthAnimator)
            start()
        }
    }

    override fun onStart() {
        super.onStart()
        if (bottom_modal.childCount < 1) {
            bottom_modal.addView(loginView)
        }
        if (bottomSheetBehavior.isHideable) {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }
        bottomSheetBehavior.setBottomSheetCallback(object: BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(p0: View, p1: Float) { }

            @SuppressLint("SwitchIntDef")
            override fun onStateChanged(p0: View, p1: Int) {
                when (p1) {
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        bottomSheetBehavior.isHideable = false
                    }
                }
            }
        })

        animateSmileLogo()
    }

    private fun animateSmileLogo() {
        smile_icon.apply {
            animate()
                .translationY(-500f)
                .scaleX(0.5f)
                .scaleY(0.5f)
                .alpha(0.5f)
                .setDuration(mediumAnimationDuration.toLong())
                .setStartDelay(350)
                .setInterpolator(AccelerateDecelerateInterpolator())
                .setListener(object: AnimatorListenerAdapter() {

                    override fun onAnimationStart(animation: Animator?) {
                        animateText()
                    }
                })
        }
    }

    private fun animateText() {
        money_image.apply {
            animate()
                .translationY(-700f)
                .scaleX(0.7f)
                .scaleY(0.7f)
                .alpha(0.5f)
                .setDuration(mediumAnimationDuration.toLong())
                .setListener(null)
                .setInterpolator(AccelerateDecelerateInterpolator())
                .setListener(object: AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                    }
                })
        }
    }
}
