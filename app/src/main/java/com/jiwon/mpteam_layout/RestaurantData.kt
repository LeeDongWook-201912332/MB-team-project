package com.jiwon.mpteam_layout

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "food_list")
data class RestaurantData(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(index = true, name = "id") var food_id: Int,
    @ColumnInfo(index = true, name = "Restaurant") var RESTAURANT: String,
    @ColumnInfo(index = true, name = "Address") var ADDRESS: String,
    @ColumnInfo(index = true, name = "Operate_hour") var OPERATEHOUR:String,
    @ColumnInfo(index = true, name = "Break") var BREAK:String?,
    @ColumnInfo(index = true, name = "Last_order") var LASTORDER: String?,
    @ColumnInfo(index = true, name = "Menu") var MENU: String,
    @ColumnInfo(index = true, name = "Price") var PRICE: String,
    @ColumnInfo(index = true, name = "Location") var LOCATION: String,
    @ColumnInfo(index = true, name = "Category") var CATEGORY: String,
    @ColumnInfo(index = true, name = "Rid") var RID: Int,
    @ColumnInfo(index = true, name = "Latitude") var LATITUDE: String,
    @ColumnInfo(index = true, name = "Longitude") var LONGITUDE: String,
    @ColumnInfo(index = true, name = "Rating") var RATING: Int
)