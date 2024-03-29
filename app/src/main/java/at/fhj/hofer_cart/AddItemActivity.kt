package at.fhj.hofer_cart

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import at.fhj.hofer_cart.ui.theme.Hofer_cartTheme
import java.io.Serializable

private lateinit var appDatabase:AppDatabase
class AddItemActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try{
            appDatabase = AppDatabase.getDatabase(this)


            val intent = intent
            var groceryList:ArrayList<GroceryItem> = ArrayList<GroceryItem>()

            if(intent.hasExtra("BUNDLE")){
                val args = intent.getBundleExtra("BUNDLE")
                groceryList = args?.getSerializable("LIST") as ArrayList<GroceryItem>
            }

            setContent {
                Hofer_cartTheme {
                    // A surface container using the 'background' color from the theme
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        AddItemView(groceryList)
                    }
                }
            }
        }catch (ex: Exception){
            Toast.makeText(this@AddItemActivity, "Error occurred: ${ex.localizedMessage}", Toast.LENGTH_LONG).show()
        }

    }
}

@Composable
fun AddItemView(groceryList: ArrayList<GroceryItem>) {
    var name by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var unit by remember { mutableStateOf("") }
    var category by remember { mutableStateOf(FoodCategory.OTHER) }
    var selected by remember { mutableStateOf(false)}


    var nameIsValid by remember { mutableStateOf(false) }
    var nameEntered by remember { mutableStateOf(false) }

    var amountIsValid by remember { mutableStateOf(false) }
    var amountEntered by remember { mutableStateOf(false) }

    var unitIsValid by remember { mutableStateOf(false) }
    var unitEntered by remember { mutableStateOf(false) }

    var categoryIsValid by remember { mutableStateOf(false) }

    var enterPressed by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Add Grocery Item", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onPrimary)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { input ->
                name = input
                nameIsValid = input.isNotEmpty() && input.isNotBlank()
                nameEntered = true;
            },

            label = { Text(context.getString(R.string.name)) }
        )
        if (nameEntered && !nameIsValid) {
            Text(text = context.getString(R.string.errorName), color = Color.Red)
        }
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            singleLine = true,
            keyboardOptions =
            KeyboardOptions.Default.copy(keyboardType = KeyboardType.Decimal),
            value = amount,
            onValueChange = {
                if ((it.isEmpty() || it.toDoubleOrNull()!=null) &&!it.contains(" "))  amount = it
                amountIsValid = it.isNotEmpty() && it.isNotBlank()
                amountEntered = true
            },
            label = { Text(context.getString(R.string.amount)) }
        )
        if (amountEntered && !amountIsValid) {
            Text(text = context.getString(R.string.errorAmount), color = Color.Red)
        }
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = unit,
            onValueChange = { input ->
                unit = input
                unitIsValid = input.isNotEmpty()  && input.isNotBlank()
                unitEntered = true;
            },
            label = { Text(context.getString(R.string.unit)) }
        )
        if (unitEntered && !unitIsValid) {
            Text(text = context.getString(R.string.errorUnit), color = Color.Red)
        }
        Spacer(modifier = Modifier.height(16.dp))

        CategoryDropdownMenu(selected = selected, items = FoodCategory.values().toList(), selectedItem = category, onItemSelected = { category = it
        selected = true})

        categoryIsValid = if(!selected && enterPressed){
            Text(text = context.getString(R.string.errorCategory), color = Color.Red)
            false
        }else{
            true
        }


        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            if(!nameEntered){
                nameEntered = true
                nameIsValid = false
            }

            if(!amountEntered){
                amountEntered = true
                amountIsValid = false
            }

            if(!unitEntered){
                unitEntered = true
                unitIsValid = false
            }
            enterPressed = true

            if(nameIsValid && amountIsValid && unitIsValid && categoryIsValid){
                try{
                    val newGroceryItem = GroceryItem(name = name, amount = amount.toInt(), unit = unit, category = category)
                    appDatabase.GroceryItemDao().insert(newGroceryItem)
                    groceryList += newGroceryItem
                    if(name == "Beer" && amount == "5")
                        Toast.makeText(context, "I hope you don't have university classes tomorrow ;)", Toast.LENGTH_LONG).show()
                    val intent = Intent(context, MainActivity::class.java)
                    val arguments = Bundle()
                    arguments.putSerializable("LIST", groceryList as Serializable)
                    intent.putExtra("BUNDLE", arguments)
                    context.startActivity(intent)
                }catch (ex:Exception){
                    Toast.makeText(context, "Error occurred: ${ex.localizedMessage}", Toast.LENGTH_LONG).show()
                }

            }

        }) {
            Text(context.getString(R.string.addItem))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDropdownMenu(selected:Boolean, items: List<FoodCategory>, selectedItem: FoodCategory, onItemSelected: (FoodCategory) -> Unit) {
    var isExpanded by remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current
    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = { newValue ->
            isExpanded = newValue
        }
    ) {
        var itemName = if(selected){
            context.getString(selectedItem.stringId)
        }else{
            ""
        }

        TextField(
        value = itemName,
        onValueChange = {},
        readOnly = true,
        trailingIcon = {
            ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
        },
        placeholder = {
            Text(text = context.getString(R.string.category))
        },
        colors = ExposedDropdownMenuDefaults.textFieldColors(),
        modifier = Modifier.menuAnchor()
    )

        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = {
                isExpanded = false
            }
        ) {
            items.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(text = context.getString(option.stringId))
                    },
                    onClick = {
                        isExpanded = false
                        onItemSelected(option)
                    }
                )
            }
        }
    }

}



