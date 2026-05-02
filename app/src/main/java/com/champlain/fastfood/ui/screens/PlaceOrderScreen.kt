package com.champlain.fastfood.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.champlain.fastfood.components.FoodItemComponent
import com.champlain.fastfood.models.FoodItem
import com.champlain.fastfood.models.Order

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceOrderScreen(
    restaurantName: String,
    foodItems: List<FoodItem>,
    discountPercent: Int,
    onBackClick: () -> Unit,
    onPlaceOrderClick: (String, String) -> Unit // orderType, notes
) {
    var orderType by remember { mutableStateOf(Order.TYPE_PICKUP) }
    var notes by remember { mutableStateOf("") }
    
    // Trigger subtotal update when quantities change
    var subtotal by remember { mutableDoubleStateOf(foodItems.sumOf { it.price * it.quantity }) }

    val discount = subtotal * discountPercent / 100.0
    val total = subtotal - discount

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(restaurantName, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF1C1C1E))
            )
        },
        bottomBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shadowElevation = 8.dp,
                color = Color.White
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Subtotal", color = Color(0xFF6B6B6B), fontSize = 14.sp)
                        Text("$%.2f".format(subtotal), color = Color(0xFF1C1C1E), fontSize = 14.sp)
                    }
                    if (discountPercent > 0) {
                        Row(modifier = Modifier.fillMaxWidth().padding(top = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Student Discount ($discountPercent%)", color = Color(0xFF06C167), fontSize = 14.sp)
                            Text("-$%.2f".format(discount), color = Color(0xFF06C167), fontSize = 14.sp)
                        }
                    }
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFF0F0F0))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Total", color = Color(0xFF6B6B6B), fontSize = 12.sp)
                            Text("$%.2f".format(total), color = Color(0xFF1C1C1E), fontSize = 26.sp, fontWeight = FontWeight.Bold)
                        }
                        Button(
                            onClick = { onPlaceOrderClick(orderType, notes) },
                            modifier = Modifier.height(52.dp).padding(start = 16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1C1C1E)),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Place Order 🎉", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF6F6F6))
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Order Type Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Order Type", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1C1C1E))
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = orderType == Order.TYPE_PICKUP,
                                onClick = { orderType = Order.TYPE_PICKUP },
                                colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF1C1C1E))
                            )
                            Text("🛍️  Pickup", modifier = Modifier.padding(end = 16.dp), fontSize = 15.sp)
                            
                            RadioButton(
                                selected = orderType == Order.TYPE_DELIVERY,
                                onClick = { orderType = Order.TYPE_DELIVERY },
                                colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF1C1C1E))
                            )
                            Text("🛵  Delivery", fontSize = 15.sp)
                        }
                    }
                }
            }

            // Items Section
            item {
                Text("Select Items", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1C1C1E))
            }

            items(foodItems) { item ->
                FoodItemComponent(
                    item = item,
                    showQty = true,
                    onQtyChanged = {
                        subtotal = foodItems.sumOf { it.price * it.quantity }
                    }
                )
            }

            // Notes Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("📝  Special Instructions", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1C1C1E))
                        Spacer(modifier = Modifier.height(8.dp))
                        TextField(
                            value = notes,
                            onValueChange = { notes = it },
                            modifier = Modifier.fillMaxWidth().height(100.dp),
                            placeholder = { Text("Any allergies or special requests?", fontSize = 14.sp) },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color(0xFFF6F6F6),
                                unfocusedContainerColor = Color(0xFFF6F6F6),
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                            shape = RoundedCornerShape(8.dp)
                        )
                    }
                }
            }
        }
    }
}
