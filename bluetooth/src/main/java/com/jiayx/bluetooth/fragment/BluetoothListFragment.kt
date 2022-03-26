package com.jiayx.bluetooth.fragment

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.jiayx.bluetooth.R
import com.jiayx.bluetooth.adapter.BluetoothListAdapter
import com.jiayx.bluetooth.base.BaseFragment
import com.jiayx.bluetooth.const.State
import com.jiayx.bluetooth.databinding.BluetoothListBinding
import com.jiayx.bluetooth.dialog.ThreeButtonDialog
import com.jiayx.bluetooth.interfaces.IBTBondListener
import com.jiayx.bluetooth.interfaces.IBTConnectListener
import com.jiayx.bluetooth.interfaces.IBTScanListener
import com.jiayx.bluetooth.interfaces.IBTStateListener
import com.jiayx.bluetooth.recyclerview.SimplePaddingDecoration
import com.juxing.helmet.lib_bluetooth.control.BluetoothHelper



@SuppressLint("MissingPermission")
class BluetoothListFragment : BaseFragment(R.layout.bluetooth_list){

    private val mBluetoothHelper by lazy { BluetoothHelper() }

    //
    private val items = arrayListOf<BluetoothListAdapter.Companion.Item?>()
    private val adapter: BluetoothListAdapter by lazy { BluetoothListAdapter(items, 2, 3) }
    private val paddingDecoration by lazy { SimplePaddingDecoration(0, 30, 15, 15) }

    private lateinit var binding: BluetoothListBinding
    private var dialog: ThreeButtonDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = BluetoothListBinding
        .inflate(inflater, container, false)
        .also { binding = it }.root

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
        initListener(init = true)
        initPager()
        mBluetoothHelper.init(requireContext())
        initBondedDevices()
        mBluetoothHelper.getConnectedDevices()
    }

    override fun onResume() {
        super.onResume()
        mBluetoothHelper.startDiscovery()
        binding.rlvBluetoothList.addItemDecoration(paddingDecoration)
    }

    override fun onStop() {
        super.onStop()
        mBluetoothHelper.stopDiscovery()
        binding.rlvBluetoothList.removeItemDecoration(paddingDecoration)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        dialog?.dismissAllowingStateLoss()
    }

    override fun onDestroy() {
        super.onDestroy()
        initListener(init = false)
        mBluetoothHelper.destroy()
    }

    private fun initView() {
        binding.rlvBluetoothList.adapter = adapter
        binding.rlvBluetoothList.layoutManager = GridLayoutManager(context, adapter.columnCount)
        adapter.listener = { index ->
            val item = adapter.getData()[index]
            Log.d(
                TAG,
                "onVoiceTrigger: name = ${item?.device?.name}, address = ${item?.device?.address}, selection = ${adapter.selection}, index = $index"
            )
            val bluetoothDevice: BluetoothDevice? = item?.device
            val content: String? =
                if (TextUtils.isEmpty(bluetoothDevice?.name)) bluetoothDevice?.address else bluetoothDevice?.name
            when (item?.state) {
                State.STATE_UNCONNECT, State.STATE_BOND_NONE -> // 取消，配对
                    dialog = ThreeButtonDialog("未配对", content, "取消", "配对")
                        .also {
                            it.onButtonClickListener = { WhichButton ->
                                when (WhichButton) {
                                    ThreeButtonDialog.WhichButton.POSITIVE_BUTTON -> {
                                        // 配对
                                        mBluetoothHelper.createBond(bluetoothDevice)
                                    }
                                }
                            }
                        }.also {
                            it.show(childFragmentManager, null)
                        }

                State.STATE_BONDING -> {
                }
                State.STATE_BONDED -> {
                    dialog = ThreeButtonDialog("已配对", content, "取消", "连接", "取消配对")
                        .also {
                            it.onButtonClickListener = { WhichButton ->
                                when (WhichButton) {
                                    ThreeButtonDialog.WhichButton.NEGATIVE_BUTTON -> {}
                                    ThreeButtonDialog.WhichButton.POSITIVE_BUTTON -> {
                                        // 连接
                                        mBluetoothHelper.connect(bluetoothDevice)
                                    }
                                    ThreeButtonDialog.WhichButton.NEUTRAL_BUTTON -> {
                                        // 取消配对
                                        mBluetoothHelper.removeBond(bluetoothDevice)
                                    }
                                }
                            }
                        }.also {
                            it.show(childFragmentManager, null)
                        }
                }
                State.STATE_CONNECTING -> {
                }
                State.STATE_CONNECTED ->
                    dialog = ThreeButtonDialog("已连接", content, "取消", "断开连接")
                        .also {
                            it.onButtonClickListener = { WhichButton ->
                                when (WhichButton) {
                                    ThreeButtonDialog.WhichButton.POSITIVE_BUTTON -> {
                                        // 断开连接
                                        mBluetoothHelper.disconnect(bluetoothDevice)
                                    }
                                }
                            }
                        }.also {
                            it.show(childFragmentManager, null)
                        }
                State.STATE_DISCONNECTING -> {
                }
                State.STATE_DISCONNECTED ->
                    dialog = ThreeButtonDialog("已保存", content, "取消", "删除", "连接")
                        .also {
                            it.onButtonClickListener = { WhichButton ->
                                when (WhichButton) {
                                    ThreeButtonDialog.WhichButton.NEGATIVE_BUTTON -> {}
                                    ThreeButtonDialog.WhichButton.POSITIVE_BUTTON -> {
                                        // 删除
                                        mBluetoothHelper.removeBond(bluetoothDevice)
                                    }
                                    ThreeButtonDialog.WhichButton.NEUTRAL_BUTTON -> {
                                        // 连接
                                        mBluetoothHelper.connect(bluetoothDevice)
                                    }
                                }
                            }
                        }.also {
                            it.show(childFragmentManager, null)
                        }
            }
        }
    }

    private fun initListener(init: Boolean) {
        //设置打开关闭状态监听
        mBluetoothHelper.setBTStateListener(if (init) mBTStateListener else null)
        //设置扫描监听
        mBluetoothHelper.setBTScanListener(if (init) mBTScanListener else null)
        //设置配对监听
        mBluetoothHelper.setBTBondListener(if (init) mBTBondListener else null)
        //设置连接监听
        mBluetoothHelper.setBTConnectListener(if (init) mBTConnectListener else null)
    }

    @SuppressLint("SetTextI18n")
    private fun initPager() {
        binding.wlanTvPages.visibility = if (adapter.pageCount > 0) View.VISIBLE else View.GONE
        adapter.onPageChangeListener =
            { binding.wlanTvPages.text = "${it + 1} / ${adapter.pageCount}" }
    }

    /**
     * 获取已配对设备
     */
    private fun initBondedDevices() { //以配对设备
        val bluetoothDeviceSet: Set<BluetoothDevice>? = mBluetoothHelper.getBondedDevices()
        bluetoothDeviceSet?.let {
            for (device in bluetoothDeviceSet) {
                addBondDevice(State.STATE_BONDED, device)
            }
        }
    }

    /**
     * 向已配对列表中添加设备
     *
     * @param state
     * @param device
     */
    private fun addBondDevice(state: Int, device: BluetoothDevice) {
        val item: BluetoothListAdapter.Companion.Item? = findItemByList(adapter.getData(), device)
        if (item != null) {
            item.device = device
        } else {
            val bluetoothItem: BluetoothListAdapter.Companion.Item = createBluetoothItem(device)
            bluetoothItem.state = state
            adapter.add(0, bluetoothItem)
        }
        adapter.notifyDataSetChanged()
        initPager()
    }

    /**
     * 从集合 datas 中找 dev 对应的项
     *
     * @param items
     * @param device
     */
    private fun findItemByList(items: ArrayList<BluetoothListAdapter.Companion.Item?>, device: BluetoothDevice?): BluetoothListAdapter.Companion.Item? {
        if (items.isEmpty()) return null
        for (item in items) {
            if (!TextUtils.isEmpty(device?.address) && device?.address == item?.device?.address) return item
        }
        return null
    }

    private fun createBluetoothItem(device: BluetoothDevice?): BluetoothListAdapter.Companion.Item {
        return BluetoothListAdapter.Companion.Item(device, State.STATE_UNCONNECT)
    }

    /**
     * 向可用列表中添加设备
     */
    private fun addDevUse(device: BluetoothDevice?) {
        val btUseItem: BluetoothListAdapter.Companion.Item? = findItemByList(adapter.getData(), device)
        if (btUseItem != null) {
            btUseItem.device = device?:null
        } else {
            val bluetoothItem: BluetoothListAdapter.Companion.Item = createBluetoothItem(device)
            if (device?.bondState == BluetoothDevice.BOND_BONDED) {
                bluetoothItem?.state = State.STATE_BONDED
            } else if (device?.bondState == BluetoothDevice.BOND_BONDING) {
                bluetoothItem.state = State.STATE_BONDING
            }
            adapter.addData(bluetoothItem)
        }
        adapter.notifyDataSetChanged()
        initPager()
    }

    /**
     * 可用设备列表发生改变
     */
    private fun paireDevStateChange(state: Int, device: BluetoothDevice?) {
        val item: BluetoothListAdapter.Companion.Item? = findItemByList(adapter.getData(), device)
        val bondItem: BluetoothListAdapter.Companion.Item? = findItemByList(adapter.getData(), device)
        when {
            item != null -> {
                item.state = state
                item.device = device
                adapter.remove(item)
                adapter.notifyDataSetChanged()
                if (bondItem != null) {
                    adapter.remove(bondItem)
                }
                adapter.add(0, item)
            }
            bondItem != null -> {
                bondItem.state = state
                bondItem.device = device
            }
            else -> {
                val bluetoothItem: BluetoothListAdapter.Companion.Item = createBluetoothItem(device)
                bluetoothItem.state = state
                adapter.add(0, bluetoothItem)
            }
        }
        adapter.notifyDataSetChanged()
        initPager()
    }

    /**
     * 可用设备列表发生改变
     */
    private fun useDevStateChange(state: Int, device: BluetoothDevice?) {
        val item: BluetoothListAdapter.Companion.Item? = findItemByList(adapter.getData(), device)
        val bondItem: BluetoothListAdapter.Companion.Item? = findItemByList(adapter.getData(), device)
        when {
            bondItem != null -> {
                bondItem.state = state
                bondItem.device = device
                adapter.remove(bondItem)
                adapter.notifyDataSetChanged()
                if (item != null) {
                    adapter.remove(item)
                }
                adapter.add(0, bondItem)
            }
            item != null -> {
                item.state = state
                item.device = device
            }
            else -> {
                val bluetoothItem: BluetoothListAdapter.Companion.Item = createBluetoothItem(device)
                bluetoothItem.state = state
                adapter.add(0, bluetoothItem)
            }
        }
        adapter.notifyDataSetChanged()
        initPager()
    }

    //蓝牙状态监听
    private val mBTStateListener: IBTStateListener = object : IBTStateListener {
        /**
         * 蓝牙开关状态
         * int STATE_OFF = 10; //蓝牙关闭
         * int STATE_ON = 12; //蓝牙打开
         * int STATE_TURNING_OFF = 13; //蓝牙正在关闭
         * int STATE_TURNING_ON = 11; //蓝牙正在打开
         */
        override fun onStateChange(state: Int) {
            when (state) {
                BluetoothAdapter.STATE_OFF -> {
                    Toast.makeText(requireContext(), "蓝牙已关闭", Toast.LENGTH_SHORT).show()
                    adapter.clear()
                    initPager()
                }
                BluetoothAdapter.STATE_ON -> {
                    Toast.makeText(requireContext(), "蓝牙已打开", Toast.LENGTH_SHORT).show()
                    initBondedDevices()
                    //设置可见时间
                    mBluetoothHelper.setDiscoverableTimeout(300)
                    mBluetoothHelper.startDiscovery()
                }
                BluetoothAdapter.STATE_TURNING_OFF, BluetoothAdapter.STATE_TURNING_ON -> {
                }
            }
        }
    }

    //蓝牙搜索监听
    private val mBTScanListener: IBTScanListener = object : IBTScanListener {
        override fun onScanStart() {
            Toast.makeText(requireContext(), "搜索开始", Toast.LENGTH_SHORT).show()
        }

        override fun onScanStop(deviceList: List<BluetoothDevice>?) {
        }

        override fun onFindDevice(device: BluetoothDevice?) { //发现新设备
            if (device?.bondState == BluetoothDevice.BOND_BONDED) { //已配对
                addBondDevice(State.STATE_BONDED, device)
            } else {
                addDevUse(device)
            }
        }
    }

    //蓝牙配对监听
    private val mBTBondListener: IBTBondListener = object : IBTBondListener {
        /**
         * 设备配对状态改变
         * int BOND_NONE = 10; //配对没有成功
         * int BOND_BONDING = 11; //配对中
         * int BOND_BONDED = 12; //配对成功
         */
        override fun onBondStateChange(device: BluetoothDevice?) {
            when (device?.bondState) {
                BluetoothDevice.BOND_BONDED -> { //已配对
                    paireDevStateChange(State.STATE_BONDED, device)
                    mBluetoothHelper.connect(device)
                }
                BluetoothDevice.BOND_BONDING -> { //配对中
                    useDevStateChange(State.STATE_BONDING, device)
                }
                else -> { //未配对
                    val item: BluetoothListAdapter.Companion.Item? = findItemByList(adapter.getData(), device)
                    if (item != null && item.state == State.STATE_BONDING) {
                        Toast.makeText(requireContext(), "请确认配对设备已打开且在通信范围内", Toast.LENGTH_SHORT)
                            .show()
                    }
                    useDevStateChange(State.STATE_BOND_NONE, device)
                }
            }
        }
    }

    //蓝牙配对监听
    private val mBTConnectListener: IBTConnectListener = object : IBTConnectListener {
        override fun onConnecting(bluetoothDevice: BluetoothDevice?) { //连接中
            paireDevStateChange(State.STATE_CONNECTING, bluetoothDevice)
        }

        override fun onConnected(bluetoothDevice: BluetoothDevice?) { //连接成功
            paireDevStateChange(State.STATE_CONNECTED, bluetoothDevice)
        }

        override fun onDisConnecting(bluetoothDevice: BluetoothDevice?) { //断开中
            paireDevStateChange(State.STATE_DISCONNECTING, bluetoothDevice)
        }

        override fun onDisConnect(bluetoothDevice: BluetoothDevice?) { //断开
            paireDevStateChange(State.STATE_DISCONNECTED, bluetoothDevice)
        }

        override fun onConnectedDevice(devices: MutableList<BluetoothDevice>) { //已连接设备
            if (devices.isEmpty()) return
            for (device in devices) {
                val item: BluetoothListAdapter.Companion.Item? = findItemByList(items, device)
                if (item != null) {
                    item.device = device
                    if (mBluetoothHelper.isConnected(device)) {
                        item.state = State.STATE_CONNECTED
                    } else if (item.state != State.STATE_CONNECTED) {
                        item.state = State.STATE_DISCONNECTED
                    }
                } else {
                    val bluetoothItem: BluetoothListAdapter.Companion.Item = createBluetoothItem(device)
                    if (mBluetoothHelper.isConnected(device)) {
                        bluetoothItem.state = State.STATE_CONNECTED
                    } else {
                        bluetoothItem.state = State.STATE_DISCONNECTED
                    }
                    adapter.add(0, bluetoothItem)
                }
            }
            adapter.notifyDataSetChanged()
            initPager()
        }
    }
}