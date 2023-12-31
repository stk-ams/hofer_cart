# Concept
[https://github.com/stk-ams/hofer_cart/wiki/Concept](https://github.com/stk-ams/hofer_cart/wiki/Concept)

# HoferCart
A shopping list specialized on Hofer

## Security Measures
### Encrypted Shared Preferences
We use the encrypted shared preferences to store:
- Firstname which will be shown at login
- other personal information and user settings
#### Implementation
1. Create a file "secretSharedPrefs" that stores the encrypted shared prefs with AES256 encryption
    ```kotlin
    MasterKey masterKey = new MasterKey.Builder(context)
         .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
         .build();
    
     SharedPreferences sharedPreferences = EncryptedSharedPreferences.create(
         context,
         "secret_shared_prefs",
         masterKey,
         EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
         EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
     );
    
     // use the shared preferences and editor as you normally would
     SharedPreferences.Editor editor = sharedPreferences.edit();
    ```
2. Inserting a Key-Value pair into the secret shared prefs
    ```kotlin
    sharedPreferences.edit{ putString("Name", "Max")}
    ```

### Crypto API
We use Crypto API to store the grocery objects
#### Implementation
1. **Serialization:** Convert the grocery object using JSON serialization
   ```kotlin
   val json = Json.encodeToString(Data(42, "str"))
   ```
2. **Encryption:** Encrypt the JSON using Androids Crypto API
   ```kotlin
   
   ```
3. **Storage:** Store the encrypted JSON
   ```kotlin
   
   ```
4. **Decryption:** When needed, decrypt the JSON
   ```kotlin
   
   ```
5. **Deserialization:** Convert the JSON to an Object
   ```kotlin
   val obj = Json.decodeFromString<Data>("""{"a":42, "b": "str"}""")
   ```
