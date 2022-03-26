package onlyTest

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.jiayx.wlan.databinding.LibWifiActivityOnlytestSoftinputLayoutBinding
import com.jiayx.wlan.recyclerview.SimplePaddingDecoration
import com.jiayx.wlan.viewModel.SoftInputViewModel
import com.jiayx.wlan.adapter.SoftInputAdapter

class SoftInputActivity : AppCompatActivity() {

    private val binding: LibWifiActivityOnlytestSoftinputLayoutBinding by lazy {
        LibWifiActivityOnlytestSoftinputLayoutBinding.inflate(layoutInflater)
    }
    private val paddingDecoration by lazy {
        SimplePaddingDecoration(5, 0, 5, 0)
    }
    private val viewModel: SoftInputViewModel by lazy {
        ViewModelProvider(
            this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.application)
        ).get(SoftInputViewModel::class.java)
    }
    private val roomListAdapter: SoftInputAdapter by lazy {
        SoftInputAdapter(this, viewModel.characterItems)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        binding.activitySoftInputRecycler.layoutManager =
            GridLayoutManager(this, 10)
        (binding.activitySoftInputRecycler.layoutManager as GridLayoutManager).spanSizeLookup =
            object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return viewModel.characterItems[position]?.columnsSpan ?: 1
                }
            }
        binding.activitySoftInputRecycler.addItemDecoration(paddingDecoration)
        binding.activitySoftInputRecycler.adapter = roomListAdapter
        roomListAdapter.setData(viewModel.characterItems)
        viewModel.changeCharacter.observe(this) {
            roomListAdapter.selection = 0
            roomListAdapter.setData(viewModel.characterItems)
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (event?.action == KeyEvent.ACTION_UP && event.repeatCount == 0) {
            when (keyCode) {
                KeyEvent.KEYCODE_DPAD_UP -> {
                }

                KeyEvent.KEYCODE_DPAD_LEFT
                -> {
                    if (roomListAdapter.selection <= 0) return true
                    roomListAdapter.selection -= 1
                }
                KeyEvent.KEYCODE_DPAD_RIGHT -> {
                    if (roomListAdapter.selection >= viewModel.characterItems.size - 1) return true
                    roomListAdapter.selection += 1
                }

                KeyEvent.KEYCODE_ENTER
                -> {
                    viewModel.enterKey(roomListAdapter, binding.activitySoftInputEditText)
//                    viewModel.characterItems[roomListAdapter.selection]?.let { it ->
//                        val editable = binding.activitySoftInputEditText.text.toString()
//                        when (it.keyType) {
//                            SPACE_KEY -> {
//                                binding.activitySoftInputEditText.text =
//                                    Editable.Factory.getInstance().newEditable("$editable ")
//                                binding.activitySoftInputEditText.setSelection(binding.activitySoftInputEditText.text.length)
//                            }
//                            DEL_KEY -> {
//                                // 回退键,删除字符
//                                val substring = editable.substring(
//                                    0,
//                                    if ((editable.length - 1) < 0) 0 else editable.length - 1
//                                )
//                                binding.activitySoftInputEditText.text =
//                                    Editable.Factory.getInstance().newEditable(substring)
//                                binding.activitySoftInputEditText.setSelection(binding.activitySoftInputEditText.text.length)
//                            }
//                            SHIFT_KEY -> {
//                                roomListAdapter.ifShift = !roomListAdapter.ifShift
//                                roomListAdapter.notifyDataSetChanged()
//                            }
//                            OK_KEY -> {
//                                Log.d("soft_input_log", "onKeyDown: ok : ${editable.toString()}")
//                            }
//                            CANCEL_KEY -> {
//                                Log.d("soft_input_log", "onKeyDown: 取消 ")
//                            }
//                            SWITCH_CHARACTER_KEY, SWITCH_SPECIAL_KEY -> {
//                                switchSoftInput(it.keyType)
//                            }
//                            else -> {
//                                binding.activitySoftInputEditText.text =
//                                    Editable.Factory.getInstance()
//                                        .newEditable(editable.toString() + if (roomListAdapter.ifShift) it.shiftKeyword else it.keyword)
//                                binding.activitySoftInputEditText.setSelection(binding.activitySoftInputEditText.text.length)
//                            }
//                        }
//                    }
                }
                KeyEvent.KEYCODE_F1
                -> {
                }
                KeyEvent.KEYCODE_BACK, KeyEvent.KEYCODE_ESCAPE -> {
                    finish()
                    return true
                }
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun dispatchKeyEvent(event: KeyEvent?): Boolean {
        event?.let { onKeyDown(event.keyCode, event) }
        return false
    }
}