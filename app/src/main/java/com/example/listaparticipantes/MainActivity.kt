package com.example.listaparticipantes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

// 1. Definir un modelo de datos sencillo para el Participante
data class Participante(
    val nombre: String,
    val alias: String)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                Scaffold { padding ->
                    PantallaTorneo(modifier = Modifier.padding(paddingValues = padding))
                }
            }
        }
    }

    @Composable
    fun PantallaTorneo(modifier: Modifier = Modifier) {
        var nombre by rememberSaveable {
            mutableStateOf(value = "")
        }
        var alias by rememberSaveable { mutableStateOf(value = "") }
        val participantes = remember {
            mutableStateListOf<Participante>()
        }
        val formularioValido =
            nombre.isNotBlank() && alias.isNotBlank()
        Column(
            // Con modifier.fillMaxSize() le decimos que ocupe TODA la pantalla del móvil.
            // Con padding(all = 16.dp) dejamos un margen de seguridad en los bordes para que no quede pegado.
            modifier = Modifier.fillMaxSize().padding(all = 16.dp),
            // Esto hace que haya una separación automática de 8 puntos entre cada elemento que metamos dentro.
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // 1. El título de la pantalla
            Text(
                text = "Inscripción al torneo",
                style = MaterialTheme.typography.headlineSmall
            )

            // 2. El campo de texto para el Nombre
            OutlinedTextField(
                value = nombre, // Muestra en pantalla lo que vale la variable 'nombre'
                onValueChange = {
                    nombre = it
                }, // Cada vez que el usuario pulsa una tecla, actualiza la variable 'nombre'
                label = { Text("Nombre") }, // El texto flotante de ayuda
                singleLine = true, // Hace que no se pueda pulsar 'Intro' para saltar de línea
                modifier = Modifier.fillMaxWidth() // Ocupa todo el ancho disponible
            )

            // 3. El campo de texto para el Alias
            OutlinedTextField(
                value = alias, // Muestra en pantalla lo que vale la variable 'alias'
                onValueChange = {
                    alias = it
                }, // Cada vez que el usuario escribe, actualiza 'alias'
                label = { Text("Alias") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Button(
                onClick = {
                    // Si el nombre y el alias tienen texto (formularioValido es true)...
                    if (formularioValido) {
                        // Creamos un nuevo objeto Participante limpiando los espacios en blanco que sobren (.trim())
                        val nuevo = Participante(
                            nombre = nombre.trim(),
                            alias = alias.trim()
                        )
                        // Lo metemos en nuestra lista mutable. ¡Al meterlo, la lista visual se actualizará sola!
                        participantes.add(nuevo)

                        // Limpiamos los campos del formulario para poder escribir el siguiente
                        nombre = ""
                        alias = ""
                    }
                },
                // Aquí está el truco: el botón se desactiva solo (se pone gris) si los campos están vacíos
                enabled = formularioValido,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Añadir participante")
                // 1. El contador: se actualiza solo gracias a .size
                Text("Participantes inscritos: ${participantes.size}")

                // 2. Si la lista está vacía, mostramos un mensaje de ayuda
                if (participantes.isEmpty()) {
                    Text("Todavía no hay participantes")
                }
            }
            // 3. El contenedor de la lista
            LazyColumn(
                // .weight(1f) hace que la lista ocupe todo el espacio que sobre en la pantalla
                modifier = Modifier.weight(1f).fillMaxWidth(),
                // Deja una separación de 8 puntos entre tarjeta y tarjeta
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                // Recorremos la lista de participantes uno a uno
                items(participantes) { participante ->
                    // Pintamos la tarjeta visual del participante (la crearemos en el siguiente paso)
                    TarjetaParticipante(participante)

                    // Ponemos un botón justo debajo de su tarjeta para poder eliminarlo
                    Button(onClick = { participantes.remove(element = participante) }) {
                        Text("Eliminar participante")
                    }
                }
            }
        }
    }

    @Composable
    fun TarjetaParticipante(participante: Participante) {
        // Card es un contenedor que tiene bordes redondeados y un diseño estilo tarjeta
        Card(modifier = Modifier.fillMaxWidth()) {
            // Metemos un Column interno para poner el nombre arriba y el alias abajo
            Column(modifier = Modifier.padding(all = 12.dp)) {
                Text(
                    text = participante.nombre,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(text = "Alias: ${participante.alias}")
            }
        }
    }
    @Preview(showSystemUi = true, showBackground = true)
    @Composable
    fun PantallaTorneoPreview() {
        MaterialTheme {
            PantallaTorneo()
        }
    }
}
