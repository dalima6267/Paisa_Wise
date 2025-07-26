package com.dalima.paisawise.signinprocess

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Observer
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.dalima.paisawise.Enum.Screen
import com.dalima.paisawise.R
import com.dalima.paisawise.ui.theme.ButtonGreen
import com.dalima.paisawise.ui.theme.LightGreen
import com.dalima.paisawise.viewmodel.AuthViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider

@Composable
fun SignUpScreen(    onSwitchClick: () -> Unit,
                     isChecked: Boolean,
                     onCheckedChange: (Boolean) -> Unit,
                     onPrivacyPolicyClick: () -> Unit,
                     navController: NavController,
                     viewModel: AuthViewModel = viewModel()){

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            viewModel.signInWithGoogle(credential)
        } catch (e: ApiException) {
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    DisposableEffect(lifecycleOwner) {
        val observer = Observer<Result<Unit>> { result ->
            result?.let {
                if (it.isSuccess) {
                    navController.navigate(Screen.ExpenseCategory.name)
                } else {
                    Toast.makeText(
                        context,
                        it.exceptionOrNull()?.message ?: "Error",
                        Toast.LENGTH_LONG
                    ).show()
                }
                viewModel.clearStatus()
            }
        }

        viewModel.authStatus.observe(lifecycleOwner, observer as Observer<in Result<Unit>?>)

        onDispose {
            viewModel.authStatus.removeObserver(observer as Observer<in Result<Unit>?>)
        }
    }
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(120.dp))

        Text(
            text = "Create your account",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Spacer(modifier=Modifier.height(35.dp))

        // EMAIL FIELD
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 70.dp)
        ) {
            Text(
                text = "Name",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                placeholder = { Text("Enter your name") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(Color.White, shape = RoundedCornerShape(8.dp)),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = Color.Transparent,
                    disabledBorderColor = Color.Transparent
                )
            )
        }

// PASSWORD FIELD
        Spacer(modifier = Modifier.height(12.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 70.dp)
        ) {
            Text(
                text = "Email",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text("ex: jon.smith@email.com")},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(Color.White, shape = RoundedCornerShape(8.dp)),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = Color.Transparent,
                    disabledBorderColor = Color.Transparent
                )
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 70.dp)
        ) {
            Text(
                text = "Password",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text(".....") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(Color.White, shape = RoundedCornerShape(8.dp)),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = Color.Transparent,
                    disabledBorderColor = Color.Transparent
                )
            )
        }


        Spacer(modifier = Modifier.height(10.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(start = 60.dp)
                .fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clickable { onCheckedChange(!isChecked) }
                    .background(
                        color = if (isChecked) Color.Green else Color.Transparent,
                        shape = RoundedCornerShape(4.dp)
                    )
                    .border(width = 2.dp, color = LightGreen, shape = RoundedCornerShape(4.dp)),
                contentAlignment = Alignment.Center
            ) {
                if (isChecked) {
                    Text("✓", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
            }

            // Privacy Policy Text with clickable part
            Text(
                buildAnnotatedString {
                    withStyle(style = SpanStyle(color = Color.Black)) {
                        append("I understand the ")
                    }
                    // Second part in LightGreen, bold, and underlined
                    withStyle(
                        style = SpanStyle(
                            color = LightGreen,

                        )
                    ) {
                        append("terms & policy")
                    }
                    withStyle(style = SpanStyle(color = Color.Black)) {
                        append(".")
                    }
                },
                modifier = Modifier
                    .clickable { onPrivacyPolicyClick() }
                    .padding(start = 3.dp)
            )
        }
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 70.dp),

            onClick = {
                viewModel.signUp(name, email, password) },
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = ButtonGreen,
                contentColor = Color.White
            )
        ) {
            Text("SIGN IN", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(18.dp))
        Text(
            text="or sign up with",
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            SocialLoginButton(R.drawable.ic_google) {
                val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(context.getString(R.string.web_client_id))
                    .requestEmail()
                    .build()

                val googleSignInClient = GoogleSignIn.getClient(context, gso)
                val signInIntent = googleSignInClient.signInIntent
                launcher.launch(signInIntent)
            }
            SocialLoginButton(R.drawable.ic_facebook) {
                val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(context.getString(R.string.web_client_id))
                    .requestEmail()
                    .build()

                val googleSignInClient = GoogleSignIn.getClient(context, gso)
                val signInIntent = googleSignInClient.signInIntent
                launcher.launch(signInIntent)
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        ClickableText(
            text = buildAnnotatedString {
                // First part in gray
                withStyle(style = SpanStyle(color = Color.Gray)) {
                    append("Have an account? ")
                }
                // Second part in LightGreen, bold, and underlined
                withStyle(
                    style = SpanStyle(
                        color = LightGreen,
                        textDecoration = TextDecoration.Underline
                    )
                ) {
                    append("SIGN IN")
                }
            },
            onClick = { onSwitchClick() }
        )

    }
}
//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun SignUpScreenPreview() {
//    SignUpScreen(
//        onSwitchClick = {},
//        isChecked = false,
//        onCheckedChange = {},
//        onPrivacyPolicyClick = {}
//    )
//}

