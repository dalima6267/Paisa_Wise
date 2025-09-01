package com.dalima.paisawise.category

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dalima.paisawise.ui.theme.Black
import com.dalima.paisawise.ui.theme.ButtonGreen
import com.dalima.paisawise.ui.theme.DarkerYellow
import com.dalima.paisawise.ui.theme.LightDarkGreen
import com.dalima.paisawise.ui.theme.LighterYellow
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.dalima.paisawise.Enum.Screen
import com.dalima.paisawise.R
import com.dalima.paisawise.viewmodel.CategoryViewModel

data class Category(
    val name: String,
    val tags: List<String>,
    val iconRes: Int
)

@Composable
fun ExpenseCategoryScreen(
    navController: NavController? = null, // nullable so we can call without navigation
    viewModel: CategoryViewModel = viewModel(),
    onCategorySelected: ((Category) -> Unit)? = null // callback when used as a picker
) {
    val selectedTags = viewModel.selectedTags.collectAsState(emptyList())

    val categories = listOf(
        Category("Essentials & Living",
            listOf("Groceries", "Rent", "Utilities", "Internet", "Mobile", "Transportation", "Medical", "EMIs"),
             R.drawable.essentials),
        Category("Food & Dining",
            listOf("Restaurants", "Cafe", "Snacks", "Delivery", "Office Lunch", "Date", "Dinner"),
             R.drawable.food),
        Category("Entertainment & Leisure",
            listOf("Movies", "Events", "Gaming", "Outdoor", "Stand Up", "Subscriptions", "Show"),
            R.drawable.entertainment),
        Category("Shopping",
            listOf("Footwear", "Accessories", "Electronics", "Books", "Pens"),
             R.drawable.shopping),
        Category("Health & Wellness",
            listOf("Gym/Fitness", "Yoga/Meditation", "Medical Bills", "Medicines"),
             R.drawable.health),
        Category("Travel",
            listOf("Flight", "Train", "Hotel", "Stays", "Cabs", "Gifts", "Souvenir"),
            R.drawable.travels),
        Category("Family & Kids",
            listOf("School Fees", "Childcare", "Gifts/Toys", "Family Outings"),
            R.drawable.family),
        Category("Finance & Insurance",
            listOf("Credit Card Payments", "Insurance Premiums", "Investment Contributions", "Savings/FD"),
             R.drawable.finance),
        Category("Gifts & Donations",
            listOf("Festival Gifting", "Charity Donations", "Weddings/Birthdays"),
             R.drawable.gift),
        Category("Home & Maintenance",
            listOf("Repairs", "Furniture", "Cleaning Services"),
          R.drawable.success),
        Category("Work & Business",
            listOf("Office Supplies", "Business Travel", "Client Meetings"),
           R.drawable.work),
        Category("Education & Learning",
            listOf("Courses/Online Classes", "Books/Stationery", "Workshops"),
             R.drawable.education),
        Category("Others/Miscellaneous",
            listOf("Uncategorized", "Tips", "Cash Withdrawals"),
        R.drawable.others)
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 16.dp)
                .padding(bottom = if (onCategorySelected == null) 80.dp else 0.dp) // space for sticky button only if full screen
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                "Select Expense Categories",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            categories.forEach { category ->
                CategorySectionSimple(
                    category = category,
                    selectedTags = selectedTags.value,
                    onTagToggle = viewModel::toggleTag,
                    onCategoryClick = {
                        if (onCategorySelected != null) {
                            onCategorySelected(category) // return selection
                        }
                    }
                )
            }
        }

        // Sticky Finish Button only in full screen mode
        if (onCategorySelected == null && navController != null) {
            Button(
                onClick = { navController.navigate(Screen.Main.name) },
                colors = ButtonDefaults.buttonColors(containerColor = ButtonGreen),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(24.dp)
                    .height(50.dp)
            ) {
                Text(text = "Finish", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CategorySectionSimple(
    category: Category,
    selectedTags: List<String>,
    onTagToggle: (String) -> Unit,
    onCategoryClick: () -> Unit // used for quick selection in dialog mode
) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .background(LightDarkGreen, shape = RoundedCornerShape(50.dp))
                .padding(horizontal = 12.dp, vertical = 6.dp)
                .fillMaxWidth()
                .clickable { onCategoryClick() } // clickable row
        ) {
            Image(
                painter = painterResource(id = category.iconRes),
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = category.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = Black
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            category.tags.forEach { tag ->
                val isSelected = tag in selectedTags
                FilterChip(
                    selected = isSelected,
                    onClick = { onTagToggle(tag) },
                    label = {
                        Text(tag, color = Black, fontWeight = FontWeight.Bold)
                    },
                    shape = RoundedCornerShape(50),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = DarkerYellow,
                        containerColor = LighterYellow
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = isSelected,
                        borderColor = Color.Transparent,
                        selectedBorderColor = Color.Transparent,
                        disabledBorderColor = Color.Transparent
                    )
                )
            }
        }
    }
}
