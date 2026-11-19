package com.example.myapplication

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
            MyApplicationTheme (darkTheme = true){
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
        var favoritesIds by rememberSaveable { mutableStateOf(listOf<Int>())}

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
                    items(events, key = { it.id }) { event ->
                        val isFavorite = event.id in favoritesIds
                        EventCard(event = event, isFavorite, onFavoriteClick = {
                            favoritesIds = if (isFavorite) favoritesIds - event.id
                            else favoritesIds + event.id
                        })
                    }
                }
            }
        }
    }

    @Composable
    fun EventCard(
        event: Event,
        isFavorite: Boolean,
        onFavoriteClick: () -> Unit,
        modifier: Modifier = Modifier
    ) {
        val isSoldOut=event.capacity ==0
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable
                {
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
                    ) {
                    Text (text = stringResource(if (isSoldOut) R.string.sold_out_button else R.string.reserve_button))
                }
                TextButton(
                    onClick = onFavoriteClick,
                    colors = ButtonDefaults.textButtonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                        ),
                    modifier = Modifier.padding(top = 4.dp)
                    ) {
                    Text(if (isFavorite) "✨ Favori" else "Ajouter aux favoris")
                }
            }
        }
    }

    @Composable
    fun FavoriteButton() {
        var isFavorite by rememberSaveable {mutableStateOf(false)}

        TextButton(onClick = { isFavorite = !isFavorite}) {
            Text(if (isFavorite) "✨ Favori" else "Ajouter aux favoris")
        }
    }

    @Composable
    fun EventDetailScreen(event: Event,
                          onReserveClick: () -> Unit = {
                              Log.d(TAG, "Réservation depuis le détail pour : ${event.title}")
                          },
                          modifier: Modifier = Modifier) {
        val isSoldOut = event.capacity == 0

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.standart_img),
                contentDescription = event.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )

            Text(
                text = event.title,
                style = MaterialTheme.typography.headlineSmall
            )

            Text(
                text = "Catégorie : ${event.category}",
                style = MaterialTheme.typography.labelLarge
            )

            Text(
                text = "Lieu : ${event.location ?: "Lieu à confirmer"}",
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = event.description,
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = onReserveClick,
                enabled = !isSoldOut,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text (text = stringResource(if (isSoldOut) R.string.sold_out_button else R.string.reserve_button))
            }
        }
    }
}