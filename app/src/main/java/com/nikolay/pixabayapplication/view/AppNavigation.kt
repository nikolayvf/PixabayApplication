package com.nikolay.pixabayapplication.view

import HomeScreen
import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.nikolay.pixabayapplication.viewModel.MainViewModel

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun AppNavigation(navController: NavHostController) {
    val mainViewModel: MainViewModel = hiltViewModel()

    NavHost(navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                mainViewModel = mainViewModel,
                onImageClick = { imageInfo ->
                    mainViewModel.selectedImage = imageInfo
                    navController.navigate("imageDetails")
                },
                onVideoClick = { videoInfo ->
                    mainViewModel.selectedVideo = videoInfo
                    navController.navigate("videoDetails")
                }
            )
        }
        composable("imageDetails") {
            val image = mainViewModel.selectedImage
            if (image != null) {
                ImageDetailsScreen(image = image, navController = navController) // Pass navController
            } else {
                navController.popBackStack()
            }
        }
        composable("videoDetails") {
            val video = mainViewModel.selectedVideo
            if (video != null) {
                VideoDetailsScreen(video = video, navController = navController) // Pass navController
            } else {
                navController.popBackStack()
            }
        }
    }
}
