package org.d3if3051.assessment3.screen

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.d3if3051.assessment3.network.ApiStatus
import org.d3if3051.assessment3.network.Api
import org.d3if3051.assessment3.network.ImageApi
import org.d3if3051.assessment3.model.Scenery
import org.d3if3051.assessment3.model.ImageData
import org.d3if3051.assessment3.model.SceneryCreate
import java.io.ByteArrayOutputStream

class MainViewModel : ViewModel() {

    var data = mutableStateOf(emptyList<Scenery>())
        private set

    var status = MutableStateFlow(ApiStatus.LOADING)
        private set

    var errorMessage = mutableStateOf<String?>(null)
        private set

    var querySucces = mutableStateOf(false)
        private set

    fun retrieveData(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            status.value = ApiStatus.LOADING
            try {
                data.value = Api.userService.getAllScenery(userId)
                status.value = ApiStatus.SUCCESS
            } catch (e: Exception) {
                Log.d("MainViewModel", "Failure: ${e.message}")
                status.value = ApiStatus.FAILED
            }
        }
    }


    fun saveData(userId: String, judulPemandangan: String, lokasi: String, bitmap: Bitmap) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val upload = ImageApi.imgService.uploadImg(
                    image = bitmap.toMultipartBody()
                )
                Log.d("MainVM", "$bitmap")
                Log.d("MainVM", "$upload")
                Log.d("MainVM", "${upload.data}")
                Log.d("MainVM", upload.data.deletehash)
                Log.d("MainVM", upload.data.link)
                Log.d("MainVM", "${upload.success}")
                if (upload.success) {
                Log.d("MainVM after true", "$userId-$judulPemandangan-$lokasi")
                    val result = Api.userService.addScenery(
                        SceneryCreate(
                            userId,
                            judulPemandangan,
                            lokasi,
                            transformImageData(upload.data),
                            upload.data.deletehash
                        )
                    )
                Log.d("MainVM", "$result")
                    querySucces.value = true
                    status.value = ApiStatus.SUCCESS
                    retrieveData(userId)
                }
            } catch (e: Exception) {
                Log.d("MainVM", "${e.message}")
                if (e.message == "HTTP 500 ") {
                    errorMessage.value = "Error: Database Idle, harap masukkan data kembali."
                } else {
                    errorMessage.value = "Error: ${e.message}"
                    Log.d("MainViewModel", "Failure: ${e.message}")
                }
            }
        }
    }

    fun deleteData(email: String, sceneryId: Int, deleteHash: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val upload = ImageApi.imgService.deleteImg(
                    deleteHash = deleteHash
                )
                if (upload.success) {
                    Api.userService.deleteScenery(sceneryId, email)
                    retrieveData(email)
                }
            } catch (e: Exception) {
                if (e.message == "HTTP 500 ") {
                    errorMessage.value = "Error: Database Idle, harap masukkan data kembali."
                } else {
                    errorMessage.value = "Error: ${e.message}"
                    Log.d("MainViewModel", "Failure: ${e.message}")
                }
            }
        }
    }

    private fun Bitmap.toMultipartBody(): MultipartBody.Part {
        val stream = ByteArrayOutputStream()
        compress(Bitmap.CompressFormat.JPEG, 80, stream)
        val byteArray = stream.toByteArray()
        val requestBody = byteArray.toRequestBody(
            "image/jpg".toMediaTypeOrNull(), 0, byteArray.size
        )
        return MultipartBody.Part.createFormData("image", "image.jpg", requestBody)
    }

    private fun transformImageData(imageData: ImageData): String {
        val extension = when (imageData.type) {
            "image/png" -> "png"
            "image/jpeg" -> "jpg"
            "image/gif" -> "gif"
            else -> throw IllegalArgumentException("Unsupported image type")
        }
        return "${imageData.id}.$extension"
    }

    fun clearMessage() {
        errorMessage.value = null
    }

}