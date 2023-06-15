package com.example.cookmate.response

import com.google.gson.annotations.SerializedName

data class SearchRecipeResponse(

	@field:SerializedName("data")
	val data: MutableList<DataItem>
)

data class DataSearchRecipeItem(

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
