package com.example.minishop.data.repository

import com.example.minishop.data.remote.NetworkResult
import org.json.JSONObject
import retrofit2.HttpException
import retrofit2.Response
import java.net.ConnectException
import java.net.UnknownHostException

suspend fun  <T> safeCall(call: suspend () -> Response<T>): NetworkResult<T> {
    return try {
        val response = call()
        val body = response.body()
        if (response.isSuccessful && body!=null) {
            NetworkResult.Success(body)
        } else if( response.errorBody() != null) {
            val message = JSONObject(response.errorBody()!!.charStream().readText()).getString("message")
            NetworkResult.Error(message)
        } else {
            NetworkResult.Error("An Unexpected Error Occurred")
        }
    } catch(e: ConnectException) {
        NetworkResult.Error(message = "Unable to reach server")
    } catch (e: UnknownHostException) {
        NetworkResult.Error(message = "No internet connection")
    } catch (e: HttpException) {
        NetworkResult.Error(message = "HTTP ${e.code()}: ${e.message()}")
    }
}