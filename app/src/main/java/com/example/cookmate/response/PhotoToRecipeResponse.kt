package com.example.cookmate.response

import com.google.gson.annotations.SerializedName

data class PhotoToRecipeResponse(

	@field:SerializedName("data")
	val data: MutableList<DataPhotoItem>
)

data class DataPhotoItem(

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("ingredients")
	val ingredients: String,

	@field:SerializedName("pics_url")
	val picsUrl: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("steps")
	val steps: String
)
