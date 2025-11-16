package com.dalima.paisawise.profileScreen

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.dalima.paisawise.MainActivity
import com.dalima.paisawise.R
import com.dalima.paisawise.ui.theme.DarkerPuple
import com.dalima.paisawise.viewmodel.AuthViewModel
import com.dalima.paisawise.viewmodel.CurrencyViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun ProfileScreen(  isDarkMode: Boolean,
                    onDarkModeChange: (Boolean) -> Unit,
                    currencyViewModel: CurrencyViewModel = viewModel(),
                    authViewModel: AuthViewModel = viewModel()
) {
  val userProfile by authViewModel.userProfile.observeAsState()
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        authViewModel.fetchUserProfile()
    }

    val userName = userProfile?.name ?: "User Name"
    val userEmail = userProfile?.email ?: "Email Not Found"


    var expanded by remember { mutableStateOf(false) }
    val currencyMap = currencyViewModel.currencies
    val selectedCode = currencyViewModel.selectedCurrency


    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()
    var showLogoutSheet by remember { mutableStateOf(false) }
    var showExportSheet by remember { mutableStateOf(false) }

    if (showLogoutSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                coroutineScope.launch { sheetState.hide() }
                showLogoutSheet = false
            },
            sheetState = sheetState,
            containerColor = Color(0xFFFDF1E7),

            dragHandle = {
                Box(
                    modifier = Modifier
                        .padding(top = 8.dp, bottom = 8.dp)
                        .width(40.dp)
                        .height(4.dp)
                        .clip(RoundedCornerShape(50))
                        .background(Color(0xFFD0AEEB))

                )
            }
        ) {
            LogoutBottomSheet(
                onDismiss = {
                    coroutineScope.launch { sheetState.hide() }
                    showLogoutSheet = false
                },
                onConfirm = {
                    coroutineScope.launch { sheetState.hide() }
                    showLogoutSheet = false
                    val intent = Intent(context, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    context.startActivity(intent)
                }
            )
        }
    }
    if(showExportSheet){
        ModalBottomSheet(
            onDismissRequest={
                coroutineScope.launch { sheetState.hide() }
                showExportSheet = false
            },
            sheetState = sheetState,
            containerColor = Color(0xFFFDF1E7),
            dragHandle = {
                Box(
                    modifier = Modifier
                        .padding(top = 8.dp, bottom = 8.dp)
                        .width(40.dp)
                        .height(4.dp)
                        .clip(RoundedCornerShape(50))
                        .background(Color(0xFFD0AEEB))
                )
            }
        ){
            ExportBottomSheet(
                onDismiss = {
                    coroutineScope.launch { sheetState.hide() }
                    showExportSheet = false
                },
                onExport = { selectedOption ->
                    coroutineScope.launch { sheetState.hide() }
                    showExportSheet = false

                    when (selectedOption) {
                        "CSV" -> {
                            println("Exporting as CSV...")
                        }
                        "PDF" -> {
                            println("Exporting as PDF...")
                        }
                        "Excel" -> {
                            println("Exporting as Excel...")
                        }
                    }
                }
            )
        }

    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(R.drawable.profilelast),
                    contentDescription = "Profile",
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .border(2.dp, DarkerPuple, CircleShape)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(text = userName, style = MaterialTheme.typography.h6)
                    Text(text = userEmail, style = MaterialTheme.typography.body2)
                }
            }
            IconButton(onClick = { /* Navigate to Edit Profile */ }) {
                Icon(
                    painter = painterResource(R.drawable.edit),
                    contentDescription = "Edit Profile",
                    modifier = Modifier.size(30.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        ProfileItem(
            icon = R.drawable.editprofile,
            title = "Edit Profile",
            value = "",
            showArrow = true,
            onClick = {

            }
        )

        ProfileItem(
            icon = R.drawable.currency,
            title = "Currency",
            value = "$selectedCode - ${currencyMap[selectedCode] ?: ""}",
            showArrow = true,
            onClick = { expanded = true }
        )
//        DropdownMenu(
//            expanded = expanded,
//            onDismissRequest = { expanded = false }
//        ) {
//            currencyMap.forEach { (code, description) ->
//                DropdownMenuItem(
//                    onClick = {
//                        currencyViewModel.selectCurrency(code)
//                        expanded = false
//                    }
//                ) {
//                    Text(text = "$code - $description")
//                }
//            }
//        }
        ProfileItem(
            icon = R.drawable.export,
            title = "Export Data",
            value = "",
            showArrow = true,
            onClick = {
                showExportSheet = true
                coroutineScope.launch { sheetState.show() }
            }
        )
        ProfileItem(
            icon = R.drawable.helpcentre,
            title = "Help Centre",
            value = "",
            showArrow = true,
            onClick = {

                val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:support@paisawise.com")
                    putExtra(Intent.EXTRA_SUBJECT, "Help Needed - PaisaWise App")
                }
                context.startActivity(emailIntent)
            }
        )
        ProfileItem(
            icon = R.drawable.contact,
            title = "Contact",
            value = "",
            showArrow = true,
            onClick = {   val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:support@paisawise.com") // Replace with your actual support email
                putExtra(Intent.EXTRA_SUBJECT, "Support Needed - PaisaWise App")
                putExtra(Intent.EXTRA_TEXT, "Hello team,\n\nI need help with...")
            }

                try {
                    context.startActivity(emailIntent)
                } catch (e: Exception) {
                    Toast.makeText(context, "No email app found", Toast.LENGTH_SHORT).show()
                } }
        )

        ProfileSwitchItem(
            icon = R.drawable.darkmode,
            title = "Dark Mode",
            isChecked = isDarkMode,
            onCheckedChange = { onDarkModeChange(it) }
        )

        var isPinEnabled by remember { mutableStateOf(true) }
        ProfileSwitchItem(
            icon = R.drawable.enablepin,
            title = "Enable Pin",
            isChecked = isPinEnabled,
            onCheckedChange = { isPinEnabled = it }
        )

        ProfileItem(
            icon = R.drawable.logout,
            title = "Log out",
            value = "",
            showArrow = true,
            onClick = {         showLogoutSheet = true
                coroutineScope.launch { sheetState.show() }}
        )

    }}

@Composable
fun LogoutBottomSheet(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Title
        Text(
            text = "Logout?",
            style = MaterialTheme.typography.h6.copy(
                color = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Subtitle
        Text(
            text = "Are you sure do you wanna logout?",
            style = MaterialTheme.typography.body2.copy(
                color = Color.Gray
            )
        )

        Spacer(modifier = Modifier.height(28.dp))

        // Buttons Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // Cancel Button
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xFFD6D6D6) // gray
                ),
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
            ) {
                Text("Cancel", color = Color.Black)
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Confirm Button
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xFFE57373)
                ),
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
            ) {
                Text("Yes, Logout", color = Color.White)
            }
        }
    }
}