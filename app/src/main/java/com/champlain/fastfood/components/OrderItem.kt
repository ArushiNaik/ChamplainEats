package com.champlain.fastfood.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.champlain.fastfood.models.Order

@Composable
fun OrderItem(
    order: Order,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 14.dp, bottom = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = order.restaurantName,
                    modifier = Modifier.weight(1f),
                    color = Color(0xFF1C1C1E),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                
                val (bgColor, fgColor) = getStatusColors(order.status)
                Surface(
                    color = bgColor,
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = order.status,
                        color = fgColor,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            Text(
                text = order.itemsSummary,
                color = Color(0xFF6B6B6B),
                fontSize = 13.sp,
                modifier = Modifier.padding(horizontal = 16.dp),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            HorizontalDivider(
                modifier = Modifier.padding(top = 10.dp),
                thickness = 1.dp,
                color = Color(0xFFF0F0F0)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 10.dp, bottom = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (order.orderType == Order.TYPE_DELIVERY) "🛵 Delivery" else "🛍️ Pickup",
                    color = Color(0xFF6B6B6B),
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = order.createdAt,
                    color = Color(0xFFADADAD),
                    fontSize = 12.sp,
                    modifier = Modifier.padding(end = 12.dp)
                )
                Text(
                    text = "$%.2f".format(order.totalPrice),
                    color = Color(0xFF1C1C1E),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

private fun getStatusColors(status: String): Pair<Color, Color> {
    return when (status) {
        Order.STATUS_CONFIRMED -> Color(0xFFE3F2FD) to Color(0xFF1E88E5)
        Order.STATUS_PREPARING -> Color(0xFFFFF3E0) to Color(0xFFF97316)
        Order.STATUS_READY,
        Order.STATUS_DELIVERED -> Color(0xFFE8F9F1) to Color(0xFF06C167)
        Order.STATUS_CANCELLED -> Color(0xFFFEE2E2) to Color(0xFFEF4444)
        else -> Color(0xFFF5F5F5) to Color(0xFF6B6B6B)
    }
}
