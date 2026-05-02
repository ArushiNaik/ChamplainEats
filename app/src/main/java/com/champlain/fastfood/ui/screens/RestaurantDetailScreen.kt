package com.champlain.fastfood.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.champlain.fastfood.R
import com.champlain.fastfood.components.CommentItem
import com.champlain.fastfood.components.FoodItemComponent
import com.champlain.fastfood.models.Comment
import com.champlain.fastfood.models.FoodItem
import com.champlain.fastfood.models.Restaurant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestaurantDetailScreen(
    restaurant: Restaurant,
    menuItems: List<FoodItem>,
    comments: List<Comment>,
    onBackClick: () -> Unit,
    onAddReviewClick: () -> Unit,
    onDeleteComment: (Int) -> Unit,
    onOrderNowClick: () -> Unit
) {
    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onOrderNowClick,
                containerColor = Color(0xFF1C1C1E),
                contentColor = Color.White,
                icon = { Icon(painterResource(id = R.drawable.ic_orders), contentDescription = null) },
                text = { Text("Order Now", fontWeight = FontWeight.Bold) },
                modifier = Modifier.padding(bottom = 16.dp)
            )
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF6F6F6))
                .padding(paddingValues)
        ) {
            // Header with Banner
            item {
                Box(modifier = Modifier.height(200.dp)) {
                    // Gradient Background
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color(0xFFC0392B), Color(0xFFE74C3C))
                                )
                            )
                    )
                    
                    Text(
                        text = restaurant.emoji,
                        fontSize = 72.sp,
                        modifier = Modifier.align(Alignment.Center)
                    )

                    // Top Toolbar Scrim
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .background(Color.Black.copy(alpha = 0.4f))
                    )

                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                }
            }

            // Info Card
            item {
                Card(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = restaurant.name,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1C1C1E)
                        )
                        Text(
                            text = restaurant.category,
                            fontSize = 14.sp,
                            color = Color(0xFF6B6B6B),
                            modifier = Modifier.padding(top = 2.dp)
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 10.dp)
                        ) {
                            Icon(
                                Icons.Default.Star,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                                tint = Color(0xFFFFB300)
                            )
                            Text(
                                text = "%.1f".format(restaurant.rating),
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(start = 4.dp)
                            )
                            Box(
                                modifier = Modifier
                                    .padding(horizontal = 10.dp)
                                    .size(1.dp, 14.dp)
                                    .background(Color(0xFFF0F0F0))
                            )
                            Icon(
                                painterResource(id = R.drawable.ic_clock),
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                                tint = Color(0xFF6B6B6B)
                            )
                            Text(
                                text = "${restaurant.openTime} – ${restaurant.closeTime}",
                                fontSize = 13.sp,
                                color = Color(0xFF6B6B6B),
                                modifier = Modifier.padding(start = 4.dp)
                            )
                        }

                        Text(
                            text = restaurant.description,
                            fontSize = 14.sp,
                            color = Color(0xFF6B6B6B),
                            lineHeight = 20.sp,
                            modifier = Modifier.padding(top = 10.dp)
                        )

                        Row(modifier = Modifier.padding(top = 12.dp)) {
                            if (restaurant.discountPercent > 0) {
                                BadgeChip(
                                    text = "🎓 ${restaurant.discountPercent}% Student Discount",
                                    containerColor = Color(0xFFE8F9F1),
                                    contentColor = Color(0xFF06C167)
                                )
                            }
                            if (restaurant.isCampus) {
                                Spacer(modifier = Modifier.width(8.dp))
                                BadgeChip(
                                    text = "📍 On Campus",
                                    containerColor = Color(0xFFF5F5F5),
                                    contentColor = Color(0xFF1C1C1E)
                                )
                            }
                        }
                    }
                }
            }

            // Menu Section
            item {
                Text(
                    text = "Menu",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1C1C1E),
                    modifier = Modifier.padding(start = 16.dp, top = 20.dp, bottom = 8.dp)
                )
            }

            items(menuItems) { item ->
                Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                    FoodItemComponent(item = item, showQty = false, onQtyChanged = {})
                }
            }

            // Reviews Section Header
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Reviews",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1C1C1E)
                    )
                    Button(
                        onClick = onAddReviewClick,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1C1C1E)),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp),
                        modifier = Modifier.height(38.dp)
                    ) {
                        Text("+ Write Review", fontSize = 13.sp)
                    }
                }
            }

            items(comments) { comment ->
                Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                    CommentItem(comment = comment, onDelete = onDeleteComment)
                }
            }

            item {
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}

@Composable
fun BadgeChip(text: String, containerColor: Color, contentColor: Color) {
    Surface(
        color = containerColor,
        shape = RoundedCornerShape(20.dp)
    ) {
        Text(
            text = text,
            color = contentColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}
