package com.ead.project.backanime

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import com.ead.lib.anime.core.models.directory.Anime
import com.ead.lib.anime.core.models.home.HomeEpisode
import com.ead.lib.anime.core.models.info.AnimeInfo
import com.ead.lib.anime.core.models.info.Episode
import com.ead.project.backanime.presentation.main.MainViewModel
import com.ead.project.backanime.presentation.theme.BackAnimeTheme

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BackAnimeTheme {
                val navController = rememberNavController()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        BottomNavBar(navController = navController)
                    }
                ) { innerPadding ->
                    NavigationGraph(
                        navController = navController,
                        viewModel = viewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

// Bottom Navigation Items
sealed class BottomNavItem(val route: String, @StringRes val title: Int, val icon: ImageVector) {
    data object Home : BottomNavItem("home", R.string.home, Icons.Default.Home)
    data object Search : BottomNavItem("search", R.string.search, Icons.Default.Search)
}

// Navigation Graph
@Composable
fun NavigationGraph(navController: NavHostController, viewModel: MainViewModel, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = BottomNavItem.Home.route,
        modifier = modifier
    ) {
        composable(BottomNavItem.Home.route) {
            HomeScreen(viewModel, navController)
        }
        composable(BottomNavItem.Search.route) {
            SearchScreen(viewModel, navController)
        }
        composable("animeInfo/{seo}") { backStackEntry ->
            val seo = backStackEntry.arguments?.getString("seo") ?: ""
            AnimeInfoScreen(viewModel, seo)
        }
        composable("episodes/{seo}") { backStackEntry ->
            val seo = backStackEntry.arguments?.getString("seo") ?: ""
            EpisodeScreen(viewModel, seo, navController)
        }
        composable("watchInfo/{seo}") { backStackEntry ->
            val seo = backStackEntry.arguments?.getString("seo") ?: ""
            WatchInfoScreen(viewModel, seo)
        }
    }
}

// Bottom Navigation Bar
@Composable
fun BottomNavBar(navController: NavController) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Search
    )

    NavigationBar {
        val currentRoute = navController.currentDestination?.route
        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(item.icon, contentDescription = stringResource(item.title))
                },
                label = { Text(stringResource(item.title)) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}

// Home Screen
@Composable
fun HomeScreen(viewModel: MainViewModel, navController: NavController) {
    LaunchedEffect(Unit) {
        viewModel.getHome()
    }
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(viewModel.homeEpisodes.value) { episode ->
            HomeEpisodeRow(episode, navController)
        }
    }
}

// HomeEpisodeRow to navigate to WatchInfo
@Composable
fun HomeEpisodeRow(episode: HomeEpisode, navController: NavController) {
    var seo = episode.seo
    if (seo.contains("/"))
        seo = seo.replace("/","WafSymbol")
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navController.navigate("watchInfo/${seo}")
            }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = episode.image,
            contentDescription = null,
            modifier = Modifier
                .size(120.dp, 160.dp)
                .clip(RoundedCornerShape(8.dp))
        )
        Column(modifier = Modifier.padding(start = 8.dp)) {
            Text(episode.title)
            Text("Episode: ${episode.number}")
            Text("Seo: ${episode.seo}")
        }
    }
}

// Search Screen
@Composable
fun SearchScreen(viewModel: MainViewModel, navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }

    Column {
        TextField(
            value = searchQuery,
            onValueChange = {
                searchQuery = it
                if (it.isNotBlank()) {
                    viewModel.getSearch(it)
                }
            },
            label = { Text("Search Anime") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(viewModel.searchAnimes.value) { anime ->
                AnimeRow(anime, navController)
            }
        }
    }
}

// AnimeRow with buttons to navigate
@Composable
fun AnimeRow(anime: Anime, navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = anime.image,
            contentDescription = null,
            modifier = Modifier
                .size(120.dp, 160.dp)
                .clip(RoundedCornerShape(8.dp))
        )
        Column(modifier = Modifier.padding(start = 8.dp)) {
            Text(anime.title)
            Row {
                Button(onClick = {
                    navController.navigate("animeInfo/${anime.seo}")
                }) {
                    Text("Info")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = {
                    navController.navigate("episodes/${anime.seo}")
                }) {
                    Text("Episodes")
                }
            }
        }
    }
}

@Composable
fun AnimeInfoScreen(viewModel: MainViewModel, seo: String) {
    LaunchedEffect(seo) {
        viewModel.getAnimeInfo(seo)
    }

    viewModel.animeInfo.value?.let { animeInfo ->
        AnimeInfoCard(animeInfo)
    } ?: Text("Loading Anime Info...")
}

@Composable
fun AnimeInfoCard(animeInfo: AnimeInfo) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            AsyncImage(
                model = animeInfo.coverImage,
                contentDescription = "Cover Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .clip(RoundedCornerShape(16.dp))
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = animeInfo.title,
                style = MaterialTheme.typography.headlineMedium,
                maxLines = 1
            )

            animeInfo.alternativeTitle?.let {
                Text(
                    text = "Alt Title: $it",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Status: ${animeInfo.status}",
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "Episodes: ${animeInfo.episodes ?: "Unknown"}",
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "Release Date: ${animeInfo.release}",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Genres: ${animeInfo.genres.joinToString(", ")}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Synopsis",
                style = MaterialTheme.typography.headlineSmall
            )

            Text(
                text = animeInfo.synopsis ?: "Unknown Synopsis",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun EpisodeScreen(viewModel: MainViewModel, seo: String, navController: NavController) {
    LaunchedEffect(seo) {
        viewModel.getEpisodes(seo)
    }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(viewModel.episodes.value) { episode ->
            EpisodeRow(episode, navController)
        }
    }
}

@Composable
fun EpisodeRow(episode: Episode, navController: NavController) {
    var seo = episode.seo
    if (seo.contains("/"))
        seo = seo.replace("/","WafSymbol")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navController.navigate("watchInfo/${seo}")
            }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = episode.image,
            contentDescription = null,
            modifier = Modifier
                .size(120.dp, 160.dp)
                .clip(RoundedCornerShape(8.dp))
        )
        Column(modifier = Modifier.padding(start = 8.dp)) {
            Text("Episode: ${episode.number}")
            Text("Seo: ${episode.seo}")
        }
    }
}

@Composable
fun WatchInfoScreen(viewModel: MainViewModel, seo: String) {
    LaunchedEffect(seo) {
        var seoResponse = seo
        if (seo.contains("WafSymbol"))
            seoResponse = seo.replace("WafSymbol","/")
        viewModel.getWatchInfoEpisode(seoResponse)
    }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(viewModel.watchInfoEpisodes.value) { server ->
            WatchInfoRow(server)
        }
    }
}

@Composable
fun WatchInfoRow(server: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.PlayArrow,
            contentDescription = null,
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(8.dp))
        )
        Column(modifier = Modifier.padding(start = 8.dp)) {
            Text(
                text = server,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "Server Link",
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}
