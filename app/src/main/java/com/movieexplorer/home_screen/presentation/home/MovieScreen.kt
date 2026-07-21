package com.movieexplorer.home_screen.presentation.home

import android.app.Activity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.airbnb.lottie.compose.rememberLottieDynamicProperties
import com.airbnb.lottie.compose.rememberLottieDynamicProperty
import com.movieexplorer.R
import com.movieexplorer.navigation.Screen
import com.movieexplorer.ui.theme.DarkBlue
import com.movieexplorer.ui.theme.Gold
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun MovieScreen(
    navController: NavController,
    onLogout: () -> Unit
) {
    val viewModel: HomeViewModel = hiltViewModel()

    val tabs = listOf("Action", "Comedy", "Adventure")

    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { tabs.size }
    )

    val scope = rememberCoroutineScope()

    val movies = viewModel.movies.collectAsLazyPagingItems()
    val isOnline = viewModel.isOnline.collectAsStateWithLifecycle().value
    val favoriteIds = viewModel.favoriteIds.collectAsStateWithLifecycle().value

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    var isDrawerBusy by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val activity = context as? Activity

    val isDrawerShowing by remember {
        derivedStateOf {
            drawerState.currentValue == DrawerValue.Open ||
                drawerState.targetValue == DrawerValue.Open ||
                drawerState.isAnimationRunning
        }
    }

    suspend fun closeDrawerFully() {
        if (drawerState.currentValue != DrawerValue.Closed ||
            drawerState.targetValue != DrawerValue.Closed
        ) {
            drawerState.snapTo(DrawerValue.Closed)
        }
    }

    suspend fun toggleDrawer() {
        if (isDrawerBusy || drawerState.isAnimationRunning) return
        isDrawerBusy = true
        try {
            if (drawerState.currentValue == DrawerValue.Open ||
                drawerState.targetValue == DrawerValue.Open
            ) {
                drawerState.close()
            } else {
                drawerState.open()
            }
        } finally {
            isDrawerBusy = false
        }
    }

    // Always close drawer when returning to Home (Search/Profile/Favorites back)
    LaunchedEffect(Unit) {
        closeDrawerFully()
    }

    // Phone back: close drawer first; on Home itself exit app (never go to Login)
    BackHandler {
        if (isDrawerShowing) {
            scope.launch { closeDrawerFully() }
        } else {
            activity?.finish()
        }
    }

    LaunchedEffect(pagerState.currentPage) {
        viewModel.onTabSelected(pagerState.currentPage)
    }

    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.splash)
    )
    val progress by animateLottieCompositionAsState(composition = composition)

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

    SideEffect {
        val window = (view.context as Activity).window
        window.statusBarColor = DarkBlue.toArgb()
        WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        // Only dismiss via scrim/back when drawer is open — avoids edge-swipe stealing gestures
        gesturesEnabled = isDrawerShowing,
        scrimColor = Color.Black.copy(alpha = 0.55f),
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = DarkBlue,
                drawerContentColor = Color.White,
                modifier = Modifier
                    .fillMaxHeight()
                    .width(280.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(DarkBlue)
                        .padding(horizontal = 24.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Menu",
                        color = Gold,
                        fontSize = 22.sp,
                        modifier = Modifier.padding(bottom = 28.dp)
                    )

                    DrawerMenuButton(
                        title = "Profile",
                        onClick = {
                            scope.launch {
                                closeDrawerFully()
                                navController.navigate(Screen.Profile) {
                                    launchSingleTop = true
                                }
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    DrawerMenuButton(
                        title = "Favorites",
                        onClick = {
                            scope.launch {
                                closeDrawerFully()
                                navController.navigate(Screen.Watchlist) {
                                    launchSingleTop = true
                                }
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    DrawerMenuButton(
                        title = "Logout",
                        onClick = {
                            scope.launch {
                                closeDrawerFully()
                                viewModel.logout { onLogout() }
                            }
                        }
                    )
                }
            }
        }
    ) {
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
                        start = 12.dp,
                        end = 16.dp,
                        bottom = 16.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { scope.launch { toggleDrawer() } },
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Open drawer",
                        tint = Gold
                    )
                }

                Spacer(modifier = Modifier.width(4.dp))

                // Decorative only — not clickable
                LottieAnimation(
                    composition = composition,
                    progress = { progress },
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

                IconButton(
                    onClick = {
                        scope.launch {
                            closeDrawerFully()
                            navController.navigate(Screen.Search)
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Gold
                    )
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
                        },
                        text = {
                            Text(
                                text = title,
                                color = if (pagerState.currentPage == index) Gold else Color.Gray
                            )
                        }
                    )
                }
            }

            // SWIPEABLE PAGES
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) { page ->
                // Only the active page shows the live list; others keep a placeholder
                // so swipe still works without fighting paging.
                if (page == pagerState.currentPage) {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(
                            count = movies.itemCount,
                            key = { index -> movies[index]?.id ?: index }
                        ) { index ->
                            val movie = movies[index]
                            movie?.let {
                                MovieCard(
                                    movie = it,
                                    genre = tabs[page],
                                    isFavorite = favoriteIds.contains(it.id),
                                    onFavoriteClick = { viewModel.toggleFavorite(it) },
                                    onClick = {
                                        navController.currentBackStackEntry
                                            ?.savedStateHandle
                                            ?.set("genre", tabs[page])
                                        navController.navigate("movie_details/${it.id}")
                                    }
                                )
                            }
                        }

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

                        if (movies.loadState.append is androidx.paging.LoadState.Error) {
                            item {
                                Text(
                                    text = "Error loading more movies",
                                    color = Color.Red,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        }

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

                        if (
                            movies.loadState.refresh is androidx.paging.LoadState.Error &&
                            movies.itemCount == 0
                        ) {
                            val error =
                                movies.loadState.refresh as androidx.paging.LoadState.Error
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
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(DarkBlue)
                    )
                }
            }
        }
    }
}

@Composable
private fun DrawerMenuButton(
    title: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF0E1A4A),
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(14.dp)
    ) {
        Text(
            text = title,
            color = Gold,
            fontSize = 16.sp
        )
    }
}
