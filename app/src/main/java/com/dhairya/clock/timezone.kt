package com.dhairya.clock

class timezone(var name:String,var gmt:String, var timestamp:String) {

    companion object{

        var zoneArray: ArrayList<timezone> = arrayListOf()
        var clockArray: ArrayList<timezone> = arrayListOf()
    }

}