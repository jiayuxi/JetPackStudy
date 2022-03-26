package com.jiayx.bluetooth.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import com.jiayx.bluetooth.R
import com.jiayx.bluetooth.base.BaseDialogFragment
import com.jiayx.bluetooth.databinding.LibBluetoothDialogThreeButtonBinding

class ThreeButtonDialog constructor(
    private val title: String? = null,
    private val content: String? = "",
    private val negativeTitle: String? = null,
    private val positiveTitle: String? = null,
    private val neutralTitle: String? = null,
) : BaseDialogFragment() {

    var onButtonClickListener: ((which: WhichButton) -> Unit)? = null

    private val arrayList = arrayListOf<RadioButton>()
    private var mCurrentIndex = 0

    enum class WhichButton {
        NEGATIVE_BUTTON, POSITIVE_BUTTON, NEUTRAL_BUTTON
    }

    private val binding: LibBluetoothDialogThreeButtonBinding by lazy {
        LibBluetoothDialogThreeButtonBinding.inflate(layoutInflater)
    }

    override fun onKeyEventListener(code: Int) {
        when (code) {
            KeyEvent.KEYCODE_DPAD_LEFT -> {
                if (mCurrentIndex > 0) {
                    mCurrentIndex--
                    initChecked(mCurrentIndex)
                }
            }
            KeyEvent.KEYCODE_DPAD_RIGHT -> {
                if (mCurrentIndex < (arrayList.size - 1)) {
                    mCurrentIndex++
                    initChecked(mCurrentIndex)
                }
            }
            KeyEvent.KEYCODE_ENTER -> {
                val radioButton = arrayList[mCurrentIndex]
                when (radioButton.id) {
                    R.id.rbNegative -> onButtonClickListener?.invoke(WhichButton.NEGATIVE_BUTTON)
                    R.id.rbPositive -> onButtonClickListener?.invoke(WhichButton.POSITIVE_BUTTON)
                    R.id.rbNeutral -> onButtonClickListener?.invoke(WhichButton.NEUTRAL_BUTTON)
                }
                dismissAllowingStateLoss()
            }
            KeyEvent.KEYCODE_BACK, KeyEvent.KEYCODE_ESCAPE -> {
                dismissAllowingStateLoss()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //弹出框透明背景样式
        setStyle(STYLE_NO_TITLE, R.style.BottomDialog)
    }

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View = binding.root

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialog?.setCancelable(true)
        dialog?.setCanceledOnTouchOutside(true)
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
        initListener()
    }

    private fun initView() {
        // 初始化标题
        title?.let {
            binding.tvTitle.text = title
        } ?: run {
            binding.tvTitle.visibility = View.GONE
        }
        // 初始化内容
        binding.tvContent.text = content ?:""
        // 初始化按钮1
        negativeTitle?.let {
            binding.rbNegative.text = it
            arrayList.add(binding.rbNegative)
        } ?: kotlin.run {
            binding.rbNegative.visibility = View.GONE
        }
        // 初始化按钮2
        positiveTitle?.let {
            binding.rbPositive.text = it
            arrayList.add(binding.rbPositive)
        } ?: kotlin.run {
            binding.rbPositive.visibility = View.GONE
        }
        // 初始化按钮3
        neutralTitle?.let {
            binding.rbNeutral.text = it
            arrayList.add(binding.rbNeutral)
        } ?: kotlin.run {
            binding.rbNeutral.visibility = View.GONE
        }
        initChecked(mCurrentIndex)
    }

    private fun initListener() {
        binding.rbNegative.setOnClickListener {
            onButtonClickListener?.invoke(WhichButton.NEGATIVE_BUTTON)
            dismissAllowingStateLoss()
        }
        binding.rbPositive.setOnClickListener {
            onButtonClickListener?.invoke(WhichButton.POSITIVE_BUTTON)
            dismissAllowingStateLoss()
        }
        binding.rbNeutral.setOnClickListener {
            onButtonClickListener?.invoke(WhichButton.NEUTRAL_BUTTON)
            dismissAllowingStateLoss()
        }

    }

    private fun initChecked(value: Int) {
        for (i in arrayList.indices) {
            val radioButton = arrayList[i]
            radioButton.isChecked = (i == value)
        }
        mCurrentIndex = value
    }
}