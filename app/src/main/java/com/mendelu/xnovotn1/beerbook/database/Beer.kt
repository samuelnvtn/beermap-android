package com.mendelu.xnovotn1.beerbook.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(tableName = "beers")
data class Beer(
    var name: String,
    var timestamp: Instant,
    var locationName: String,
    var description: String?,
    var rating: Float,
    var imageUri: String?,
    var latitude: Double,
    var longitude: Double,
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    var beerState: Boolean = false,
){
    fun isDefault(): Boolean = this == Beer("", Instant.now(),"","",0.0f,"", 0.0, 0.0)
}