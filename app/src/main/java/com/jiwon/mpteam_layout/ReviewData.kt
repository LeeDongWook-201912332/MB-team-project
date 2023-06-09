package com.jiwon.mpteam_layout

//data class ReviewData(var Restaurant: String, var Date : String, var Menu: String,var Rating:Int,var Content:String){
//    constructor():this("noinfo","noinfo","noinfo",0,"noinfo")//디폴트 생성자 반드시 추가해야함 기본값
//}
data class ReviewData(
    val restaurant : String = "",
    val date: String = "",
    val menu: String = "",
    val rating: String = "",
    val content: String = ""
)
