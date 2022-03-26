package com.jiayx.bluetooth.const

sealed interface State {
    companion object {
        //未配对
        const val STATE_BOND_NONE: Int = -1

        //未连接
        const val STATE_UNCONNECT: Int = 0

        //配对中
        const val STATE_BONDING: Int = 1

        //已配对
        const val STATE_BONDED: Int = 2

        //连接中
        const val STATE_CONNECTING: Int = 3

        //已连接
        const val STATE_CONNECTED: Int = 4

        //断开中
        const val STATE_DISCONNECTING: Int = 5

        //已断开(但还保存)
        const val STATE_DISCONNECTED: Int = 6
    }
}