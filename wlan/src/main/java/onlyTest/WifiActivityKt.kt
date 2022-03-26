package onlyTest

import android.net.wifi.WifiManager
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.jiayx.wlan.databinding.LibWifiActivityOnlytestWifiBinding
import com.jiayx.wlan.dialog.ConnectWifiDialog
import com.jiayx.wlan.recyclerview.SimplePaddingDecoration
import com.jiayx.wlan.adapter.WifiListAdapter
import com.jiayx.wlan.viewModel.WifiViewModel
import com.jiayx.wlan.wifi.SecurityModeEnum

class WifiActivityKt : AppCompatActivity(), OnRefreshListener {
    private val scanResultItems by lazy { arrayListOf<WifiListAdapter.Companion.WifiItem>() }
    private val wifiViewModel: WifiViewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(this.application)
        ).get(WifiViewModel::class.java)
    }
    private val binding: LibWifiActivityOnlytestWifiBinding by lazy {
        LibWifiActivityOnlytestWifiBinding.inflate(layoutInflater)
    }
    private val adapter: WifiListAdapter by lazy {
        WifiListAdapter(this, scanResultItems, 2, 3)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initReceiver()
        initView()
        initAction()
    }

    private fun initReceiver() {
        wifiViewModel.initReceiver(this)
    }

    override fun onResume() {
        super.onResume()
        // 添加监听
        wifiViewModel.addInterfaceListener()
        // 更新WIFI开关状态
        binding.switchWifi.isChecked = wifiViewModel.isWifiEnabled()
    }

    override fun onPause() {
        super.onPause()
        // 移除监听
        wifiViewModel.removeInterfaceListener()
    }

    private fun initView() {
        adapter.selection = 0
        if (binding.wifiRecycler.itemDecorationCount > 0)
            binding.wifiRecycler.removeItemDecorationAt(binding.wifiRecycler.itemDecorationCount - 1)
        binding.wifiRecycler.adapter = adapter
        binding.wifiRecycler.layoutManager = GridLayoutManager(this, adapter.columnCount)
        binding.wifiRecycler.addItemDecoration(SimplePaddingDecoration(0, 30, 15, 15))
        adapter.setData(scanResultItems)

//        binding.tvPages.visibility = if (adapter.pageCount > 0) View.VISIBLE else View.GONE
//        adapter.onPageChangeListener = { binding.tvPages.text = "${it + 1} / ${adapter.pageCount}" }

    }

    private fun initAction() {

        // 添加下拉刷新的监听
        binding.swipeRefreshLayout.setOnRefreshListener(this)
        // WIFI管理器
        binding.switchWifi.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                wifiViewModel.enabledWifi()
            } else {
                wifiViewModel.closeWifi()
            }
        })
        binding.wifiConnectName.text = wifiViewModel.getWifiConnectSSID()
        wifiViewModel.getScanResults()
        wifiViewModel.scanResultsListener = { scanResults ->
            runOnUiThread {
                Log.d("scan_result_log", "当前WiFi 个数 ： ${scanResults?.size}")
                scanResultItems.clear()
                scanResults?.let {
                    scanResultItems.addAll(it)
                }
                refreshData(scanResultItems)
            }
        }
        wifiViewModel.wifiEnabledListener = { state ->
            when (state) {
                //wifi 正在关闭
                WifiManager.WIFI_STATE_DISABLING -> {

                }
                // wifi 已经关闭
                WifiManager.WIFI_STATE_DISABLED -> {
                    updateWlanState(false)
                }
                // wifi 正在打开
                WifiManager.WIFI_STATE_ENABLING -> {

                }
                // wifi 已经打开
                WifiManager.WIFI_STATE_ENABLED -> {
                    updateWlanState(true)
                }
                // wifi 位置状态
                WifiManager.WIFI_STATE_UNKNOWN -> {
                    updateWlanState(false)
                }
            }
        }
        wifiViewModel.wifiConnectStateListener = { s ->
            binding.wifiConnectName.text = "$s"
            updateConnectState(s)
        }
        wifiViewModel.wifiConnectFailureListener = { s -> // 连接失败的回调
            Toast.makeText(this, "$s", Toast.LENGTH_SHORT).show()
            binding.wifiConnectName.text = "$s"
            updateConnectState(s)
        }
        wifiViewModel.wifiConnectSuccessListener = { s ->
            binding.wifiConnectName.text = s
            Toast.makeText(applicationContext, "$s  连接成功", Toast.LENGTH_SHORT).show()
            startScanWifi()
        }
        wifiViewModel.wifiDisconnectedListener = {
            Toast.makeText(this, "断开连接", Toast.LENGTH_SHORT).show()
            binding.wifiConnectName.text = ""
        }
        adapter.listener = {
            adapter.selection = it

            val scanResult = adapter.items[adapter.pageNum * 6 + it]
            wifiViewModel.currentSSID = scanResult.ssid
            if (wifiViewModel.isCheckSSIDConnect(scanResult.ssid)) {
                // 弹出 ， 断开连接 ， 删除网络的选项
                wifiViewModel.disconnectWifi()
            } else {

                when (wifiViewModel.securityMode(scanResult.capabilities)) {
                    SecurityModeEnum.WEP, SecurityModeEnum.WPA, SecurityModeEnum.WPA2 ->
                        connectWifi(
                            wifiViewModel.securityMode(scanResult.capabilities),
                            scanResult.ssid
                        )
                    SecurityModeEnum.OPEN -> {
                        updateWifiState("正在连接...")
                        wifiViewModel.connectOpenNetwork(scanResult.ssid)
                    }
                }
            }
        }
    }

    private fun connectWifi(modeEnum: SecurityModeEnum, ssid: String) {
        val wifiConfiguration = wifiViewModel.isWifiConfigExits(ssid)
        if (wifiConfiguration != null) {
            updateWifiState("正在连接...")
            wifiViewModel.connectNetwork(wifiConfiguration)
        } else {
            if (modeEnum == SecurityModeEnum.WEP) {
                object : ConnectWifiDialog(this) {
                    override fun connect(password: String) {
                        updateWifiState("正在连接...")
                        wifiViewModel.connectWEPNetwork(ssid, password)
                    }
                }.setSsid(ssid).show()
            } else if (modeEnum == SecurityModeEnum.WPA || modeEnum == SecurityModeEnum.WPA2) {
                object : ConnectWifiDialog(this) {
                    override fun connect(password: String) {
                        updateWifiState("正在连接...")
                        wifiViewModel.connectWPA2Network(ssid, password)
                    }
                }.setSsid(ssid).show()
            }
        }
    }

    /**
     * 刷新页面
     *
     * @param scanResults WIFI数据
     */
    private fun refreshData(scanResults: List<WifiListAdapter.Companion.WifiItem>?) {
        binding.swipeRefreshLayout.isRefreshing = false
        // 刷新界面
        scanResults?.let {
            adapter.setData(it as ArrayList<WifiListAdapter.Companion.WifiItem>)
            adapter.selection = adapter.updatePage(adapter.selection)
        }
        try {
            binding.tvPages.visibility = if (adapter.pageCount > 0) View.VISIBLE else View.GONE
            adapter.onPageChangeListener =
                { binding.tvPages.text = "${it + 1} / ${adapter.pageCount}" }
        } catch (e: Exception) {
            Log.e("pages_log", "refreshData: ${e.toString()}")
        }
    }

    override fun onRefresh() {
        // 下拉刷新
        wifiViewModel.startScanWifi()
    }

    override fun onDestroy() {
        super.onDestroy()
        wifiViewModel.unregisterReceiver(this)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (event?.action == KeyEvent.ACTION_UP && event.repeatCount == 0) {
            when (keyCode) {
                KeyEvent.KEYCODE_DPAD_UP -> {
                    Log.d("pages_log", "onKeyDown: dpad up")
                    adapter.onFrontPage()
                }
                KeyEvent.KEYCODE_DPAD_DOWN -> {
                    Log.d("pages_log", "onKeyDown: dpad down ")
                    adapter.onNextPage()
                }
                KeyEvent.KEYCODE_DPAD_LEFT
                -> {
                    adapter.selection -= 1
                }
                KeyEvent.KEYCODE_DPAD_RIGHT -> {
                    adapter.selection += 1
                }

                KeyEvent.KEYCODE_ENTER
                -> {
                    adapter.listener?.invoke(adapter.selection)
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

    private fun startScanWifi() {
//        adapter.selection = 0
        wifiViewModel.startScanWifi()
    }

    private fun updateConnectState(str: String?) {
        var currentIndex: Int = 0
        scanResultItems.forEachIndexed { index, wifiItem ->
            if (wifiViewModel.currentSSID == wifiItem.ssid) {
                wifiItem.networkState = str
                currentIndex = index
            }
        }
        adapter.notifyItemChanged(currentIndex, "wlan")
    }

    private fun updateWifiState(str: String?) {
//        adapter.selection = 0
        wifiViewModel.updateWlanState(scanResultItems, str)
//        scanResultItems.forEachIndexed { index, wifiItem ->
//            if (wifiViewModel.currentSSID == wifiItem.ssid) {
//                wifiItem.networkState = str
//                wifiItem.number = 2
//                wifiItem.isSave = true
//            } else {
//                if (wifiItem.isSave) {
//                    wifiItem.networkState = "已保存"
//                    wifiItem.number = 1
//                }
//            }
//        }
//        val c1: Comparator<WifiListAdapter.Companion.WifiItem> = Comparator { o1, o2 ->
//            if (o2.number == o1.number) {
//                o2.level - o1.level
//            } else {
//                o2.number - o1.number
//            }
//        }
//        scanResultItems.sortWith(c1)
//        adapter.selection = 0
//        adapter.setData(scanResultItems)
//        adapter.notifyDataSetChanged()
    }

    private fun updateWlanState(flag: Boolean) {
        binding.switchWifi.isChecked = flag
        binding.swipeRefreshLayout.visibility = if (flag) View.VISIBLE else View.GONE
    }
}