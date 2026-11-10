package arthur.example.projeto_final.userinterface

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import arthur.example.projeto_final.userinterface.theme.Ink
import arthur.example.projeto_final.userinterface.theme.NavBarColor
import arthur.example.projeto_final.viewmodel.AppViewModelProvider
import arthur.example.projeto_final.viewmodel.AuthViewModel
import arthur.example.projeto_final.viewmodel.DetailViewModel
import arthur.example.projeto_final.viewmodel.MoviesViewModel
import kotlinx.serialization.Serializable
import kotlin.reflect.KClass

@Serializable data class LoginRoute(val registered: Boolean = false)
@Serializable data object RegisterRoute
@Serializable data object HomeRoute
@Serializable data object SearchRoute
@Serializable data object FavoritesRoute
@Serializable data object ProfileRoute

@Serializable data class DetailRoute(val movieId: String)

private data class TabItem(
    val route: Any,
    val routeClass: KClass<*>,
    val label: String,
    val icon: ImageVector,
    val selectedIcon: ImageVector
)

private val tabs = listOf(
    TabItem(HomeRoute, HomeRoute::class, "Início", Icons.Outlined.Home, Icons.Filled.Home),
    TabItem(SearchRoute, SearchRoute::class, "Buscar", Icons.Outlined.Search, Icons.Filled.Search),
    TabItem(FavoritesRoute, FavoritesRoute::class, "Favoritos", Icons.Outlined.FavoriteBorder, Icons.Filled.Favorite),
    TabItem(ProfileRoute, ProfileRoute::class, "Perfil", Icons.Outlined.Person, Icons.Filled.Person)
)

private fun NavHostController.navigateToTab(route: Any) = navigate(route) {
    popUpTo<HomeRoute> { saveState = true }
    launchSingleTop = true
    restoreState = true
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val authVm: AuthViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val moviesVm: MoviesViewModel = viewModel(factory = AppViewModelProvider.Factory)

    val userId by authVm.userId.collectAsStateWithLifecycle()
    val usuario by authVm.usuario.collectAsStateWithLifecycle()
    val authState by authVm.state.collectAsStateWithLifecycle()

    val startDestination: Any = remember { if (authVm.userId.value != null) HomeRoute else LoginRoute() }

    LaunchedEffect(userId) {
        val dest = navController.currentDestination ?: return@LaunchedEffect
        val onAuth = dest.hasRoute(LoginRoute::class) || dest.hasRoute(RegisterRoute::class)
        if (userId == null && !onAuth) {
            navController.navigate(LoginRoute()) { popUpTo(navController.graph.id) { inclusive = true } }
        } else if (userId != null && onAuth) {
            navController.navigate(HomeRoute) { popUpTo(navController.graph.id) { inclusive = true } }
        }
    }

    LaunchedEffect(authState.registered) {
        if (authState.registered) {
            navController.navigate(LoginRoute(registered = true)) { popUpTo<RegisterRoute> { inclusive = true } }
            authVm.clearState()
        }
    }

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination
    val showBottomBar = tabs.any { tab -> currentDestination?.hierarchy?.any { it.hasRoute(tab.routeClass) } == true }

    Scaffold(
        containerColor = Ink,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(containerColor = NavBarColor) {
                    tabs.forEach { tab ->
                        val selected = currentDestination?.hierarchy?.any { it.hasRoute(tab.routeClass) } == true
                        NavigationBarItem(
                            selected = selected,
                            onClick = { navController.navigateToTab(tab.route) },
                            icon = { Icon(if (selected) tab.selectedIcon else tab.icon, contentDescription = tab.label) },
                            label = { Text(tab.label, fontSize = 10.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color(0xFFFF3943),
                                selectedTextColor = Color.White,
                                indicatorColor = Color.Transparent,
                                unselectedIconColor = Color(0xFF8993A6),
                                unselectedTextColor = Color(0xFF8993A6)
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable<LoginRoute> { entry ->
                val route = entry.toRoute<LoginRoute>()
                LoginScreen(
                    state = authState,
                    registeredNotice = route.registered,
                    onLogin = authVm::login,
                    onGoToRegister = { navController.navigate(RegisterRoute) },
                    onEnter = authVm::clearState
                )
            }

            composable<RegisterRoute> {
                RegisterScreen(
                    state = authState,
                    onRegister = authVm::register,
                    onGoToLogin = { navController.popBackStack() },
                    onEnter = authVm::clearState
                )
            }

            composable<HomeRoute> {
                val state by moviesVm.state.collectAsStateWithLifecycle()
                val favoriteIds by moviesVm.favoriteIds.collectAsStateWithLifecycle()
                HomeScreen(
                    userName = usuario?.nome.orEmpty(),
                    state = state,
                    favoriteIds = favoriteIds,
                    onRetry = moviesVm::load,
                    onSearchClick = { navController.navigateToTab(SearchRoute) },
                    onProfileClick = { navController.navigateToTab(ProfileRoute) },
                    onMovieClick = { navController.navigate(DetailRoute(it.id)) },
                    onToggleFavorite = moviesVm::toggleFavorite
                )
            }

            composable<SearchRoute> {
                val state by moviesVm.state.collectAsStateWithLifecycle()
                val query by moviesVm.query.collectAsStateWithLifecycle()
                val director by moviesVm.director.collectAsStateWithLifecycle()
                val directors by moviesVm.directors.collectAsStateWithLifecycle()
                val results by moviesVm.searchResults.collectAsStateWithLifecycle()
                val favoriteIds by moviesVm.favoriteIds.collectAsStateWithLifecycle()
                SearchScreen(
                    state = state,
                    query = query,
                    director = director,
                    directors = directors,
                    results = results,
                    favoriteIds = favoriteIds,
                    onQueryChange = moviesVm::onQueryChange,
                    onDirectorChange = moviesVm::onDirectorChange,
                    onRetry = moviesVm::load,
                    onMovieClick = { navController.navigate(DetailRoute(it.id)) },
                    onToggleFavorite = moviesVm::toggleFavorite
                )
            }

            composable<FavoritesRoute> {
                val favorites by moviesVm.favorites.collectAsStateWithLifecycle()
                FavoritesScreen(
                    favorites = favorites,
                    onExplore = { navController.navigateToTab(HomeRoute) },
                    onMovieClick = { navController.navigate(DetailRoute(it.id)) },
                    onToggleFavorite = moviesVm::toggleFavorite
                )
            }

            composable<ProfileRoute> {
                val favorites by moviesVm.favorites.collectAsStateWithLifecycle()
                ProfileScreen(
                    name = usuario?.nome.orEmpty(),
                    email = usuario?.email.orEmpty(),
                    favoritesCount = favorites.size,
                    directorsCount = favorites.map { it.director }.distinct().size,
                    onOpenFavorites = { navController.navigateToTab(FavoritesRoute) },
                    onLogout = authVm::logout
                )
            }

            composable<DetailRoute> { entry ->
                val route = entry.toRoute<DetailRoute>()
                val detailVm: DetailViewModel = viewModel(factory = AppViewModelProvider.Factory)
                LaunchedEffect(route.movieId) { detailVm.load(route.movieId) }
                val state by detailVm.state.collectAsStateWithLifecycle()
                val favoriteIds by moviesVm.favoriteIds.collectAsStateWithLifecycle()
                DetailScreen(
                    state = state,
                    isFavorite = route.movieId in favoriteIds,
                    onBack = { navController.popBackStack() },
                    onRetry = { detailVm.load(route.movieId, force = true) },
                    onToggleFavorite = detailVm::toggleFavorite
                )
            }
        }
    }
}
