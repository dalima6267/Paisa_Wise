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
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dalima.paisawise.MainActivity
import com.dalima.paisawise.R
import com.dalima.paisawise.ui.theme.DarkerPuple
import com.dalima.paisawise.viewmodel.CurrencyViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(currencyViewModel: CurrencyViewModel = viewModel()) {
    var expanded by remember { mutableStateOf(false) }
    val currencyMap = currencyViewModel.currencies
    val selectedCode = currencyViewModel.selectedCurrency

    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()
    var showLogoutSheet by remember { mutableStateOf(false) }
    if (showLogoutSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                coroutineScope.launch { sheetState.hide() }
                showLogoutSheet = false
            },
            sheetState = sheetState
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
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Spacer(modifier = Modifier.height(30.dp))
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
                    Text("Dalima Sahu", style = MaterialTheme.typography.h6)
                    Text("dalima8625@gmail.com", style = MaterialTheme.typography.body2)
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
            onClick = { /* Handle edit profile */ }
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
            onClick = {     }
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

        var isDarkMode by remember { mutableStateOf(true) }
        ProfileSwitchItem(
            icon = R.drawable.darkmode,
            title = "Dark Mode",
            isChecked = isDarkMode,
            onCheckedChange = { isDarkMode = it }
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
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                .background(MaterialTheme.colors.surface)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Divider
            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(MaterialTheme.colors.primary.copy(alpha = 0.5f))
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Logout?",
                style = MaterialTheme.typography.h6
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Are you sure you want to logout?",
                style = MaterialTheme.typography.body2
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.LightGray
                    )
                ) {
                    Text("Cancel")
                }

                Button(
                    onClick = onConfirm,
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(0xFFE57373) // Red color
                    )
                ) {
                    Text("Yes, Logout")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen()
}