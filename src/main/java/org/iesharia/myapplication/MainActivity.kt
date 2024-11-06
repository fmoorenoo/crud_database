package org.iesharia.myapplication

import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
                        .padding(10.dp)
                ) { innerPadding ->
                    MainActivity (
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
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Base de Datos",
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp
        )


        OutlinedTextField(
            value = nameValue,
            onValueChange = { nameValue = it },
            label = { Text(text = "Nombre") },
            singleLine = true,
            shape = RoundedCornerShape(10.dp)
        )


        OutlinedTextField(
            value = ageValue,
            onValueChange = { ageValue = it },
            label = { Text(text = "Edad") },
            singleLine = true,
            shape = RoundedCornerShape(10.dp)
        )


        OutlinedTextField(
            value = idValue,
            onValueChange = { idValue = it },
            label = { Text(text = "ID para eliminar") },
            singleLine = true,
            shape = RoundedCornerShape(10.dp)
        )
    Column {
        Row {
// BOTÓN AÑADIR
            Button(
                onClick = {
                    val name = nameValue
                    val age = ageValue
                    db.addName(name, age)
                    Toast.makeText(context, "$name añadido a la base de datos", Toast.LENGTH_SHORT).show()
                    nameValue = ""
                    ageValue = ""
                }
            ) {
                Text("Añadir")
            }

    // BOTÓN EDITAR
            Button(
                onClick = {
                }
            ) {
                Text("Editar")
            }
        }


        Row {
    // BOTÓN MOSTRAR
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
                }
            ) {
                Text("Mostrar")
            }

    // BOTÓN ELIMINAR
            Button(
                onClick = {
                    val id = idValue.toInt()
                    db.deleteName(id)
                    Toast.makeText(context, "Persona con ID $id eliminada", Toast.LENGTH_SHORT).show()
                    idValue = ""
                }
            ) {
                Text("Eliminar")
            }
        }
    }

        Row {
            Text(
                modifier = Modifier.padding(8.dp),
                text = lId
            )
            Text(
                modifier = Modifier.padding(8.dp),
                text = lName
            )
            Text(
                modifier = Modifier.padding(8.dp),
                text = lAge
            )
        }
    }
}
