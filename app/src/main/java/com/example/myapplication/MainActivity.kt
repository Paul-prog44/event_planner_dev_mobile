package com.example.myapplication

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
            title = "Atelier Jetpack Compose",
            category = "Formation",
            location = "Salle 204",
            capacity = 25
        ),
        Event(
            id = 3,
            title = "Réunion d'équipe secrète",
            category = "Interne",
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
            title = "Hackathon Mobile",
            category = "Compétition",
            location = "Lab Innovation",
            capacity = 50
        )
    )

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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Correction ici : it.id au lieu de id.id
            items(events, key = { it.id }) { event ->
                EventCard(event = event)
            }
        }
            }
    }

    @Composable
    fun EventCard(event: Event) {
        val isSoldOut=event.capacity ==0
        Card {
            Column(modifier = Modifier
                .padding(16.dp)) {
                Text(event.title)
                Text(event.category)
                Text(event.location ?: "Lieu à confirmer")
                Button(
                    onClick = { },
                    enabled = !isSoldOut
                    ) {if (isSoldOut) Text("Complet") else Text("Réserver") }
            }
        }
    }
}