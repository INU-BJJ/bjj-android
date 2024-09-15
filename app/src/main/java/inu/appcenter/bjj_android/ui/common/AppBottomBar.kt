package inu.appcenter.bjj_android.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import inu.appcenter.bjj_android.ui.navigate.Screen
import inu.appcenter.bjj_android.ui.theme.SelectedBottomBarIconColor
import inu.appcenter.bjj_android.ui.theme.UnselectedBottomBarIconColor


@Composable
fun AppBottomBar(
    navController: NavHostController
){
    val screens = listOf(Screen.Main, Screen.Tier, Screen.Review, Screen.MyPage)


    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 0.dp
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        screens.forEach { screen ->
            NavigationBarItem(
                icon = { Icon(painter = painterResource(screen.icon) , contentDescription = null) },
                label = { Text(screen.label) },
                selected = currentDestination?.route == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = SelectedBottomBarIconColor,
                    selectedTextColor = SelectedBottomBarIconColor,
                    unselectedIconColor = UnselectedBottomBarIconColor,
                    unselectedTextColor = UnselectedBottomBarIconColor,
                    indicatorColor = Color.Transparent
                ),
                interactionSource = remember { MutableInteractionSource(

                ) },
            )
        }
    }
}


