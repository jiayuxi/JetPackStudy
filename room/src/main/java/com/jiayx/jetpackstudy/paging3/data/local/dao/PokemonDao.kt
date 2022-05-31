package com.jiayx.jetpackstudy.paging3.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jiayx.jetpackstudy.paging3.model.PokemonEntity

@Dao
interface PokemonDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemon(pokemonList: List<PokemonEntity>)

    @Query("SELECT * FROM PokemonEntity")
    fun getPokemon(): PagingSource<Int, PokemonEntity>

    @Query("DELETE FROM PokemonEntity where remoteName = :name")
    suspend fun clearPokemon(name: String)

    @Query("SELECT * FROM PokemonEntity where name LIKE '%' || :parameter || '%'")
    fun pokemonInfoByParameter(parameter: String): PagingSource<Int, PokemonEntity>
}