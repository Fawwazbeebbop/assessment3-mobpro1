package org.d3if3051.assessment3.screen

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.d3if3051.assessment3.R
import org.d3if3051.assessment3.navigation.ScreenNav
import org.d3if3051.assessment3.ui.theme.Assessment3Theme
import org.d3if3051.assessment3.ui.theme.DarkGreen


@Composable
fun WelcomeScreen(navController: NavHostController){
    Scaffold(containerColor = Color(0xFFCDF6D1)){
        ScreenContent(modifier = Modifier.padding(it), navController = navController)
    }
}

@Composable
fun ScreenContent(modifier: Modifier, navController: NavHostController){
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    )    {
        Image(
            modifier = modifier
                .size(173.dp),
            painter = painterResource(id = R.drawable.landing),
            contentDescription = ""
        )
        Text(
            text = stringResource(id = (R.string.introduction)),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black,
            fontSize = 14.sp
        )
        Button(
            modifier = modifier
                .padding(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = DarkGreen,
                contentColor = Color.Black
            ),
            onClick = { navController.navigate(ScreenNav.App.route) }
        ) {
            Text(
                text = stringResource(R.string.getting_started),
                color = Color.White,
                fontSize = 16.sp
            )
        }
    }
}



@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun WelcomeScreenPreview() {
    Assessment3Theme {
        WelcomeScreen(rememberNavController())
    }
}