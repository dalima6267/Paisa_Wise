package com.dalima.paisawise

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.dalima.paisawise.ui.theme.DarkerPuple

@Composable
fun ProfileScreen() {
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
            value = "₹ INR",
            showArrow = true,
            onClick = { /* Handle currency click */ }
        )
        ProfileItem(
            icon = R.drawable.export,
            title = "Export Data",
            value = "",
            showArrow = true,
            onClick = { /* Handle currency click */ }
        )
        ProfileItem(
            icon = R.drawable.helpcentre,
            title = "Help Centre",
            value = "",
            showArrow = true,
            onClick = { /* Handle currency click */ }
        )
        ProfileItem(
            icon = R.drawable.contact,
            title = "Contact",
            value = "",
            showArrow = true,
            onClick = { /* Handle currency click */ }
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
            onClick = { /* Handle currency click */ }
        )
    }
}
