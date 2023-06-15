package com.example.cookmate.response

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class RecipeResponse(

	@field:SerializedName("data")
	val data: MutableList<DataItem>
)

@Parcelize
@Entity(tableName = "recipe")
data class DataItem(

	@ColumnInfo(name = "name")
	@field:SerializedName("name")
	val name: String,

	@ColumnInfo(name = "ingredients")
	@field:SerializedName("ingredients")
	val ingredients: String,

	@ColumnInfo(name = "pics_url")
	@field:SerializedName("pics_url")
	val picsUrl: String,

	@ColumnInfo(name = "id")
	@field:SerializedName("id")
	val id: String,

	@ColumnInfo(name = "steps")
	@field:SerializedName("steps")
	val steps: String
) : Parcelable
