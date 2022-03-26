package com.jiayx.wlan.softInput


enum class SpecialCharactersEnum(
    val keyword: String,
    val shiftKeyword: String,
    val columnsSpan: Int,
    val res: Int,
    val selectRes: Int,
    val keyType: Int
) {
    SPECIAL_15("!", "!", 1, 0, 0, 0),
    SPECIAL_1("@", "@", 1, 0, 0, 0),
    SPECIAL_2("#", "#", 1, 0, 0, 0),
    SPECIAL_3("$", "$", 1, 0, 0, 0),
    SPECIAL_4("%", "%", 1, 0, 0, 0),
    SPECIAL_35("^", "^", 1, 0, 0, 0),
    SPECIAL_5("&", "&", 1, 0, 0, 0),
    SPECIAL_10("*", "*", 1, 0, 0, 0),
    SPECIAL_8("(", "(", 1, 0, 0, 0),
    SPECIAL_9(")", ")", 1, 0, 0, 0),
    SPECIAL_18("_", "_", 1, 0, 0, 0),
    SPECIAL_11("\"", "\"", 1, 0, 0, 0),
    SPECIAL_12("\'", "\'", 1, 0, 0, 0),
    SPECIAL_17(",", ",", 1, 0, 0, 0),
    SPECIAL_13(":", ":", 1, 0, 0, 0),
    SPECIAL_14(";", ";", 1, 0, 0, 0),
    SPECIAL_20(".", ".", 1, 0, 0, 0),
    SPECIAL_16("?", "?", 1, 0, 0, 0),
    SPECIAL_19("/", "/", 1, 0, 0, 0),
    SPECIAL_40("\\", "\\", 1, 0, 0, 0),
    SPECIAL_7("+", "+", 1, 0, 0, 0),
    SPECIAL_37("=", "=", 1, 0, 0, 0),
    SPECIAL_6("-", "-", 1, 0, 0, 0),
    SPECIAL_45("[", "[", 1, 0, 0, 0),
    SPECIAL_46("]", "]", 1, 0, 0, 0),
    SPECIAL_47("<", "<", 1, 0, 0, 0),
    SPECIAL_48(">", ">", 1, 0, 0, 0),
    SPECIAL_23("|", "|", 1, 0, 0, 0),
    DEL("", "", 2, com.jiayx.wlan.R.mipmap.lib_wifi_soft_input_del, 0, DEL_KEY),
    SWITCH("1abc","1abc",1,0,0, SWITCH_SPECIAL_KEY),
    SPECIAL_38("{", "{", 1, 0, 0, 0),
    SPECIAL_39("}", "}", 1, 0, 0, 0),
    SPECIAL_21("~", "~", 1, 0, 0, 0),
    Space("", "", 2, com.jiayx.wlan.R.mipmap.lib_wifi_soft_input_space, 0, SPACE_KEY),
    SPECIAL_22("`", "`", 1, 0, 0, 0),
    OK("连接", "连接", 2, 0, 0, OK_KEY),
    CANCEL("取消", "取消", 1, 0, 0, CANCEL_KEY),

    ;

}