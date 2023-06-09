package com.jiwon.mpteam_layout

import androidx.room.*

@Dao
interface RestaurantDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertProduct(restaurantData: RestaurantData)

    @Delete
    fun deleteProduct(restaurantData: RestaurantData)

    @Update
    fun updateProduct(restaurantData: RestaurantData)

    @Query("Select * from food_list")
    fun getAllRecord(): List<RestaurantData>

//    @Query("Select * from food_list where Restaurant = :restaurant")
//    fun findProduct(restaurant: String): List<RestaurantData>

    @Query("Select * from food_list where Restaurant like :restaurant Group By Restaurant")
    fun findRestaurant(restaurant: String): List<RestaurantData>

    @Query("Select * from food_list where Category = :category Group By Restaurant")
    fun findRestaurantByMenuType(category: String): List<RestaurantData>

    @Query("Select * from food_list where location = :location Group By Restaurant")
    fun findRestaurantByLocation(location: String): List<RestaurantData>

    @Query("Select * from food_list where location = :location and category = :category Group By Restaurant")
    fun findRestaurantByLocationAndCategory(location: String, category: String): List<RestaurantData>

    @Query("Select * from food_list Group By Restaurant")
    fun sortByRestaurant(): List<RestaurantData>

    @Query("Select * from food_list where Restaurant like :restaurant")
    fun getAllMenu(restaurant: String): List<RestaurantData>
}