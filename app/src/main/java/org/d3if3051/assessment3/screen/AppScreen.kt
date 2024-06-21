package org.d3if3051.assessment3.screen

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.ClearCredentialException
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.d3if3051.assessment3.BuildConfig
import org.d3if3051.assessment3.R
import org.d3if3051.assessment3.model.Scenery
import org.d3if3051.assessment3.model.User
import org.d3if3051.assessment3.network.Api
import org.d3if3051.assessment3.network.ApiStatus
import org.d3if3051.assessment3.network.UserDataStore
import org.d3if3051.assessment3.ui.theme.Assessment3Theme
import org.d3if3051.assessment3.ui.theme.DarkGreen
import org.d3if3051.assessment3.ui.theme.SoftGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScreen(navController: NavHostController) {
    val context = LocalContext.current
    val dataStore = UserDataStore(context)
    val user by dataStore.userFlow.collectAsState(User())
    val showList by dataStore.layoutFlow.collectAsState(true)


    val viewModel: MainViewModel = viewModel()
    val errorMessage by viewModel.errorMessage

    var showDialog by remember { mutableStateOf(false) }
    var showSceneryDialog by remember { mutableStateOf(false) }

    var bitmap: Bitmap? by remember { mutableStateOf(null) }
    val launcer = rememberLauncherForActivityResult(CropImageContract()) {
        bitmap = getCroppedImage(context.contentResolver, it)
        if (bitmap != null) showSceneryDialog = true
    }

    val isSuccess by viewModel.querySuccess

    LaunchedEffect(isSuccess) {
        if (isSuccess) {
            Toast.makeText(context, "Done", Toast.LENGTH_LONG).show()
            viewModel.clearMessage()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.back_arrow),
                            tint = Color.White
                        )
                    }
                },
                title = {
                    Text(
                        text = stringResource(R.string.app_name),
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(DarkGreen),
                actions = {
                    IconButton(
                        onClick = {
                            CoroutineScope(Dispatchers.IO).launch {
                                dataStore.saveLayout(!showList)
                            }
                        }) {
                        Icon(
                            painter = painterResource(
                                if (showList) R.drawable.baseline_grid_view_24
                                else R.drawable.baseline_view_list_24,

                                ),
                            contentDescription =
                            if (showList) "Grid"
                            else "List",
                            tint = SoftGreen
                        )
                    }
                    IconButton(onClick = {
                        if (user.email.isEmpty()) {
                            CoroutineScope(Dispatchers.IO).launch { signIn(context, dataStore) }
                        } else {
                            showDialog = true
                        }
                    }) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_account_circle_24),
                            contentDescription = stringResource(R.string.icon_profile),
                            tint = Color(0xFFCDF6D1)
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                containerColor = Color(0xFF29A478),
                onClick = {
                    val options = CropImageContractOptions(
                        null, CropImageOptions(
                            imageSourceIncludeGallery = true,
                            imageSourceIncludeCamera = true,
                            fixAspectRatio = true
                        )
                    )
                    launcer.launch(options)
                }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(id = R.string.add_scenery),
                    tint = Color(0xFFCDF6D1)
                )
            }
        },
        containerColor = Color(0xFFCDF6D1)
    ) {
        ScreenContent2(showList, viewModel, user.email, modifier = Modifier.padding(it), context, dataStore)

        if (showDialog) {
            ProfilePopUp(
                user = user,
                onDismissReq = { showDialog = false }
            ) {
                CoroutineScope(Dispatchers.IO).launch { signOut(context, dataStore) }
                showDialog = false
            }
        }
        if (showSceneryDialog) {
            SceneryDialog(
                bitmap = bitmap,
                onDismissReq = { showSceneryDialog = false }) { judulPemandangan, lokasi ->
                viewModel.saveData(user.email, judulPemandangan, lokasi, bitmap!!)
                showSceneryDialog = false
            }
        }
        if (errorMessage != null) {
            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
            viewModel.clearMessage()
        }
    }
}

@Composable
fun ScreenContent2(
    showList: Boolean,
    viewModel: MainViewModel,
    userId: String,
    modifier: Modifier,
    context: Context,
    dataStore: UserDataStore
) {
    val data by viewModel.data
    val status by viewModel.status.collectAsState()
    var showDeleteDialog by remember { mutableStateOf(false) }
    var sceneryData by remember { mutableStateOf<Scenery?>(null) }


    LaunchedEffect(userId) {
        viewModel.retrieveData(userId)
    }

    when (status) {
        ApiStatus.LOADING -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier,
                    color = Color(0xFF29A478)
                )
            }
        }

        ApiStatus.SUCCESS -> {
            if (showList) {
                LazyColumn(
                    modifier = modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 90.dp)
                ) {
                    items(data) {
                        ListItem(it) {
                            sceneryData = it
                            showDeleteDialog = true
                        }
                        HorizontalDivider()
                    }
                }
            } else {
                LazyVerticalGrid(
                    modifier = modifier.fillMaxSize(),
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(bottom = 95.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(data) {
                        GridData(it) {
                            sceneryData = it
                            showDeleteDialog = true
                        }
                    }
                }
            }
            if (showDeleteDialog) {
                DeleteDialog(data = sceneryData!!, onDismissRequest = { showDeleteDialog = false }) {
                    viewModel.deleteData(userId, sceneryData!!.scenery_id)
                    showDeleteDialog = false
                }
            }
        }

        ApiStatus.FAILED -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (userId == "") {
                    Image(
                        modifier = modifier
                            .size(173.dp),
                        painter = painterResource(id = R.drawable.state),
                        contentDescription = ""
                    )
                    Text(
                        text = "You haven't logged in yet.",
                        textAlign = TextAlign.Center,
                        color = Color(0xFF29A478)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = {
                            CoroutineScope(Dispatchers.IO).launch { signIn(context, dataStore) }
                        },
                        colors = buttonColors(
                            containerColor = DarkGreen,
                            contentColor = Color.White
                        )
                    ) {
                        Text(text = "Login")
                    }
                } else {
                    Image(
                        modifier = modifier
                            .size(173.dp),
                        painter = painterResource(id = R.drawable.login_state),
                        contentDescription = ""
                    )
                    Text(
                        text = stringResource(id = R.string.error_notification),
                        textAlign = TextAlign.Center,
                        color = Color(0xFF29A478)
                    )
                    Button(
                        onClick = { viewModel.retrieveData(userId) },
                        modifier = Modifier.padding(top = 16.dp),
                        contentPadding = PaddingValues(horizontal = 32.dp, vertical = 16.dp),
                        colors = buttonColors(
                            containerColor = DarkGreen,
                            contentColor = Color.Black
                        )
                    ) {
                        Text(
                            text = stringResource(id = R.string.button_try),
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GridData(scenery: Scenery, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(4.dp)
            .border(1.dp, Color.Green)
            .clickable { onClick() },
        contentAlignment = Alignment.BottomCenter
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(Api.getImageUrl(scenery.image_id))
                .crossfade(true)
                .build(),
            contentDescription = stringResource(R.string.picture, scenery.judul_pemandangan),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(id = R.drawable.loading_img),
            error = painterResource(id = R.drawable.baseline_broken_image_24),
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
                .background(Color(red = 0f, green = 0f, blue = 0f, alpha = 0.5f))
                .padding(4.dp)
        ) {
            Text(
                text = scenery.judul_pemandangan,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFCDF6D1)
            )
            Text(
                text = scenery.lokasi,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                color = Color(0xFFCDF6D1)
            )
            Text(text = scenery.created_at, color =Color(0xFF29A478))
        }
    }
}

private suspend fun signIn(context: Context, dataStore: UserDataStore) {
    val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
        .setFilterByAuthorizedAccounts(false)
        .setServerClientId(BuildConfig.BUILD_TYPE)
        .build()

    val request: GetCredentialRequest = GetCredentialRequest.Builder()
        .addCredentialOption(googleIdOption)
        .build()

    try {
        val credentialManager = CredentialManager.create(context)
        val result = credentialManager.getCredential(context, request)
        handleSignIn(result, dataStore)
    } catch (e: GetCredentialException) {
        Log.e("SIGN-IN", "Error: ${e.errorMessage}")
    }
}

private suspend fun handleSignIn(
    result: GetCredentialResponse,
    dataStore: UserDataStore
) {
    val credential = result.credential
    if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
        try {
            val googleId = GoogleIdTokenCredential.createFrom(credential.data)
            val nama = googleId.displayName ?: "???"
            val email = googleId.id
            val photoUrl = googleId.profilePictureUri.toString()
            dataStore.saveData(User(nama, email, photoUrl))
        } catch (e: GoogleIdTokenParsingException) {
            Log.e("SIGN-IN", "Error: ${e.message}")
        }
    } else {
        Log.e("SIGN-IN", "Error: unrecognized custom credential type.")
    }
}

@Composable
fun ListItem(data: Scenery, onClick: () -> Unit) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(Api.getImageUrl(data.image_id))
                    .crossfade(true)
                    .build(),
                contentDescription = "Image ${data.judul_pemandangan}",
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.loading_img),
                error = painterResource(id = R.drawable.baseline_broken_image_24),
                modifier = Modifier
                    .size(150.dp)
                    .padding(4.dp)
                    .clip(RoundedCornerShape(15.dp))
            )

            IconButton(onClick = { onClick() }) {
                Icon(
                    imageVector = Icons.Filled.Delete, contentDescription = "Delete Icon",
                    tint = Color(0xFF29A478)
                )
            }
        }
        Column(modifier = Modifier.padding(start = 8.dp)) {
            Text(
                text = data.judul_pemandangan,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF29A478)
            )
            Text(
                text = data.lokasi,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = Color(0xFF29A478)
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = data.created_at,
                modifier = Modifier.weight(1f),
                color = Color(0xFF29A478)
            )
            IconButton(
                onClick = {
                    shareData(
                        context = context,
                        message = context.getString(
                            R.string.share_template,
                            data.judul_pemandangan, data.lokasi, Api.getImageUrl(data.image_id)
                        )
                    )
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Share,
                    contentDescription = null,
                    tint = Color(0xFF29A478)
                )
            }
        }
    }
}

private fun shareData(context: Context, message: String) {
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, message)
    }
    if (shareIntent.resolveActivity(context.packageManager) != null) {
        context.startActivity(shareIntent)
    }
}

private suspend fun signOut(context: Context, dataStore: UserDataStore) {
    try {
        val credentialManager = CredentialManager.create(context)
        credentialManager.clearCredentialState(
            ClearCredentialStateRequest()
        )
        dataStore.saveData(User())
    } catch (e: ClearCredentialException) {
        Log.e("SIGN-IN", "Error: ${e.errorMessage}")
    }
}

private fun getCroppedImage(
    resolver: ContentResolver,
    result: CropImageView.CropResult
): Bitmap? {
    if (!result.isSuccessful) {
        Log.e("IMAGE", "Error: ${result.error}")
        return null
    }

    val uri = result.uriContent ?: return null

    return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
        MediaStore.Images.Media.getBitmap(resolver, uri)
    } else {
        val source = ImageDecoder.createSource(resolver, uri)
        ImageDecoder.decodeBitmap(source)
    }
}


@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun AppScreenPreview() {
    Assessment3Theme {
        AppScreen(rememberNavController())
    }
}