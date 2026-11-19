package com.example.myapplication

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.MyApplicationTheme

private const val TAG = "Campus"

class MainActivity : ComponentActivity() {
    val eventsList = listOf(
        Event(
            id = 1,
            title = "Conférence Kotlin & Android",
            category = "Technologie",
            location = "Amphithéâtre A",
            capacity = 120
        ),
        Event(
            id = 2,
            title = "Exposition Gustave Klint",
            category = "Culture",
            location = "Musée du Belvédère, Vienne",
            capacity = 25
        ),
        Event(
            id = 3,
            title = "Concert de musique de chambre",
            category = "Musique",
            location = null,
            capacity = 10
        ),
        Event(
            id = 4,
            title = "Concert Rock Live",
            category = "Musique",
            location = "Stade Municipal",
            capacity = 0
        ),
        Event(
            id = 5,
            title = "Marathon",
            category = "Sport",
            location = "Lab Innovation",
            capacity = 50
        ),
        Event(
            id = 6,
            title="Balade dans le Morvan",
            category = "Nature",
            location = "Parc régional naturel du Morvan",
            capacity =10
        )
    )

    val categories = listOf("Technologie","Sport","Musique","Culture", "Nature", "Social")

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.w(TAG, "Evenement à afficher")
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    EventList(
                        events = eventsList,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun EventList(events: List<Event>, modifier: Modifier = Modifier) {
        Scaffold(
            modifier = modifier,
            topBar = {
                TopAppBar(
                    title = { Text("Campus") }
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(categories) { category ->
                        FilterChip(
                            selected = false,
                            onClick = {},
                            label = { Text(category) }
                        )

                    }
                }

                LazyColumn(
                    modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        end = 16.dp,
                        top = 4.dp,
                        bottom = 16.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Correction ici : it.id au lieu de id.id
                    items(events, key = { it.id }) { event ->
                        EventCard(event = event)
                    }
                }
            }
        }
    }

    @Composable
    fun EventCard(event: Event) {
        val isSoldOut=event.capacity ==0
        Card(
            modifier = Modifier
                .clickable {
                    Log.d(TAG,"Click sur la carte de ${event.toString()}")
            }
        ){
            Column(modifier = Modifier
                .padding(16.dp )) {
                Text(text = event.title, style = MaterialTheme.typography.titleLarge)
                Text(event.category)
                Text(event.location ?: "Lieu à confirmer")
                Button(
                    onClick = {
                        Log.d(TAG, "Réservation tentée pour l'event: ${event.title}")
                    },
                    enabled = !isSoldOut
                    ) {if (isSoldOut) Text("Complet") else Text("Réserver") }
            }
        }
    }
}