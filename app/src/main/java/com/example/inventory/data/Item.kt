package com.example.inventory.data

import androidx.room.Entity
import androidx.room.PrimaryKey


/**
 * Entity data class represents a single row in the database.
 */
@Entity(tableName = "items")
data class Item(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val servicio: String,
    val hora: String,
    val fecha: String
)

