package com.champlain.fastfood.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.champlain.fastfood.models.*
import java.text.SimpleDateFormat
import java.util.*

class DatabaseHelper private constructor(context: Context) :
    SQLiteOpenHelper(context.applicationContext, DB_NAME, null, DB_VERSION) {

    companion object {
        private const val DB_NAME    = "champlain_eats.db"
        private const val DB_VERSION = 2   // bumped to force re-seed

        private const val T_REST     = "restaurants"
        private const val T_FOOD     = "food_items"
        private const val T_ORDERS   = "orders"
        private const val T_OI       = "order_items"
        private const val T_COMMENTS = "comments"

        @Volatile private var instance: DatabaseHelper? = null
        fun getInstance(ctx: Context) = instance ?: synchronized(this) {
            instance ?: DatabaseHelper(ctx).also { instance = it }
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""CREATE TABLE $T_REST (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            name TEXT NOT NULL, category TEXT, description TEXT,
            open_time TEXT, close_time TEXT, rating REAL DEFAULT 4.0,
            is_campus INTEGER DEFAULT 0, discount_percent INTEGER DEFAULT 0,
            emoji TEXT, delivery_mins INTEGER DEFAULT 25, banner_drawable TEXT)""")

        db.execSQL("""CREATE TABLE $T_FOOD (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            restaurant_id INTEGER NOT NULL, name TEXT NOT NULL,
            description TEXT, price REAL NOT NULL, category TEXT,
            FOREIGN KEY(restaurant_id) REFERENCES $T_REST(id))""")

        db.execSQL("""CREATE TABLE $T_ORDERS (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            restaurant_id INTEGER, restaurant_name TEXT,
            order_type TEXT DEFAULT 'Pickup', status TEXT DEFAULT 'Pending',
            total_price REAL DEFAULT 0, created_at TEXT, notes TEXT, items_summary TEXT)""")

        db.execSQL("""CREATE TABLE $T_OI (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            order_id INTEGER NOT NULL, item_name TEXT NOT NULL,
            quantity INTEGER DEFAULT 1, price REAL NOT NULL,
            FOREIGN KEY(order_id) REFERENCES $T_ORDERS(id))""")

        db.execSQL("""CREATE TABLE $T_COMMENTS (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            restaurant_id INTEGER NOT NULL, user_name TEXT,
            rating INTEGER DEFAULT 5, comment_text TEXT, created_at TEXT,
            FOREIGN KEY(restaurant_id) REFERENCES $T_REST(id))""")

        seedData(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldV: Int, newV: Int) {
        listOf(T_COMMENTS, T_OI, T_ORDERS, T_FOOD, T_REST).forEach {
            db.execSQL("DROP TABLE IF EXISTS $it")
        }
        onCreate(db)
    }

    private fun seedData(db: SQLiteDatabase) {
        fun rest(name:String,cat:String,desc:String,open:String,close:String,
                 rating:Double,campus:Boolean,disc:Int,emoji:String,mins:Int,banner:String) =
            db.insert(T_REST, null, ContentValues().apply {
                put("name",name); put("category",cat); put("description",desc)
                put("open_time",open); put("close_time",close); put("rating",rating)
                put("is_campus",if(campus) 1 else 0); put("discount_percent",disc)
                put("emoji",emoji); put("delivery_mins",mins); put("banner_drawable",banner)
            })

        fun food(rId:Int,name:String,desc:String,price:Double,cat:String) =
            db.insert(T_FOOD, null, ContentValues().apply {
                put("restaurant_id",rId); put("name",name); put("description",desc)
                put("price",price); put("category",cat)
            })

        fun comment(rId:Int,user:String,rating:Int,text:String,date:String) =
            db.insert(T_COMMENTS, null, ContentValues().apply {
                put("restaurant_id",rId); put("user_name",user); put("rating",rating)
                put("comment_text",text); put("created_at",date)
            })

        rest("Campus Café","Café","Your cozy on-campus coffee & snack spot.","7:30","18:00",4.5,true,10,"☕",10,"bg_cafe")
        rest("Burger Bliss","Burgers","Juicy smash burgers made fresh to order.","11:00","21:00",4.3,false,15,"🍔",20,"bg_burger")
        rest("Pita Palace","Mediterranean","Authentic wraps & pitas since 2010.","10:30","20:30",4.6,false,10,"🥙",25,"bg_mediterranean")
        rest("Sushi Zen","Japanese","Fresh sushi & bento boxes daily.","11:30","21:00",4.7,false,0,"🍣",30,"bg_japanese")
        rest("Pizza Pronto","Pizza","Neapolitan-style pizza by the slice.","11:00","22:00",4.4,false,20,"🍕",20,"bg_pizza")
        rest("Taco Loco","Mexican","Street-style tacos & burritos.","10:00","21:30",4.2,false,10,"🌮",15,"bg_mexican")
        rest("Green Bowl","Healthy","Salads, grain bowls & smoothies.","8:00","19:00",4.5,true,15,"🥗",12,"bg_healthy")
        rest("Noodle House","Asian","Ramen, pad thai & stir-fry dishes.","11:00","21:00",4.4,false,10,"🍜",25,"bg_asian")

        food(1,"Espresso","Double shot espresso",2.50,"Drinks")
        food(1,"Latte","Creamy milk & espresso",4.25,"Drinks")
        food(1,"Cappuccino","Equal parts espresso, milk & foam",4.50,"Drinks")
        food(1,"Matcha Latte","Premium matcha with steamed milk",5.25,"Drinks")
        food(1,"Bagel & Cream Cheese","Fresh toasted bagel",4.00,"Food")
        food(1,"Butter Croissant","Buttery flaky croissant",3.50,"Food")
        food(1,"Blueberry Muffin","Freshly baked daily",2.75,"Food")
        food(1,"Granola Bowl","Oats, mixed berries & honey",5.50,"Food")

        food(2,"Classic Smash Burger","Beef patty, American cheese, pickles, special sauce",9.99,"Burgers")
        food(2,"Double Smash","Two smashed patties, caramelized onions",13.49,"Burgers")
        food(2,"BBQ Bacon Burger","Crispy bacon, cheddar, BBQ sauce",12.49,"Burgers")
        food(2,"Veggie Smash","Plant-based patty, avocado, tomato",10.99,"Burgers")
        food(2,"Crispy Chicken Sandwich","Buttermilk fried chicken, coleslaw",11.49,"Burgers")
        food(2,"Seasoned Fries","Crispy golden fries",3.99,"Sides")
        food(2,"Loaded Fries","Fries with cheese sauce & bacon bits",6.99,"Sides")
        food(2,"Chocolate Milkshake","Thick & creamy hand-spun shake",5.99,"Drinks")
        food(2,"Vanilla Milkshake","Thick & creamy hand-spun shake",5.99,"Drinks")

        food(3,"Chicken Shawarma Wrap","Marinated chicken, garlic sauce, veggies",10.99,"Wraps")
        food(3,"Beef Shawarma Wrap","Spiced beef, tahini, pickled veg",11.49,"Wraps")
        food(3,"Falafel Pita","Crispy falafel, tahini, fresh salad",9.49,"Wraps")
        food(3,"Beef Kofta Plate","Grilled kofta, saffron rice, salad",13.99,"Plates")
        food(3,"Mixed Grill Plate","Chicken, beef & kofta with rice",16.99,"Plates")
        food(3,"Hummus & Pita","Creamy hummus with warm pita bread",6.99,"Sides")
        food(3,"Greek Salad","Tomatoes, feta, olives, cucumber",7.99,"Salads")

        food(4,"Salmon Roll (8pcs)","Fresh salmon, avocado, cucumber",12.99,"Rolls")
        food(4,"Spicy Tuna Roll (8pcs)","Spicy tuna, masago, sriracha",13.49,"Rolls")
        food(4,"Dragon Roll (8pcs)","Shrimp tempura, avocado top",15.99,"Rolls")
        food(4,"Rainbow Roll (8pcs)","California with assorted fish top",16.99,"Rolls")
        food(4,"Chirashi Bowl","Assorted sashimi over sushi rice",17.99,"Bowls")
        food(4,"Teriyaki Chicken Bowl","Grilled chicken, teriyaki glaze, rice",13.99,"Bowls")
        food(4,"Edamame","Steamed salted soybeans",4.50,"Sides")
        food(4,"Miso Soup","Traditional miso with tofu & wakame",3.00,"Sides")

        food(5,"Margherita 12\"","San Marzano tomato, fresh mozzarella, basil",13.99,"Pizzas")
        food(5,"Pepperoni 12\"","Double pepperoni, aged mozzarella",14.99,"Pizzas")
        food(5,"BBQ Chicken 12\"","Smoky BBQ, chicken, red onion, cilantro",15.99,"Pizzas")
        food(5,"Veggie Supreme 12\"","Bell peppers, mushrooms, olives, sun-dried tomato",14.49,"Pizzas")
        food(5,"Quattro Formaggi 12\"","Mozzarella, gorgonzola, parmesan, fontina",15.49,"Pizzas")
        food(5,"Garlic Bread","Toasted ciabatta with herb butter",4.99,"Sides")
        food(5,"Tiramisu","Classic Italian dessert",5.99,"Desserts")

        food(6,"Carne Asada Taco","Grilled beef, salsa verde, cilantro",5.49,"Tacos")
        food(6,"Chicken Tinga Taco","Smoky chipotle chicken, avocado",4.99,"Tacos")
        food(6,"Baja Fish Taco","Beer-battered fish, cabbage slaw, lime crema",5.49,"Tacos")
        food(6,"Al Pastor Taco","Marinated pork, pineapple, onion",4.99,"Tacos")
        food(6,"Chicken Burrito Bowl","Rice, black beans, grilled chicken, all toppings",11.99,"Bowls")
        food(6,"Loaded Nachos","Tortilla chips, three cheese, jalapeños, guacamole",9.99,"Sides")
        food(6,"Guacamole & Chips","Fresh avocado guacamole",6.99,"Sides")

        food(7,"Classic Caesar","Romaine, shaved parmesan, croutons, caesar",9.99,"Salads")
        food(7,"Harvest Bowl","Quinoa, roasted sweet potato, kale, tahini",12.49,"Bowls")
        food(7,"Protein Power Bowl","Brown rice, grilled chicken, edamame, avocado",13.99,"Bowls")
        food(7,"Avocado Toast","Sourdough, smashed avocado, everything bagel spice",8.99,"Food")
        food(7,"Acai Smoothie Bowl","Acai, banana, granola, fresh berries",10.99,"Bowls")
        food(7,"Green Smoothie","Spinach, banana, mango, almond milk",6.99,"Drinks")
        food(7,"Berry Blast Smoothie","Mixed berries, Greek yogurt, honey",7.49,"Drinks")

        food(8,"Tonkotsu Ramen","Rich pork broth, chashu, soft egg, nori",14.99,"Ramen")
        food(8,"Spicy Miso Ramen","Miso broth, corn, bamboo shoots, chili oil",13.99,"Ramen")
        food(8,"Shoyu Ramen","Soy-based broth, chicken, menma",12.99,"Ramen")
        food(8,"Pad Thai","Rice noodles, shrimp or tofu, peanuts, lime",13.49,"Noodles")
        food(8,"Kung Pao Noodles","Wok-fried noodles, chicken, peanuts, chili",12.99,"Noodles")
        food(8,"Gyoza (6pcs)","Pan-fried pork & cabbage dumplings",8.99,"Sides")
        food(8,"Spring Rolls (4pcs)","Crispy veggie spring rolls, sweet chili",6.99,"Sides")
        food(8,"Taro Bubble Tea","Creamy taro with tapioca pearls",5.99,"Drinks")
        food(8,"Matcha Bubble Tea","Premium matcha with tapioca pearls",5.99,"Drinks")

        comment(1,"Alex T.",5,"Best coffee on campus! The matcha latte is incredible. Great discount too!","2025-03-10")
        comment(1,"Sara M.",4,"Love the croissants — always fresh. Quick service between classes.","2025-03-15")
        comment(2,"Jordan K.",5,"Double Smash burger changed my life. Absolutely must try!","2025-03-08")
        comment(2,"Priya S.",4,"Great burgers, gets busy at lunch but worth the wait.","2025-03-20")
        comment(3,"Mike R.",5,"Best shawarma in the area. The mixed grill plate is incredible!","2025-03-05")
        comment(4,"Yuki T.",5,"Freshest sushi near campus. Dragon roll is phenomenal!","2025-03-18")
        comment(5,"Emma L.",4,"Quattro Formaggi pizza is amazing. Student discount makes it perfect!","2025-03-12")
        comment(6,"Carlos V.",5,"Al Pastor tacos are authentic and delicious!","2025-03-22")
        comment(7,"Nina P.",5,"Protein Power Bowl is my go-to between classes — so filling!","2025-03-17")
        comment(8,"Kevin C.",5,"Tonkotsu ramen is pure comfort food for cold Montréal days.","2025-03-25")
    }

    private fun rowToRestaurant(db: SQLiteDatabase, selection: String? = null, selArgs: Array<String>? = null): List<Restaurant> {
        val list = mutableListOf<Restaurant>()
        db.query(T_REST, null, selection, selArgs, null, null, "name ASC").use { c ->
            while (c.moveToNext()) list.add(Restaurant(
                id              = c.getInt(c.getColumnIndexOrThrow("id")),
                name            = c.getString(c.getColumnIndexOrThrow("name")),
                category        = c.getString(c.getColumnIndexOrThrow("category")),
                description     = c.getString(c.getColumnIndexOrThrow("description")),
                openTime        = c.getString(c.getColumnIndexOrThrow("open_time")),
                closeTime       = c.getString(c.getColumnIndexOrThrow("close_time")),
                rating          = c.getDouble(c.getColumnIndexOrThrow("rating")),
                isCampus        = c.getInt(c.getColumnIndexOrThrow("is_campus")) == 1,
                discountPercent = c.getInt(c.getColumnIndexOrThrow("discount_percent")),
                emoji           = c.getString(c.getColumnIndexOrThrow("emoji")),
                deliveryMins    = c.getInt(c.getColumnIndexOrThrow("delivery_mins")),
                bannerDrawable  = c.getString(c.getColumnIndexOrThrow("banner_drawable"))
            ))
        }
        return list
    }

    fun getAllRestaurants(): List<Restaurant> = rowToRestaurant(readableDatabase)

    fun searchRestaurants(q: String): List<Restaurant> {
        val like = "%$q%"
        val list = mutableListOf<Restaurant>()
        readableDatabase.rawQuery(
            "SELECT * FROM $T_REST WHERE name LIKE ? OR category LIKE ? ORDER BY name ASC",
            arrayOf(like, like)
        ).use { c ->
            while (c.moveToNext()) list.add(Restaurant(
                id=c.getInt(c.getColumnIndexOrThrow("id")), name=c.getString(c.getColumnIndexOrThrow("name")),
                category=c.getString(c.getColumnIndexOrThrow("category")), description=c.getString(c.getColumnIndexOrThrow("description")),
                openTime=c.getString(c.getColumnIndexOrThrow("open_time")), closeTime=c.getString(c.getColumnIndexOrThrow("close_time")),
                rating=c.getDouble(c.getColumnIndexOrThrow("rating")), isCampus=c.getInt(c.getColumnIndexOrThrow("is_campus"))==1,
                discountPercent=c.getInt(c.getColumnIndexOrThrow("discount_percent")), emoji=c.getString(c.getColumnIndexOrThrow("emoji")),
                deliveryMins=c.getInt(c.getColumnIndexOrThrow("delivery_mins")), bannerDrawable=c.getString(c.getColumnIndexOrThrow("banner_drawable"))
            ))
        }
        return list
    }

    fun getRestaurantsByCategory(cat: String): List<Restaurant> =
        if (cat == "All") getAllRestaurants()
        else rowToRestaurant(readableDatabase, "category=?", arrayOf(cat))

    fun getRestaurantById(id: Int): Restaurant? =
        rowToRestaurant(readableDatabase, "id=?", arrayOf(id.toString())).firstOrNull()

    fun getFoodItemsByRestaurant(rId: Int): List<FoodItem> {
        val list = mutableListOf<FoodItem>()
        readableDatabase.query(T_FOOD, null, "restaurant_id=?", arrayOf(rId.toString()),
            null, null, "category ASC, name ASC").use { c ->
            while (c.moveToNext()) list.add(FoodItem(
                id=c.getInt(c.getColumnIndexOrThrow("id")),
                restaurantId=c.getInt(c.getColumnIndexOrThrow("restaurant_id")),
                name=c.getString(c.getColumnIndexOrThrow("name")),
                description=c.getString(c.getColumnIndexOrThrow("description")),
                price=c.getDouble(c.getColumnIndexOrThrow("price")),
                category=c.getString(c.getColumnIndexOrThrow("category"))
            ))
        }
        return list
    }

    fun insertOrder(order: Order): Long = writableDatabase.insert(T_ORDERS, null, ContentValues().apply {
        put("restaurant_id", order.restaurantId); put("restaurant_name", order.restaurantName)
        put("order_type", order.orderType); put("status", order.status)
        put("total_price", order.totalPrice); put("created_at", now())
        put("notes", order.notes); put("items_summary", order.itemsSummary)
    })

    fun insertOrderItems(orderId: Long, items: List<FoodItem>) {
        items.filter { it.quantity > 0 }.forEach { item ->
            writableDatabase.insert(T_OI, null, ContentValues().apply {
                put("order_id", orderId); put("item_name", item.name)
                put("quantity", item.quantity); put("price", item.price)
            })
        }
    }

    fun getAllOrders(): List<Order> {
        val list = mutableListOf<Order>()
        readableDatabase.query(T_ORDERS,null,null,null,null,null,"id DESC").use { c ->
            while(c.moveToNext()) list.add(Order(
                id=c.getInt(c.getColumnIndexOrThrow("id")),
                restaurantId=c.getInt(c.getColumnIndexOrThrow("restaurant_id")),
                restaurantName=c.getString(c.getColumnIndexOrThrow("restaurant_name")),
                orderType=c.getString(c.getColumnIndexOrThrow("order_type")),
                status=c.getString(c.getColumnIndexOrThrow("status")),
                totalPrice=c.getDouble(c.getColumnIndexOrThrow("total_price")),
                createdAt=c.getString(c.getColumnIndexOrThrow("created_at")) ?: "",
                notes=c.getString(c.getColumnIndexOrThrow("notes")) ?: "",
                itemsSummary=c.getString(c.getColumnIndexOrThrow("items_summary")) ?: ""
            ))
        }
        return list
    }

    fun getOrderById(id: Int): Order? {
        readableDatabase.query(T_ORDERS,null,"id=?", arrayOf(id.toString()),null,null,null).use { c ->
            if(c.moveToFirst()) return Order(
                id=c.getInt(c.getColumnIndexOrThrow("id")),
                restaurantId=c.getInt(c.getColumnIndexOrThrow("restaurant_id")),
                restaurantName=c.getString(c.getColumnIndexOrThrow("restaurant_name")),
                orderType=c.getString(c.getColumnIndexOrThrow("order_type")),
                status=c.getString(c.getColumnIndexOrThrow("status")),
                totalPrice=c.getDouble(c.getColumnIndexOrThrow("total_price")),
                createdAt=c.getString(c.getColumnIndexOrThrow("created_at")) ?: "",
                notes=c.getString(c.getColumnIndexOrThrow("notes")) ?: "",
                itemsSummary=c.getString(c.getColumnIndexOrThrow("items_summary")) ?: ""
            )
        }
        return null
    }

    fun updateOrderStatus(orderId: Int, status: String) =
        writableDatabase.update(T_ORDERS, ContentValues().apply { put("status",status) }, "id=?", arrayOf(orderId.toString()))

    fun updateOrderNotes(orderId: Int, notes: String) =
        writableDatabase.update(T_ORDERS, ContentValues().apply { put("notes",notes) }, "id=?", arrayOf(orderId.toString()))

    fun deleteOrder(orderId: Int) {
        writableDatabase.delete(T_OI,"order_id=?", arrayOf(orderId.toString()))
        writableDatabase.delete(T_ORDERS,"id=?", arrayOf(orderId.toString()))
    }

    fun getOrderItems(orderId: Int): List<OrderItem> {
        val list = mutableListOf<OrderItem>()
        readableDatabase.query(T_OI,null,"order_id=?", arrayOf(orderId.toString()),null,null,null).use { c ->
            while(c.moveToNext()) list.add(OrderItem(
                id=c.getInt(c.getColumnIndexOrThrow("id")),
                orderId=c.getInt(c.getColumnIndexOrThrow("order_id")),
                itemName=c.getString(c.getColumnIndexOrThrow("item_name")),
                quantity=c.getInt(c.getColumnIndexOrThrow("quantity")),
                price=c.getDouble(c.getColumnIndexOrThrow("price"))
            ))
        }
        return list
    }

    fun getOrderCount(): Int {
        readableDatabase.rawQuery("SELECT COUNT(*) FROM $T_ORDERS",null).use { c ->
            if(c.moveToFirst()) return c.getInt(0)
        }
        return 0
    }

    fun getTotalSaved(): Double {
        var saved = 0.0
        readableDatabase.rawQuery(
            "SELECT o.total_price, r.discount_percent FROM $T_ORDERS o LEFT JOIN $T_REST r ON o.restaurant_id=r.id",null
        ).use { c ->
            while(c.moveToNext()) { val d=c.getInt(1); if(d>0) saved+=c.getDouble(0)*d/100.0 }
        }
        return saved
    }

    fun insertComment(comment: Comment): Long = writableDatabase.insert(T_COMMENTS,null, ContentValues().apply {
        put("restaurant_id",comment.restaurantId); put("user_name",comment.userName)
        put("rating",comment.rating); put("comment_text",comment.commentText)
        put("created_at", now().substring(0,10))
    })

    fun deleteComment(id: Int) = writableDatabase.delete(T_COMMENTS,"id=?", arrayOf(id.toString()))

    fun getCommentsByRestaurant(rId: Int): List<Comment> {
        val list = mutableListOf<Comment>()
        readableDatabase.query(T_COMMENTS,null,"restaurant_id=?", arrayOf(rId.toString()),null,null,"id DESC").use { c ->
            while(c.moveToNext()) list.add(Comment(
                id=c.getInt(c.getColumnIndexOrThrow("id")),
                restaurantId=c.getInt(c.getColumnIndexOrThrow("restaurant_id")),
                userName=c.getString(c.getColumnIndexOrThrow("user_name")),
                rating=c.getInt(c.getColumnIndexOrThrow("rating")),
                commentText=c.getString(c.getColumnIndexOrThrow("comment_text")),
                createdAt=c.getString(c.getColumnIndexOrThrow("created_at"))
            ))
        }
        return list
    }

    private fun now() = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())
}
