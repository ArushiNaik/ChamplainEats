package com.champlain.fastfood

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.champlain.fastfood.activities.RestaurantDetailActivity
import com.champlain.fastfood.database.DatabaseHelper
import com.champlain.fastfood.ui.screens.*
import com.champlain.fastfood.ui.theme.ChamplainEatsTheme
import com.champlain.fastfood.models.Restaurant

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = DatabaseHelper.getInstance(this)

        setContent {
            ChamplainEatsTheme {
                MainScreen(db)
            }
        }
    }
}

sealed class Screen(val route: String, val title: String, val icon: Int) {
    object Home : Screen("home", "Home", R.drawable.ic_home)
    object Restaurants : Screen("restaurants", "Restaurants", R.drawable.ic_restaurant)
    object Orders : Screen("orders", "Orders", R.drawable.ic_orders)
    object Profile : Screen("profile", "Profile", R.drawable.ic_profile)
}

@Composable
fun MainScreen(db: DatabaseHelper) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val items = listOf(Screen.Home, Screen.Restaurants, Screen.Orders, Screen.Profile)

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                items.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(painterResource(id = screen.icon), contentDescription = null) },
                        label = { Text(screen.title) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color(0xFF1C1C1E),
                            selectedTextColor = Color(0xFF1C1C1E),
                            unselectedIconColor = Color(0xFFADADAD),
                            unselectedTextColor = Color(0xFFADADAD),
                            indicatorColor = Color(0xFFE8F9F1)
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(navController, startDestination = Screen.Home.route, Modifier.padding(innerPadding)) {
            composable(Screen.Home.route) {
                val categories = listOf("All", "Café", "Burgers", "Pizza", "Japanese", "Mexican", "Mediterranean", "Healthy", "Asian")
                var selectedCategory by remember { mutableStateOf("All") }
                val restaurantsState = remember { mutableStateListOf<Restaurant>() }
                
                LaunchedEffect(selectedCategory) {
                    restaurantsState.clear()
                    restaurantsState.addAll(db.getRestaurantsByCategory(selectedCategory))
                }

                HomeScreen(
                    restaurants = restaurantsState,
                    categories = categories,
                    selectedCategory = selectedCategory,
                    onCategorySelected = { selectedCategory = it },
                    onSearch = { query ->
                        restaurantsState.clear()
                        restaurantsState.addAll(
                            if (query.isEmpty()) db.getRestaurantsByCategory(selectedCategory)
                            else db.searchRestaurants(query)
                        )
                    },
                    onRestaurantClick = { restaurant ->
                        context.startActivity(
                            Intent(context, RestaurantDetailActivity::class.java)
                                .putExtra("restaurant_id", restaurant.id)
                        )
                    }
                )
            }
            composable(Screen.Restaurants.route) {
                val restaurantsState = remember { mutableStateListOf<Restaurant>() }
                LaunchedEffect(Unit) {
                    restaurantsState.addAll(db.getAllRestaurants())
                }

                AllRestaurantsScreen(
                    restaurants = restaurantsState,
                    onSearch = { query ->
                        restaurantsState.clear()
                        restaurantsState.addAll(
                            if (query.isEmpty()) db.getAllRestaurants()
                            else db.searchRestaurants(query)
                        )
                    },
                    onRestaurantClick = { restaurant ->
                        context.startActivity(
                            Intent(context, RestaurantDetailActivity::class.java)
                                .putExtra("restaurant_id", restaurant.id)
                        )
                    }
                )
            }
            composable(Screen.Orders.route) {
                val orders = remember { mutableStateListOf<com.champlain.fastfood.models.Order>() }
                LaunchedEffect(Unit) {
                    orders.addAll(db.getAllOrders())
                }
                MyOrdersScreen(
                    orders = orders,
                    onOrderClick = { order ->
                        context.startActivity(Intent(context, com.champlain.fastfood.activities.OrderDetailActivity::class.java)
                            .putExtra("order_id", order.id))
                    },
                    onNewOrderClick = {
                        // In a full compose app, we'd navigate to a restaurant list or selection screen
                        navController.navigate(Screen.Restaurants.route)
                    }
                )
            }
            composable(Screen.Profile.route) {
                ProfileScreen(
                    orderCount = db.getOrderCount(),
                    totalSaved = db.getTotalSaved()
                )
            }
        }
    }
}
