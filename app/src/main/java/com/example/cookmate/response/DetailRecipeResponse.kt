package com.example.cookmate.response

import com.google.gson.annotations.SerializedName

data class DetailRecipeResponse(

	@field:SerializedName("data")
	val data: Data
)

data class Data(

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("ingredients")
	val ingredients: String,

	@field:SerializedName("pics_url")
	val picsUrl: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("steps")
	val steps: String,

	@field:SerializedName("main_ingredient")
	val mainIngredient: String
)
