package org.iesharia.myapplication

import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.iesharia.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) { innerPadding ->
                    MainActivity(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
fun MainActivity(modifier: Modifier) {
    val context = LocalContext.current
    val db = DBHelper(context)

    var lId by remember { mutableStateOf("ID") }
    var lName by remember { mutableStateOf("Nombre") }
    var lAge by remember { mutableStateOf("Edad") }

    var nameValue by remember { mutableStateOf("") }
    var ageValue by remember { mutableStateOf("") }
    var idValue by remember { mutableStateOf("") }

    Column(
        verticalArrangement = Arrangement.Center,
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Base de Datos",
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp,
            color = Color(0xFF6200EA)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = nameValue,
            onValueChange = { nameValue = it },
            label = { Text(text = "Nombre") },
            singleLine = true,
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = ageValue,
            onValueChange = { ageValue = it },
            label = { Text(text = "Edad") },
            singleLine = true,
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = idValue,
            onValueChange = { idValue = it },
            label = { Text(text = "ID para eliminar") },
            singleLine = true,
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = {
                    val name = nameValue
                    val age = ageValue
                    db.addName(name, age)
                    Toast.makeText(context, "$name añadido a la base de datos", Toast.LENGTH_SHORT).show()
                    nameValue = ""
                    ageValue = ""
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Añadir")
            }

            Button(
                onClick = {
                    val id = idValue.toIntOrNull()
                    if (id != null && nameValue.isNotEmpty() && ageValue.isNotEmpty()) {
                        db.updateName(id, nameValue, ageValue)
                        Toast.makeText(context, "Persona con ID $id actualizada", Toast.LENGTH_SHORT).show()
                        nameValue = ""
                        ageValue = ""
                        idValue = ""
                    } else {
                        Toast.makeText(context, "Introduce ID, nombre y edad válidos", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Editar")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = {
                    val cursor = db.getName()
                    if (cursor != null) {
                        lId = "ID"
                        lName = "Nombre"
                        lAge = "Edad"
                        cursor.moveToFirst()
                        lId += "\n" + cursor.getString(cursor.getColumnIndex(DBHelper.ID_COL))
                        lName += "\n" + cursor.getString(cursor.getColumnIndex(DBHelper.NAME_COL))
                        lAge += "\n" + cursor.getString(cursor.getColumnIndex(DBHelper.AGE_COL))

                        while (cursor.moveToNext()) {
                            lId += "\n" + cursor.getString(cursor.getColumnIndex(DBHelper.ID_COL))
                            lName += "\n" + cursor.getString(cursor.getColumnIndex(DBHelper.NAME_COL))
                            lAge += "\n" + cursor.getString(cursor.getColumnIndex(DBHelper.AGE_COL))
                        }
                        cursor.close()
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Mostrar")
            }

            Button(
                onClick = {
                    val id = idValue.toIntOrNull()
                    if (id != null) {
                        db.deleteName(id)
                        Toast.makeText(context, "Persona con ID $id eliminada", Toast.LENGTH_SHORT).show()
                        idValue = ""
                    } else {
                        Toast.makeText(context, "Introduce un ID válido", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Eliminar")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = lId,
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            }
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = lName,
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            }
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = lAge,
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            }
        }
    }
}
