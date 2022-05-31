package com.jiayx.jetpackstudy.paging3.data.mapper

import com.jiayx.jetpackstudy.paging3.model.PokemonEntity
import com.jiayx.jetpackstudy.paging3.model.PokemonItemModel

class Entity2ItemModelMapper : Mapper<PokemonEntity, PokemonItemModel> {

    override fun map(input: PokemonEntity, value: Int): PokemonItemModel =
        PokemonItemModel(name = input.name, url = input.url)

}