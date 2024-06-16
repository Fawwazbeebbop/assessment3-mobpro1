package org.d3if3051.assessment3.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import coil.request.ImageRequest
import org.d3if3051.assessment3.R
import org.d3if3051.assessment3.model.Scenery
import org.d3if3051.assessment3.network.ImageApi
import org.d3if3051.assessment3.ui.theme.DarkGreen
import org.d3if3051.assessment3.ui.theme.Red
import org.d3if3051.assessment3.ui.theme.SoftGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteDialog(
    data: Scenery,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(modifier = Modifier.padding(16.dp), shape = RoundedCornerShape(16.dp), colors = cardColors(containerColor = DarkGreen)) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Hapus gambar?")
                Spacer(modifier = Modifier.height(16.dp))
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(ImageApi.getImageUrl(data.image_id))
                        .crossfade(true)
                        .build(),
                    contentDescription = "Image ${data.judul_pemandangan}"
                    ,
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.loading_img),
                    error = painterResource(id = R.drawable.baseline_broken_image_24),
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = { onDismissRequest() },
                    modifier = Modifier.padding(8.dp),
                    colors = buttonColors(containerColor = SoftGreen, contentColor = DarkGreen)
                ) {
                    Text(text = "Cancel")
                }
                Button(
                    onClick = { onConfirmation() },
                    modifier = Modifier.padding(8.dp),
                    colors = buttonColors(containerColor = Red, contentColor = Color.White)
                ) {
                    Text(text = "Delete")
                }
            }
        }
    }
}

@Preview
@Composable
private fun Prev() {
//    HapusDialog(onDismissRequest = { /*TODO*/ }) {
//
//    }
}
