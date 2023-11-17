package com.example.VNMH.data.remote

import com.example.VNMH.data.remote.dto.MuseumItem
import io.ktor.client.HttpClient
import io.ktor.client.features.ClientRequestException
import io.ktor.client.features.RedirectResponseException
import io.ktor.client.features.ServerResponseException
import io.ktor.client.request.get
import io.ktor.client.request.url


class MuseumServiceImpl(private val client: HttpClient): MuseumService {


    override suspend fun getBeforePhotography(): List<MuseumItem> {
        return try {
            client.get {url(HttpRoutes.BEFORE1945)}
        } catch (e: RedirectResponseException) { // redirect issue
            // 3xx - responses
            println("Error redirect: ${e.response.status.description}")
            emptyList()
        } catch (e: ClientRequestException) { // post invalid data
            // 4xx - responses
            println("Error client: ${e.response.status.description}")
            emptyList()
        } catch (e: ServerResponseException) { // server side went wrong e.g database crashed
            // 5xx - responses
            println("Error server: ${e.response.status.description}")
            emptyList()
        } catch (e: Exception) {
            println("Error error: ${e.message}")
            emptyList()
        }
    }
    override suspend fun getAfterPhotography(): List<MuseumItem> {
        return try {
            client.get {url(HttpRoutes.AFTER1945)}
        } catch (e: RedirectResponseException) { // redirect issue
            // 3xx - responses
            println("Error redirect: ${e.response.status.description}")
            emptyList()
        } catch (e: ClientRequestException) { // post invalid data
            // 4xx - responses
            println("Error client: ${e.response.status.description}")
            emptyList()
        } catch (e: ServerResponseException) { // server side went wrong e.g database crashed
            // 5xx - responses
            println("Error server: ${e.response.status.description}")
            emptyList()
        } catch (e: Exception) {
            println("Error error: ${e.message}")
            emptyList()
        }
    }
}
