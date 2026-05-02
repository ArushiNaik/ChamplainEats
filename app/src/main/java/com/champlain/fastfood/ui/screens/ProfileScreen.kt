package com.champlain.fastfood.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ProfileScreen(
    orderCount: Int,
    totalSaved: Double
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F6F6)) // background color from XML
            .verticalScroll(rememberScrollState())
    ) {
        // Dark header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF1C1C1E)) // primary color
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE8F9F1)), // green_light
                contentAlignment = Alignment.Center
            ) {
                Text(text = "👤", fontSize = 38.sp)
            }
            
            Text(
                text = "Champlain Student",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 14.dp)
            )
            
            Text(
                text = "student@champlaincollege.qc.ca",
                color = Color.White.copy(alpha = 0.6f),
                fontSize = 13.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
            
            Text(
                text = "📍 Champlain College, Saint-Lambert",
                color = Color(0xFF06C167), // accent / green
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        // Stats Row
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            StatCard(
                label = "Orders",
                value = orderCount.toString(),
                valueColor = Color(0xFF1C1C1E),
                modifier = Modifier.weight(1f)
            )
            StatCard(
                label = "Saved",
                value = "$%.2f".format(totalSaved),
                valueColor = Color(0xFF06C167),
                modifier = Modifier.weight(1f)
            )
        }

        // Development Team card
        Card(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(bottom = 24.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "Development Team",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1C1C1E),
                    modifier = Modifier.padding(bottom = 14.dp)
                )
                
                TeamMemberItem("C", "Chin-Yi Chiu", "Frontend & UI")
                TeamMemberItem("A", "Arushi Naik", "Navigation & State")
                TeamMemberItem("M", "Marwa Meskine", "Backend")
                TeamMemberItem("K", "Kiana Heidarpourmaleki", "Database")
            }
        }
    }
}

@Composable
fun StatCard(
    label: String,
    value: String,
    valueColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                color = valueColor,
                fontSize = if (value.length > 4) 28.sp else 36.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = label,
                color = Color(0xFF6B6B6B),
                fontSize = 13.sp
            )
        }
    }
}

@Composable
fun TeamMemberItem(
    initial: String,
    name: String,
    role: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 6.dp)
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(Color(0xFF1C1C1E)),
            contentAlignment = Alignment.Center
        ) {
            Text(text = initial, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }
        
        Column(modifier = Modifier.padding(start = 12.dp)) {
            Text(text = name, color = Color(0xFF1C1C1E), fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Text(text = role, color = Color(0xFF6B6B6B), fontSize = 12.sp)
        }
    }
}
