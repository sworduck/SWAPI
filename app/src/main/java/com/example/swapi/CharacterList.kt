package com.example.swapi

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable


@Serializable
data class CharacterList(@SerializedName("count")
                     val count: Int,
                    @SerializedName("next")
                     val next: String?,
                    @SerializedName("previous")
                     val previous: String?,
                    @SerializedName("results")
                     val results: List<CharacterCloud>?=null)


data class CharacterCloud(
    @SerializedName("name")
    val name: String,
    @SerializedName("height")
    val height: String,
    @SerializedName("mass")
    val mass: String,
    @SerializedName("hair_color")
    private val hair_color: String,
    @SerializedName("skin_color")
    private val skin_color: String,
    @SerializedName("eye_color")
    private val eye_color: String,
    @SerializedName("birth_year")
    private val birth_year: String,
    @SerializedName("gender")
    private val gender: String,
    @SerializedName("homeworld")
    val homeworld: String,
    @SerializedName("films")
    private val films: List<String>?=null,
    @SerializedName("species")
    private val species: List<String>?=null,
    @SerializedName("vehicles")
    private val vehicles: List<String>?=null,
    @SerializedName("starships")
    private val starships: List<String>?=null,
    @SerializedName("created")
    private val created: String,
    @SerializedName("edited")
    private val edited: String,
    @SerializedName("url")
    private val url: String


)

/*
{
"Date": "2022-05-28T11:30:00+03:00",
"PreviousDate": "2022-05-27T11:30:00+03:00",
"PreviousURL": "\/\/www.cbr-xml-daily.ru\/archive\/2022\/05\/27\/daily_json.js",
"Timestamp": "2022-05-29T20:00:00+03:00",
"Valute": {
    "AUD": {
        "ID": "R01010",
        "NumCode": "036",
        "CharCode": "AUD",
        "Nominal": 1,
        "Name": "Австралийский доллар",
        "Value": 47.3718,
        "Previous": 43.8442
    },
    "AZN": {
        "ID": "R01020A",
        "NumCode": "944",
        "CharCode": "AZN",
        "Nominal": 1,
        "Name": "Азербайджанский манат",
        "Value": 39.0605,
        "Previous": 36.4997
    },
    ...
}
}

@Serializable
data class CurrentCurrency(val Date: String? = null,
                       val PreviousDate: String? = null,
                       val PreviousURL: String? = null,
                       val Timestamp: String? = null,
                       @SerialName("Valute") val valutes: Map<String, Valute>? = null)

@Serializable
data class Valute(@SerialName("ID") val id: String? = null,
              @SerialName("NumCode") val numCode: String? = null,
              @SerialName("CharCode") val charCode: String? = null,
              @SerialName("Nominal") val nominal: Int? = null,
              @SerialName("Name") val name: String? = null,
              @SerialName("Value") val value: Double? = null,
              @SerialName("Previous") val previous: Double? = null)

@Serializable
data class CurrentCurrencyWithListValuteAndName(
var Date: String? = null,
var PreviousDate: String? = null,
var PreviousURL: String? = null,
var Timestamp: String? = null,
var valutes: List<ValuteAndName>? = null)

@Serializable
data class ValuteAndName(var name: String, var valute: Valute)

 */