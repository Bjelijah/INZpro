package com.inz.bean

import org.json.JSONObject

class AlarmBean() {
    var value = -1
    var status = -1
    constructor(jsonStr:String):this(){
        var jsonObj = JSONObject(jsonStr)
        value = jsonObj.getInt("value")
        status = jsonObj.getInt("status")
    }

    override fun toString(): String {
        return "value=$value status=$status"
    }

}