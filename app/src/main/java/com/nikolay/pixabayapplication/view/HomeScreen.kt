import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.nikolay.pixabayapplication.model.ImageInfo
import com.nikolay.pixabayapplication.model.VideoInfo
import com.nikolay.pixabayapplication.view.ImageListScreen
import com.nikolay.pixabayapplication.view.VideoListScreen
import com.nikolay.pixabayapplication.viewModel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    mainViewModel: MainViewModel,
    onImageClick: (ImageInfo) -> Unit,
    onVideoClick: (VideoInfo) -> Unit,
    modifier: Modifier = Modifier
) {
    val tabIndex = mainViewModel.tabIndex

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Pixabay Search") }
            )
        },
        content = { innerPadding ->
            Column(modifier = modifier.padding(innerPadding)) {
                TabRow(selectedTabIndex = tabIndex.value) {
                    Tab(
                        selected = tabIndex.value == 0,
                        onClick = { tabIndex.value = 0 }
                    ) {
                        Text("Images")
                    }
                    Tab(
                        selected = tabIndex.value == 1,
                        onClick = { tabIndex.value = 1 }
                    ) {
                        Text("Videos")
                    }
                }
                if (tabIndex.value == 0) {
                    ImageListScreen(mainViewModel, onImageClick = onImageClick)
                } else {
                    VideoListScreen(mainViewModel, onVideoClick = onVideoClick)
                }
            }
        }
    )
}
