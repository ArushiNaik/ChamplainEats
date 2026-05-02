package com.champlain.fastfood.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.champlain.fastfood.models.Restaurant

@Composable
fun RestaurantItem(
    restaurant: Restaurant,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            // Banner Area
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
            ) {
                // Gradient Background (simplified)
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color(0xFFC0392B), Color(0xFFE74C3C)) // Default red gradient
                            )
                        )
                )

                Text(
                    text = restaurant.emoji,
                    fontSize = 56.sp,
                    modifier = Modifier.align(Alignment.Center)
                )

                // Rating Chip
                Surface(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(10.dp),
                    color = Color(0x99000000),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(
                        text = "%.1f ★".format(restaurant.rating),
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                if (restaurant.discountPercent > 0) {
                    Surface(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(10.dp),
                        color = Color(0xFFE8F9F1), // green_light
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text(
                            text = "${restaurant.discountPercent}% off",
                            color = Color(0xFF06C167), // green
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                if (restaurant.isCampus) {
                    Surface(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(10.dp),
                        color = Color.White,
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text(
                            text = "📍 On Campus",
                            color = Color(0xFF1C1C1E),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            // Info Section
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = restaurant.name,
                    color = Color(0xFF1C1C1E),
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${restaurant.category}  •  ${restaurant.openTime}–${restaurant.closeTime}",
                    color = Color(0xFF6B6B6B),
                    fontSize = 13.sp,
                    modifier = Modifier.padding(top = 3.dp)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Icon(
                        painter = painterResource(id = android.R.drawable.ic_menu_recent_history), // Placeholder
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = Color(0xFF6B6B6B)
                    )
                    Text(
                        text = "${restaurant.deliveryMins}–${restaurant.deliveryMins + 10} min",
                        color = Color(0xFF6B6B6B),
                        fontSize = 13.sp,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "Free delivery",
                        color = Color(0xFF06C167),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
