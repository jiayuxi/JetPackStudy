package com.jiayx.jetpackstudy.paging3.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PokemonEntity(
    @PrimaryKey
    val name: String,
    var pokemonId: Int = 0,
    val page: Int = 0,
    val url: String,
    val remoteName: String
)