package com.jiayx.wlan.viewModel

import android.text.Editable
import android.widget.EditText
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jiayx.wlan.softInput.*
import com.jiayx.wlan.adapter.SoftInputAdapter

class SoftInputViewModel : ViewModel() {
    private val items1 by lazy {
        val temp = arrayListOf<SoftInputAdapter.Companion.Item?>()
        CharactersEnum.values().forEach {
            temp.add(
                SoftInputAdapter.Companion.Item(
                    it.keyword,
                    it.shiftKeyword,
                    it.columnsSpan,
                    it.res,
                    it.selectRes,
                    it.keyType
                )
            )
        }
        return@lazy temp
    }
    private val items2 by lazy {
        val temp = arrayListOf<SoftInputAdapter.Companion.Item?>()
        SpecialCharactersEnum.values().forEach {
            temp.add(
                SoftInputAdapter.Companion.Item(
                    it.keyword,
                    it.shiftKeyword,
                    it.columnsSpan,
                    it.res,
                    it.selectRes,
                    it.keyType
                )
            )
        }
        return@lazy temp
    }
    val characterItems by lazy {
        arrayListOf<SoftInputAdapter.Companion.Item?>()
    }
    var changeCharacter = MutableLiveData<Boolean>()
    var okLiveData = MutableLiveData<String>()
    var cancelLivedata = MutableLiveData<Boolean>()


    init {
        characterItems.addAll(items1)
    }

    private fun switchSoftInput(type: Int) {
        characterItems.clear()
        if (type == SWITCH_CHARACTER_KEY) {
            characterItems.addAll(items2)
        } else {
            characterItems.addAll(items1)
        }
        changeCharacter.value = true
    }

    fun enterKey(adapter: SoftInputAdapter, editText: EditText) {
        characterItems[adapter.selection]?.let { it ->
            val editable = editText.text.toString()
            when (it.keyType) {
                SPACE_KEY -> {
                    editText.text =
                        Editable.Factory.getInstance().newEditable("$editable ")
                    editText.setSelection(editText.text.length)
                }
                DEL_KEY -> {
                    // 回退键,删除字符
                    val substring = editable.substring(
                        0,
                        if ((editable.length - 1) < 0) 0 else editable.length - 1
                    )
                    editText.text =
                        Editable.Factory.getInstance().newEditable(substring)
                    editText.setSelection(editText.text.length)
                }
                SHIFT_KEY -> {
                    adapter.ifShift = !adapter.ifShift
                    adapter.notifyDataSetChanged()
                }
                OK_KEY -> {
                    if (editText.toString().isNullOrEmpty() || editText.toString().length < 8) {
                        return@let
                    }
                    okLiveData.value = editable.toString()
                }
                CANCEL_KEY -> {
                    cancelLivedata.value = true
                }
                SWITCH_CHARACTER_KEY, SWITCH_SPECIAL_KEY -> {
                    switchSoftInput(it.keyType)
                }
                else -> {
                    editText.text =
                        Editable.Factory.getInstance()
                            .newEditable(editable.toString() + if (adapter.ifShift) it.shiftKeyword else it.keyword)
                    editText.setSelection(editText.text.length)
                }
            }
        }
    }
}