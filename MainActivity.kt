package com.quickroute.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private val Green = androidx.compose.ui.graphics.Color(0xFF087F5B)
private data class Food(val name: String, val detail: String, val price: Int)
private data class Order(val id: Int, val food: String, val customer: String, val address: String,
                         val amount: Int, val payment: String, val status: String)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { QuickRouteApp() }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuickRouteApp() {
    var role by remember { mutableStateOf("Customer") }
    var payment by remember { mutableStateOf("UPI") }
    var notice by remember { mutableStateOf("Welcome! Choose a role to explore Quick Route.") }
    var nextId by remember { mutableIntStateOf(2) }
    var orders by remember { mutableStateOf(listOf(
        Order(1001, "Paneer Thali", "Aman", "Kankarbagh, Patna", 180, "UPI", "New")
    )) }
    var vendorName by remember { mutableStateOf("") }
    var vendorPhone by remember { mutableStateOf("") }
    var vendorAddress by remember { mutableStateOf("") }
    var riderName by remember { mutableStateOf("") }
    var riderPhone by remember { mutableStateOf("") }
    var riderVehicle by remember { mutableStateOf("Bike") }
    var applications by remember { mutableStateOf(listOf("Sharma Food Corner • Vendor • Pending")) }

    val foods = listOf(
        Food("Paneer Thali", "Fresh meal • Popular choice", 180),
        Food("Veg Biryani", "Aromatic rice and vegetables", 140),
        Food("Chicken Biryani", "Restaurant-style biryani", 220),
        Food("Samosa (2 pcs)", "Crispy evening snack", 35)
    )

    MaterialTheme(colorScheme = lightColorScheme(primary = Green, secondary = Green)) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Column {
                        Text("QUICK ROUTE", fontWeight = FontWeight.ExtraBold)
                        Text("Your Food, Our Route", style = MaterialTheme.typography.labelSmall)
                    } },
                    actions = { Icon(Icons.Default.DeliveryDining, contentDescription = null,
                        tint = Green, modifier = Modifier.padding(end = 16.dp).size(30.dp)) }
                )
            }
        ) { padding ->
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 28.dp, top = 8.dp)
            ) {
                item {
                    Card(colors = CardDefaults.cardColors(containerColor = Green),
                        shape = RoundedCornerShape(20.dp), modifier = Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(18.dp)) {
                            Text("Food delivery, made simple.", color = androidx.compose.ui.graphics.Color.White,
                                style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                            Text("Vendors • Delivery partners • Customers", color = androidx.compose.ui.graphics.Color.White)
                        }
                    }
                }
                item { Text("Continue as", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold) }
                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("Customer", "Vendor", "Delivery", "Admin").forEach { item ->
                            FilterChip(selected = role == item, onClick = { role = item },
                                label = { Text(item) })
                        }
                    }
                }
                item { Text(notice, color = Green, style = MaterialTheme.typography.bodySmall) }

                when (role) {
                    "Customer" -> {
                        item { Text("Discover food", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold) }
                        items(foods) { food ->
                            Card(Modifier.fillMaxWidth()) {
                                Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Column(Modifier.weight(1f)) {
                                        Text(food.name, fontWeight = FontWeight.Bold)
                                        Text(food.detail, style = MaterialTheme.typography.bodySmall)
                                        Text("₹${food.price}", color = Green, fontWeight = FontWeight.Bold)
                                    }
                                    Button(onClick = {
                                        val order = Order(nextId, food.name, "Guest Customer",
                                            "Customer address to be added", food.price, payment, "New")
                                        orders = orders + order
                                        nextId++
                                        notice = "Order #${order.id} placed (demo). Payment: $payment"
                                    }) { Text("Order") }
                                }
                            }
                        }
                        item { Text("Payment method", fontWeight = FontWeight.Bold) }
                        item {
                            Column {
                                listOf("UPI", "Card / Net Banking", "Cash on Delivery").forEach {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        RadioButton(selected = payment == it, onClick = { payment = it })
                                        Text(it)
                                    }
                                }
                            }
                        }
                        item { Text("Your orders", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold) }
                        items(orders) { OrderCard(it) }
                    }
                    "Vendor" -> {
                        item { Text("Vendor application", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold) }
                        item { OutlinedTextField(vendorName, { vendorName = it }, label = { Text("Restaurant / shop name") }, modifier = Modifier.fillMaxWidth()) }
                        item { OutlinedTextField(vendorPhone, { vendorPhone = it }, label = { Text("Phone number") }, modifier = Modifier.fillMaxWidth()) }
                        item { OutlinedTextField(vendorAddress, { vendorAddress = it }, label = { Text("Pickup address") }, modifier = Modifier.fillMaxWidth()) }
                        item { Button(onClick = {
                            if (vendorName.isNotBlank() && vendorPhone.isNotBlank() && vendorAddress.isNotBlank()) {
                                applications = applications + "$vendorName • Vendor • Pending"
                                notice = "Vendor application submitted for admin review."
                            } else notice = "Please complete all vendor application fields."
                        }, modifier = Modifier.fillMaxWidth()) { Text("Apply as vendor") } }
                        item { Text("Incoming orders", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold) }
                        items(orders) { order ->
                            OrderCard(order)
                            if (order.status == "New") {
                                Button(onClick = {
                                    orders = orders.map { if (it.id == order.id) it.copy(status = "Preparing") else it }
                                    notice = "Order #${order.id} accepted by vendor."
                                }) { Text("Accept order") }
                            }
                        }
                    }
                    "Delivery" -> {
                        item { Text("Delivery partner", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold) }
                        item { OutlinedTextField(riderName, { riderName = it }, label = { Text("Your name") }, modifier = Modifier.fillMaxWidth()) }
                        item { OutlinedTextField(riderPhone, { riderPhone = it }, label = { Text("Phone number") }, modifier = Modifier.fillMaxWidth()) }
                        item {
                            Text("Vehicle")
                            Row { listOf("Bike", "Scooter", "Cycle").forEach {
                                FilterChip(selected = riderVehicle == it, onClick = { riderVehicle = it },
                                    label = { Text(it) }, modifier = Modifier.padding(end = 6.dp))
                            } }
                        }
                        item { Button(onClick = {
                            if (riderName.isNotBlank() && riderPhone.isNotBlank()) {
                                applications = applications + "$riderName • Delivery partner • Pending"
                                notice = "Delivery partner application submitted."
                            } else notice = "Enter your name and phone number first."
                        }, modifier = Modifier.fillMaxWidth()) { Text("Apply as delivery partner") } }
                        item { Text("Available deliveries", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold) }
                        items(orders.filter { it.status == "Preparing" || it.status == "New" || it.status == "Picked up" }) { order ->
                            OrderCard(order)
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Button(onClick = {
                                    orders = orders.map { if (it.id == order.id) it.copy(status = "Picked up") else it }
                                    notice = "Order #${order.id} marked picked up."
                                }) { Text("Picked up") }
                                OutlinedButton(onClick = {
                                    orders = orders.map { if (it.id == order.id) it.copy(status = "Delivered") else it }
                                    notice = "Order #${order.id} marked delivered."
                                }) { Text("Delivered") }
                            }
                        }
                    }
                    else -> {
                        item { Text("Admin dashboard", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold) }
                        item { Text("Applications", fontWeight = FontWeight.Bold) }
                        items(applications) { app ->
                            Card(Modifier.fillMaxWidth()) {
                                Column(Modifier.padding(12.dp)) {
                                    Text(app)
                                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        Button(onClick = {
                                            applications = applications.map { if (it == app) app.replace("Pending", "Approved") else it }
                                            notice = "Application approved (demo)."
                                        }) { Text("Approve") }
                                        OutlinedButton(onClick = {
                                            applications = applications.filterNot { it == app }
                                            notice = "Application removed (demo)."
                                        }) { Text("Reject") }
                                    }
                                }
                            }
                        }
                        item { Text("All orders", fontWeight = FontWeight.Bold) }
                        items(orders) { OrderCard(it) }
                        item { Text("Payment collection and commissions will need a secure server and verified payment gateway before launch.",
                            style = MaterialTheme.typography.bodySmall) }
                    }
                }
            }
        }
    }
}

@Composable
private fun OrderCard(order: Order) {
    Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(3.dp)) {
            Text("Order #${order.id} • ${order.food}", fontWeight = FontWeight.Bold)
            Text("${order.customer} • ${order.address}", style = MaterialTheme.typography.bodySmall)
            Text("₹${order.amount} • ${order.payment} • ${order.status}", color = Green)
        }
    }
}
