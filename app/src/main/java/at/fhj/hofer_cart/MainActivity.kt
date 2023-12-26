package at.fhj.hofer_cart

import android.content.Intent
import android.os.Bundle
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDismissState
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat.startActivity
import at.fhj.hofer_cart.ui.theme.Hofer_cartTheme

val groceryItemComparator = compareBy<GroceryItem> { it.category }.thenBy { it.name }

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Hofer_cartTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ShoppingView()
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingView() {
    var text by remember { mutableStateOf("") }
    var items by remember { mutableStateOf(listOf<GroceryItem>()) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Shopping List",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(Modifier.height(20.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedTextField(
                        modifier = Modifier.weight(1f),
                        value = text,
                        onValueChange = { text = it },
                        label = { Text("Enter item") }
                    )

                    val context = LocalContext.current

                    Button(onClick = {
                        context.startActivity(Intent(context, AddItemActivity::class.java))

                    }) {
                        Icon(Icons.Default.Add, contentDescription = "Add")
                    }
                }


            }

            LazyColumn {
                items(items, key = { item -> item.id }) { item -> // Assuming 'item' has a unique 'id' property
                    val dismissState = rememberDismissState()

                    SwipeToDismiss(
                        state = dismissState,
                        directions = setOf(DismissDirection.EndToStart),
                        background = { /* Add background if needed */ },
                        dismissContent = {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                            ) {
                                Text(
                                    text = item.toString(),
                                    modifier = Modifier.padding(8.dp),
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        }
                    )

                    LaunchedEffect(key1 = dismissState.isDismissed(DismissDirection.EndToStart)) {
                        if (dismissState.isDismissed(DismissDirection.EndToStart)) {
                            items -= item
                        }
                    }
                }
            }


        }
    }
}
