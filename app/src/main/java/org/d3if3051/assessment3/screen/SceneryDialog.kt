package org.d3if3051.assessment3.screen

import android.content.res.Configuration
import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import org.d3if3051.assessment3.ui.theme.Assessment3Theme
import org.d3if3051.assessment3.R
import org.d3if3051.assessment3.ui.theme.DarkGreen
import org.d3if3051.assessment3.ui.theme.DarkGreen2
import org.d3if3051.assessment3.ui.theme.SoftGreen


@Composable
fun SceneryDialog(
    bitmap: Bitmap?,
    onDismissReq: () -> Unit,
    onConfirmation: (String, String) -> Unit
){
    var judulPemandangan by remember { mutableStateOf("") }
    var lokasi by remember { mutableStateOf("") }

    Dialog(onDismissRequest = { onDismissReq() }) {
        Card(
            modifier = Modifier.padding(16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = cardColors(Color(0xFF29A478))
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    bitmap = bitmap!!.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                )
                OutlinedTextField(
                    value = judulPemandangan,
                    onValueChange = { judulPemandangan = it },
                    label = { Text(text = stringResource(id = R.string.title_scenery)) },
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.padding(top = 8.dp)
                )
                OutlinedTextField(
                    value = lokasi,
                    onValueChange = { lokasi = it },
                    label = { Text(text = stringResource(id = R.string.location)) },
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        imeAction = ImeAction.Done
                    ),
                    modifier = Modifier.padding(top = 8.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = { onDismissReq() },
                        modifier = Modifier.padding(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SoftGreen,
                            contentColor = Color.Black
                        )
                    ) {
                        Text(
                            text = stringResource(R.string.cancel_button),
                            color = Color(0xFF29A478)
                        )
                    }
                    Button(
                        onClick = { onConfirmation(judulPemandangan, lokasi) },
                        enabled = judulPemandangan.isNotEmpty() && lokasi.isNotEmpty(),
                        modifier = Modifier.padding(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = DarkGreen2,
                            contentColor = SoftGreen
                        )
                    ) {
                        Text(
                            text = stringResource(R.string.save_button),
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun ScreneryDialogPreview() {
    Assessment3Theme {
        SceneryDialog(
            bitmap = null,
            onDismissReq = {},
            onConfirmation = { _, _ ->}
        )
    }
}