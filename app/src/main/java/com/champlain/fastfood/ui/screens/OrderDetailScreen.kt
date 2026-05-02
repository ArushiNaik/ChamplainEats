package com.champlain.fastfood.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.champlain.fastfood.models.Order
import com.champlain.fastfood.models.OrderItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDetailScreen(
    order: Order,
    items: List<OrderItem>,
    onBackClick: () -> Unit,
    onUpdateStatusClick: () -> Unit,
    onEditNotesClick: () -> Unit,
    onCancelOrderClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Order #${order.id}", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF1C1C1E))
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF6F6F6))
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Summary Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = order.restaurantName,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1C1C1E)
                    )
                    
                    val typeText = if (order.orderType == Order.TYPE_DELIVERY) "🛵 Delivery" else "🛍️ Pickup"
                    Text(text = typeText, color = Color(0xFF6B6B6B), fontSize = 14.sp, modifier = Modifier.padding(top = 4.dp))
                    Text(text = order.createdAt, color = Color(0xFFADADAD), fontSize = 13.sp, modifier = Modifier.padding(top = 2.dp))

                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFF0F0F0))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Status", fontWeight = FontWeight.SemiBold, color = Color(0xFF6B6B6B))
                        
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

                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Total Amount", fontWeight = FontWeight.SemiBold, color = Color(0xFF6B6B6B))
                        Text(text = "$%.2f".format(order.totalPrice), fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF1C1C1E))
                    }
                }
            }

            // Items Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(text = "Ordered Items", fontWeight = FontWeight.Bold, fontSize = 16.sp, modifier = Modifier.padding(bottom = 12.dp))
                    
                    items.forEach { item ->
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "• ${item.quantity}x ${item.itemName}", color = Color(0xFF1C1C1E), fontSize = 14.sp)
                            Text(text = "$%.2f".format(item.price * item.quantity), color = Color(0xFF1C1C1E), fontSize = 14.sp)
                        }
                    }
                }
            }

            // Notes Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(text = "Special Instructions", fontWeight = FontWeight.Bold, fontSize = 16.sp, modifier = Modifier.padding(bottom = 8.dp))
                    Text(
                        text = order.notes.ifEmpty { "No special instructions" },
                        color = if (order.notes.isEmpty()) Color(0xFFADADAD) else Color(0xFF6B6B6B),
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )
                }
            }

            // Actions
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (order.status != Order.STATUS_CANCELLED && order.status != Order.STATUS_DELIVERED) {
                    Button(
                        onClick = onUpdateStatusClick,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1C1C1E)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Update Status")
                    }
                    
                    OutlinedButton(
                        onClick = onEditNotesClick,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF1C1C1E))
                    ) {
                        Text("Edit Instructions")
                    }

                    TextButton(
                        onClick = onCancelOrderClick,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFFEF4444))
                    ) {
                        Text("Cancel Order")
                    }
                }
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
