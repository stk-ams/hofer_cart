package at.fhj.hofer_cart

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalMaterial3Api::class)
@RunWith(AndroidJUnit4::class)
class ShoppingViewTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testShoppingView() {
        composeTestRule.setContent {
            ShoppingView(ArrayList<GroceryItem>())
        }
        composeTestRule.onNodeWithText("Shopping List").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Add").assertIsDisplayed()
    }

    @Test
    fun testAddItemView() {
        composeTestRule.setContent {
            ShoppingView(ArrayList<GroceryItem>())
        }
        composeTestRule.onNodeWithContentDescription("Add").performClick()
        composeTestRule.onNodeWithText("Add Item").assertIsDisplayed()
    }
}
