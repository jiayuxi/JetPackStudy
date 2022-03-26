package com.jiayx.wlan.softInput

const val SHIFT_KEY = 1
const val DEL_KEY = 2
const val SPACE_KEY = 3
const val OK_KEY = 4
const val CANCEL_KEY = 5
const val SWITCH_CHARACTER_KEY = 6
const val SWITCH_SPECIAL_KEY = 7

enum class CharactersEnum(
    val keyword: String,
    val shiftKeyword: String,
    val columnsSpan: Int,
    val res: Int,
    val selectRes: Int,
    val keyType: Int
) {
    NUMBER_1("1", "1", 1, 0, 0, 0),
    NUMBER_2("2", "2", 1, 0, 0, 0),
    NUMBER_3("3", "3", 1, 0, 0, 0),
    NUMBER_4("4", "4", 1, 0, 0, 0),
    NUMBER_5("5", "5", 1, 0, 0, 0),
    NUMBER_6("6", "6", 1, 0, 0, 0),
    NUMBER_7("7", "7", 1, 0, 0, 0),
    NUMBER_8("8", "8", 1, 0, 0, 0),
    NUMBER_9("9", "9", 1, 0, 0, 0),
    NUMBER_0("0", "0", 1, 0, 0, 0),
    Q("q", "Q", 1, 0, 0, 0),
    W("w", "W", 1, 0, 0, 0),
    E("e", "E", 1, 0, 0, 0),
    R("r", "R", 1, 0, 0, 0),
    T("t", "T", 1, 0, 0, 0),
    Y("y", "Y", 1, 0, 0, 0),
    U("u", "U", 1, 0, 0, 0),
    I("i", "I", 1, 0, 0, 0),
    O("o", "O", 1, 0, 0, 0),
    P("p", "P", 1, 0, 0, 0),
    A("a", "A", 1, 0, 0, 0),
    S("s", "S", 1, 0, 0, 0),
    D("d", "D", 1, 0, 0, 0),
    F("f", "F", 1, 0, 0, 0),
    G("g", "G", 1, 0, 0, 0),
    H("h", "H", 1, 0, 0, 0),
    J("j", "J", 1, 0, 0, 0),
    K("k", "K", 1, 0, 0, 0),
    L("l", "L", 1, 0, 0, 0),
    SPECIAL_1("@", "@", 1, 0, 0, 0),
    SHIFT(
        "",
        "",
        1,
        com.jiayx.wlan.R.mipmap.lib_wifi_soft_input_shift,
        com.jiayx.wlan.R.mipmap.lib_wifi_soft_input_shift_select,
        SHIFT_KEY
    ),
    Z("z", "Z", 1, 0, 0, 0),
    X("x", "X", 1, 0, 0, 0),
    C("c", "C", 1, 0, 0, 0),
    V("v", "V", 1, 0, 0, 0),
    B("b", "B", 1, 0, 0, 0),
    N("n", "N", 1, 0, 0, 0),
    M("m", "M", 1, 0, 0, 0),
    DEL("", "", 2, com.jiayx.wlan.R.mipmap.lib_wifi_soft_input_del, 0, DEL_KEY),
    SWITCH("#+=", "#+=", 2, 0, 0, SWITCH_CHARACTER_KEY),
    SPECIAL_17(",", ",", 1, 0, 0, 0),
    Space("", "", 3, com.jiayx.wlan.R.mipmap.lib_wifi_soft_input_space, 0, SPACE_KEY),
    SPECIAL_20(".", ".", 1, 0, 0, 0),
    OK("连接", "连接", 2, 0, 0, OK_KEY),
    CANCEL("取消", "取消", 1, 0, 0, CANCEL_KEY),
    ;

}