package com.example.musicwiki.navigation

import android.annotation.SuppressLint
import android.os.Build
import android.text.Html
import android.text.Spanned
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.collectAsState

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.musicwiki.R
import com.example.musicwiki.model.Tag
import com.example.musicwiki.screens.Screen
import com.example.musicwiki.viewModel.MainViewModel

@Composable
fun Navigation(viewModel: MainViewModel){

    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.GenericScreen.route){

        // Build the Nav graph
        composable(route= Screen.GenericScreen.route){
              GenericScreen(
                 navController=navController,
                  viewModel = viewModel
              )
        }

        composable(route= Screen.GenericDetailScreen.route+"/{name}", arguments= listOf(
            navArgument("name"){
                type= NavType.StringType
                defaultValue="Rock"
            }
        )){
            entry->
            GenreDetailScreen(
                navController= navController,
                viewModel= viewModel,
                genreName = entry.arguments?.getString("name")?:"Genre"
            )
        }
    }

}

/** Create Screens*/

/**********************************GENERE SCREEN *************************************************************************/

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenericScreen(
    navController: NavController,
    viewModel: MainViewModel
    ) {

    val expanded = remember { mutableStateOf(false) }
    /**
    val items = listOf(
        "Rock", "Pop", "Jazz", "Blues", "Country",
        "R&B", "Hip Hop", "Metal", "Electronic", "Reggae",
        "Folk", "Classical", "Dance", "Latin", "Opera",
        "Gospel", "Punk", "Ska", "Indie", "Metalcore"
    )
    */
    val items = viewModel.genres.collectAsState()


    Scaffold(
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                ),
                title = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 18.dp)
                            .background(MaterialTheme.colorScheme.surface)
                    ) {
                        Text(
                            text = "musicwiki",
                            modifier = Modifier.align(alignment = Alignment.Center),
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight(500)
                        )
                    }
                }
            )
        },
    ) {
        Column(
            modifier = Modifier
                .padding(6.dp)
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            //Spacer(modifier = Modifier.height(30.dp))
            Text(
                text = "",
                fontSize = 18.sp,
                fontWeight = FontWeight(400),
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(30.dp))
            Text(
                text = "Welcome!",
                fontSize = 18.sp,
                fontWeight = FontWeight(400),
                color = MaterialTheme.colorScheme.onSurface
            )
           Spacer(modifier = Modifier.height(30.dp))
            StartWithLayout(expanded = expanded.value, onExpandClick = {
                expanded.value = !expanded.value

            })
            Spacer(modifier = Modifier.height(30.dp))
            GenericRecycler(items.value.take(if (expanded.value) items.value.size else 10)) { it ->
                navController.navigate(Screen.GenericDetailScreen.withArgs(it))
            }
        }
    }

    DisposableEffect(Unit) {
        viewModel.getAllGenres()
        onDispose {}
    }
}


@SuppressLint("SuspiciousIndentation")
@Composable
fun GenericRecycler(
    startWithList: List<Tag>,
    onStartWithClick: (String) -> Unit) {


    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        contentPadding = PaddingValues(vertical = 8.dp, horizontal = 16.dp)
    ) {
        items(startWithList) { item ->
            GenericItem(name = item.name,onGenreClick= {

                   onStartWithClick(item.name)
            })
        }
    }


}

@Composable
fun GenericItem(name: String,onGenreClick:()->Unit) {


    Box(
        modifier = Modifier
            .size(100.dp, 40.dp)
            .padding(4.dp)
            .border(0.8.dp, Color.Black)
            .clip(RoundedCornerShape(18.dp))
            .clickable { onGenreClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = name,
            fontSize = 16.sp,
            fontWeight = FontWeight(450),
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
@Composable
fun StartWithLayout(expanded:Boolean, onExpandClick: () -> Unit) {

    Row (
        modifier= Modifier.fillMaxWidth(),
        horizontalArrangement= Arrangement.Center
    ){
        Text(
            text = "Choose a generic start with",
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.width(8.dp))
        Image(
            painter = painterResource(id = if (expanded) R.drawable.baseline_expand_less_24 else R.drawable.baseline_expand_more_24),
            contentDescription = if (expanded) "Expand Less" else "Expand More",
            modifier = Modifier
                .size(30.dp)
                .clickable { onExpandClick() }
        )
    }
}



/**

@Preview(showBackground = true)
@Composable
fun StartWithScreenPreview() {

   GenericScreen(



    )
}

*/

/*************************************************GENERE DETAIL SCREEN ***********************************/

@Composable
fun GenreDetailScreen(
    navController: NavController,
    viewModel: MainViewModel,
    genreName: String,
) {
    val details = viewModel.genreDetail.collectAsState()

    val text =
        getModifiedTextFromHTML((details.value?.wiki?.summary)?:"")

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                navController.popBackStack()
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_arrow_back),
                    contentDescription = "Back"
                )
            }
            Text(
                text = genreName,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Text(
            text = text.toString(),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))

    }

    DisposableEffect(Unit) {
        viewModel.getGenreInfo(genreName)
        onDispose {}
    }
}

fun getModifiedTextFromHTML(text:String): Spanned? {
    return  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT)
    } else {
        Html.fromHtml(text)
    }
}

/**
@Preview(showBackground = true)
@Composable
fun GenreDetailScreenPreview() {
    MaterialTheme {
        GenreDetailScreen(
           // onBackClick = {},
            genreName = "Rock",
            genreDescription = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed non risus. Suspendisse lectus tortor, dignissim sit amet, adipiscing nec, ultricies sed, dolor.",
        )
    }
}

 */
