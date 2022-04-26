package com.jiayx.navigation.ui.home

import android.animation.ObjectAnimator
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.jiayx.navigation.R
import com.jiayx.navigation.databinding.Fragment01Binding
import com.jiayx.navigation.viewmodel.HomeViewModel

/**
 *Created by yuxi_
on 2022/4/19
 */
class Fragment01 : Fragment() {
    private val viewModel: HomeViewModel by viewModels<HomeViewModel>()
    private val binding: Fragment01Binding by lazy {
        Fragment01Binding.inflate(layoutInflater)
    }
    private var notificationId = 0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imageView.rotation = viewModel.rotationPosition
        val animator: ObjectAnimator = ObjectAnimator.ofFloat(binding.imageView, "rotation", 0f, 0f)
        animator.duration = 500
        binding.imageView.setOnClickListener {
            if (!animator.isRunning) {
                animator.setFloatValues(
                    binding.imageView.rotation,
                    binding.imageView.rotation + 100
                )
                viewModel.rotationPosition += 100f
                animator.start()
            }
        }

        binding.button.setOnClickListener {
            val args = Fragment01Args.Builder().setUserName("张三").setAge(20).build().toBundle()
            it.findNavController().navigate(R.id.action_fragment01_to_fragment02, args)
        }
        binding.button2.setOnClickListener {
            val args = Fragment01Args.Builder().setUserName("李四").setAge(25).build().toBundle()
            it.findNavController().navigate(R.id.action_fragment01_to_fragment03, args)
        }

        binding.button3.setOnClickListener {
            sendNotification()
        }
    }

    private fun sendNotification() {
        // 规避高版本报错问题 通知渠道
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            // id , name , 级别
            //级别，通知重要程度
            //IMPORTANCE_NONE = 0;//关闭通知
            //IMPORTANCE_MIN = 1;// 开启通知，不会弹出，但没有提示音，状态栏中无显示
            //IMPORTANCE_LOW = 2;// 开启通知，不会弹出，不会发出提示音，状态栏中显示
            //IMPORTANCE_DEFAULT = 3;//开启通知，不会弹出，发出声音，状态栏中显示
            //IMPORTANCE_HIGH = 4;//开启通知， 会弹出，发出声音，状态栏中显示
            //IMPORTANCE_MAX = 5;
            val channel = NotificationChannel(
                requireActivity().packageName,
                "myChannel",
                NotificationManager.IMPORTANCE_HIGH
            )
            // 描述
            channel.description = "My NotificationChannel"
            val notificationManager = activity?.getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
        }
        val notification =
            NotificationCompat.Builder(requireActivity(), requireActivity().packageName)
                .setSmallIcon(R.drawable.ic_baseline_emoji_emotions_24)
                .setContentTitle("Deep Link")
                .setContentText("点击我试试")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setColor(Color.GREEN)//设置图标颜色
                .setContentIntent(getPendingIntent())
                .setAutoCancel(true)//点击自动取消
                .setLargeIcon(BitmapFactory.decodeResource(resources,R.mipmap.ic_launcher))
                .build()
        val managerCompat = NotificationManagerCompat.from(requireActivity())
        managerCompat.notify(notificationId++, notification)
    }

    private fun getPendingIntent(): PendingIntent {
        val args = Fragment01Args.Builder().setUserName("秦始皇").setAge(200).build().toBundle()
        return Navigation.findNavController(requireActivity(), R.id.button3)
            .createDeepLink()
            .setGraph(R.navigation.navigation_home)
            .setDestination(R.id.fragment03)
            .setArguments(args)
            .createPendingIntent()
    }
}