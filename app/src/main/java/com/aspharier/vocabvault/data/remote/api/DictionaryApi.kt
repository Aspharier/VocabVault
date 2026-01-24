package com.aspharier.vocabvault.data.remote.api

import com.aspharier.vocabvault.data.remote.dto.WordResponseDto
import retrofit2.http.GET
import retrofit2.http.Path

interface DictionaryApi {

    @GET("api/v2/entries/en/{word}")
    suspend fun getWordDefinition(
        @Path("word") word: String
    ): List<WordResponseDto>
}