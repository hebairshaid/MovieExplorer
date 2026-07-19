package com.movieexplorer.home_screen.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.compose.*
import com.movieexplorer.R
import com.movieexplorer.ui.theme.DarkBlue
import com.movieexplorer.ui.theme.Gold
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import kotlinx.coroutines.launch
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowCompat
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.LocalContext
import android.app.Activity
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.paging.compose.collectAsLazyPagingItems


@OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun MovieScreen(
    //viewModel: HomeViewModel,
    navController: NavController,
    onLogout: () -> Unit
) {

    val viewModel: HomeViewModel = hiltViewModel()
   // var selectedTab by remember { mutableStateOf(0) }

    val tabs = listOf("Action", "Comedy", "Adventure")

    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { tabs.size }
    )

    val scope = rememberCoroutineScope()


    //val state = viewModel.state.value
    //val state = viewModel.state.collectAsStateWithLifecycle().value
    val movies = viewModel.movies.collectAsLazyPagingItems()
    val isOnline = viewModel.isOnline.collectAsStateWithLifecycle().value
    /*
    viewModel.state This is a Flow (stream of updates)
    collectAsStateWithLifecycle() Converts Flow → Compose State
    .value Gets actual HomeState object
    */

    /*LaunchedEffect(Unit) {
        viewModel.loadMovies(28)
    }*/

    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.splash)
    )

    val progress by animateLottieCompositionAsState(
        composition = composition
    )

    val dynamicProperties = rememberLottieDynamicProperties(
        rememberLottieDynamicProperty(
            property = LottieProperty.COLOR,
            value = Gold.toArgb(),
            keyPath = arrayOf("**", "Fill 1")
        ),
        rememberLottieDynamicProperty(
            property = LottieProperty.STROKE_COLOR,
            value = Gold.toArgb(),
            keyPath = arrayOf("**", "Stroke 1")
        )
    )

    val view = LocalView.current
    val activity = view.context as Activity

    SideEffect {
        val window = activity.window

        window.statusBarColor = DarkBlue.toArgb()

        WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
    }

    var menuExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(pagerState.currentPage) {
        viewModel.onTabSelected(pagerState.currentPage)
    }



    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBlue)
            .padding(WindowInsets.statusBars.asPaddingValues())
    ) {


        // HEADER
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 16.dp,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            LottieAnimation(
                composition = composition,
                progress = progress,
                dynamicProperties = dynamicProperties,
                modifier = Modifier.size(50.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "Movie Explorer",
                color = Color.White,
                fontSize = 24.sp
            )

            Spacer(modifier = Modifier.weight(1f))

            Box {

                IconButton(onClick = { menuExpanded = true }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Menu",
                        tint = Gold
                    )
                }

                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false }
                ) {

                    DropdownMenuItem(
                        text = { Text("Profile") },
                        onClick = {
                            menuExpanded = false
                            // TODO: navigate to profile
                        }
                    )

                    DropdownMenuItem(
                        text = { Text("Logout") },
                        onClick = {
                            menuExpanded = false

                            viewModel.logout {
                                navController.navigate("login") {
                                    popUpTo("home") { inclusive = true }
                                }
                            }
                        }
                    )
                }
            }
        }

        if (!isOnline) {
            Text(
                text = "You are offline",
                color = Color.White,
                fontSize = 14.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFB45309))
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            )
        }

        // TABS
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            containerColor = DarkBlue,
            contentColor = Gold,
             indicator = { tabPositions ->

                val currentTabPosition = tabPositions[pagerState.currentPage]

                Box(
                    Modifier
                        .tabIndicatorOffset(currentTabPosition)
                        .height(3.dp)
                        .padding(horizontal = 24.dp)
                        .background(
                            color = Gold,
                            shape = RoundedCornerShape(50)
                        )
                )
            }
        ) {

            tabs.forEachIndexed { index, title ->

                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                       // viewModel.onTabSelected(index)
                    },
                    /*onClick = {
                        selectedTab = index

                        when (index) {
                            0 -> viewModel.loadMovies(28)
                            1 -> viewModel.loadMovies(35)
                            2 -> viewModel.loadMovies(12)
                        }
                    },*/
                    text = {
                        Text(
                            text = title,
                            color = if (pagerState.currentPage == index) Gold else Color.Gray
                        )
                    }
                )
            }
        }

        // ================= PAGER =================
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {

            // ITEMS
            items(
                count = movies.itemCount,
                key = { index ->
                    movies[index]?.id ?: index
                }
            ) { index ->

                val movie = movies[index]

                movie?.let {
                    MovieCard(
                        movie = it,
                        genre = tabs[pagerState.currentPage],
                        onClick = {
                            navController.currentBackStackEntry
                                ?.savedStateHandle
                                ?.set("genre", tabs[pagerState.currentPage])

                            navController.navigate("movie_details/${it.id}")
                        }
                    )
                }
            }

            // 🔵 LOADING NEXT PAGE (append)
            if (movies.loadState.append is androidx.paging.LoadState.Loading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Gold)
                    }
                }
            }

            // 🔴 ERROR NEXT PAGE
            if (movies.loadState.append is androidx.paging.LoadState.Error) {
                item {
                    Text(
                        text = "Error loading more movies",
                        color = Color.Red,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            // 🟡 INITIAL LOADING
            if (movies.loadState.refresh is androidx.paging.LoadState.Loading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 100.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Gold)
                    }
                }
            }

            // 🔴 INITIAL LOAD ERROR
            if (movies.loadState.refresh is androidx.paging.LoadState.Error && movies.itemCount == 0) {
                val error = movies.loadState.refresh as androidx.paging.LoadState.Error
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 100.dp, start = 16.dp, end = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = error.error.message ?: "Failed to load movies",
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(onClick = { movies.retry() }) {
                            Text("Retry")
                        }
                    }
                }
            }
        }

        // CONTENT
        /*when {

            state.isLoading -> {

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = Gold
                    )
                }
            }

            state.error != null -> {

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = state.error ?: "",
                        color = Color.White
                    )
                }
            }

            else -> {*/
       /* HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
            beyondBoundsPageCount = 1
        ) { page ->

            LaunchedEffect(page) {
                viewModel.onTabSelected(page)
            }*/

           /* when (val uiState = state) {

            is HomeUiState.Loading -> {

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Gold)
                }
            }

            is HomeUiState.Error -> {

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = uiState.message,
                        color = Color.White
                    )
                }
            }

            is HomeUiState.Success -> {

                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {

                    //items(state.movies) { movie ->
                    items(uiState.movies) { movie ->

                        val scale by animateFloatAsState(
                            targetValue = 1f,
                            label = ""
                        )

                        MovieCard(
                            movie = movie,
                            genre = tabs[page],
                            onClick = {

                                println("🔥 CLICKED MOVIE ID = ${movie.id}")

                                navController.currentBackStackEntry
                                    ?.savedStateHandle
                                    ?.set("genre", tabs[page])

                                navController.navigate(
                                    "movie_details/${movie.id}"
                                )
                            }
                        )
                    }
                }
            }
        }*/
    }
}}