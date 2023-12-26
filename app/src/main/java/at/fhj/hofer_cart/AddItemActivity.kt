package at.fhj.hofer_cart

import android.os.Bundle
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import at.fhj.hofer_cart.ui.theme.Hofer_cartTheme


class AddItemActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Hofer_cartTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AddItemView()
                }
            }
        }
    }
}


@Composable
fun AddItemView() {
    var name by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var unit by remember { mutableStateOf("") }
    var category by remember { mutableStateOf(FoodCategory.OTHER) }
    var selected by remember { mutableStateOf(false)}

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Add Grocery Item", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onPrimary)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") }
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            singleLine = true,
            keyboardOptions =
            KeyboardOptions.Default.copy(keyboardType = KeyboardType.Decimal),
            value = amount,
            onValueChange = {
                if ((it.isEmpty() || it.toDoubleOrNull()!=null) &&!it.contains(" "))  amount = it
            },
            label = { Text("Amount") }
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = unit,
            onValueChange = { unit = it },
            label = { Text("Unit") }
        )
        Spacer(modifier = Modifier.height(16.dp))

        CategoryDropdownMenu(selected = selected, items = FoodCategory.values().toList(), selectedItem = category, onItemSelected = { category = it
        selected = true})


        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            // Handle the click event here
            // For example, you can create a new GroceryItem and add it to your list
        }) {
            Text("Add Item")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDropdownMenu(selected:Boolean, items: List<FoodCategory>, selectedItem: FoodCategory, onItemSelected: (FoodCategory) -> Unit) {
    var isExpanded by remember {
        mutableStateOf(false)
    }

    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = { newValue ->
            isExpanded = newValue
        }
    ) {
        var itemName = if(selected){
            selectedItem.name
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
            Text(text = "Category")
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
                        Text(text = option.name)
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



