package at.fhj.hofer_cart

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import at.fhj.hofer_cart.ui.theme.Hofer_cartTheme
import java.io.Serializable

val groceryItemComparator = compareBy<GroceryItem> { it.category }.thenBy { it.name }
private lateinit var appDatabase:AppDatabase
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try{
            appDatabase = AppDatabase.getDatabase(this)
            val intent = intent
            var obj:List<GroceryItem> = ArrayList<GroceryItem>()

            if(intent.hasExtra("BUNDLE")){
                val args = intent.getBundleExtra("BUNDLE")
                obj = args?.getSerializable("LIST") as ArrayList<GroceryItem>
            }
            if(obj.isEmpty()){
                obj = appDatabase.GroceryItemDao().getAll() as ArrayList<GroceryItem>
            }
            obj = obj.sortedWith(groceryItemComparator)

            setContent {
                Hofer_cartTheme {
                    // A surface container using the 'background' color from the theme
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        ShoppingView(obj)
                    }
                }
            }
        }catch (ex: Exception){
            Toast.makeText(this@MainActivity, "Error occurred: ${ex.localizedMessage}", Toast.LENGTH_LONG).show()
        }

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingView(itemList: List<GroceryItem>) {
    var text by remember { mutableStateOf("") }
    var items by remember { mutableStateOf(listOf<GroceryItem>()) }
    val context = LocalContext.current
    if(itemList != null && itemList.isNotEmpty()){
        items = itemList
    }

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
                    text = context.getString(R.string.shoppingList),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(Modifier.height(20.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = context.getString(R.string.title) ,
                        fontSize = 30.sp,
                    )



                    Button(onClick = {
                        val intent = Intent(context, AddItemActivity::class.java)
                        val arguments = Bundle()
                        arguments.putSerializable("LIST", items as Serializable)
                        intent.putExtra("BUNDLE", arguments)
                        context.startActivity(intent)

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
                            appDatabase.GroceryItemDao().deleteByLocalQuestionId(item.id)
                            items -= item
                        }
                    }
                }
            }


        }
    }
}
