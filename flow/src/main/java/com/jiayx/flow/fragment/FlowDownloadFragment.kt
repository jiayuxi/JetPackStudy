package com.jiayx.flow.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.jiayx.flow.bean.DownloadStatus
import com.jiayx.flow.databinding.FragmentFlowDownloadBinding
import com.jiayx.flow.download.DownloadManager
import java.io.File

/**
 *Created by yuxi_
on 2022/4/1

 flow download 下载
 */
@SuppressLint("SetTextI18n")
class FlowDownloadFragment : Fragment() {
    val URL = "https://img0.baidu.com/it/u=2090081131,3262806058&fm=253&fmt=auto&app=120&f=JPEG?w=889&h=500"

    private val binding: FragmentFlowDownloadBinding by lazy {
        FragmentFlowDownloadBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenCreated {
            context?.apply {
                val file = File(getExternalFilesDir(null)?.path, "pic.JPG")
                DownloadManager.download(URL, file).collect { status ->
                    when (status) {
                        is DownloadStatus.Progress -> {
                            binding.apply {
                                flowDownloadSeekBar.progress = status.value
                                flowDownloadText.text = "${status.value}%"
                            }
                        }
                        is DownloadStatus.Error -> {
                            Toast.makeText(context, "下载错误", Toast.LENGTH_SHORT).show()
                        }
                        is DownloadStatus.Done -> {
                            binding.apply {
                                flowDownloadSeekBar.progress = 100
                                flowDownloadText.text = "100%"
                            }
                            Toast.makeText(context, "下载完成", Toast.LENGTH_SHORT).show()
                        }
                        else -> {
                            Log.d("ning", "下载失败.")
                        }
                    }
                }
            }
        }
    }
}