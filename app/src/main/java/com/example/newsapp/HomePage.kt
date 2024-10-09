package com.example.newsapp

import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.kwabenaberko.newsapilib.models.Article

@Composable
fun HomePageScreen(newsViewModel: NewsViewModel, navController: NavController) {

    val articles by newsViewModel.article.observeAsState(emptyList())

    Column(modifier = Modifier.fillMaxSize()) {
        CategoryBar(newsViewModel)

        LazyColumn(modifier = Modifier.fillMaxSize()) {

            items(articles) { singleArticle ->
                ArticleItem(singleArticle, navController)
            }
        }
    }
}

@Composable
fun ArticleItem(singleArticle: Article, navController: NavController) {
    Card(
        onClick = {
            navController.navigate(NewsArticleScreen(singleArticle.url))
        },
        modifier = Modifier.padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            AsyncImage(
                model = singleArticle.urlToImage
                    ?: "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSUrgu4a7W_OM8LmAuN7Prk8dzWXm7PVB_FmA&s",
                contentDescription = "image",
                modifier = Modifier
                    .size(80.dp)
                    .aspectRatio(1f), contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 8.dp)
            ) {
                Text(text = singleArticle.title, fontWeight = FontWeight.Bold, maxLines = 3)

                Text(text = singleArticle.source.name, maxLines = 1, fontSize = 14.sp)
            }

        }

    }
}

@Composable
fun CategoryBar(viewModel: NewsViewModel) {

    var searchQuery by remember {
        mutableStateOf("")
    }
    var isSearchExpanded by remember {
        mutableStateOf(false)
    }

    val categoryList = listOf(
        "GENERAL",
        "BUSINESS",
        "ENTERTAINMENT",
        "HEALTH",
        "SCIENCE",
        "SPORTS",
        "TECHNOLOGY"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        verticalAlignment = Alignment.CenterVertically
    ) {

        if (isSearchExpanded) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .padding(8.dp)
                    .height(48.dp)
                    .border(1.dp, Color.Gray, CircleShape)
                    .clip(CircleShape), trailingIcon = {
                    IconButton(onClick = {
                        isSearchExpanded = false
                        if (searchQuery.isNotEmpty()) {
                            viewModel.fetchEverythingWithQuery(searchQuery)
                        }
                    }) {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "search icon")
                    }
                }
            )
        } else {
            IconButton(onClick = {
                isSearchExpanded = true
            }) {
                Icon(imageVector = Icons.Default.Search, contentDescription = "search icon")
            }
        }
        categoryList.forEach { category ->
            Button(
                onClick = { viewModel.fetchNewsHeadlines(category = category) },
                modifier = Modifier.padding(4.dp)
            ) {
                Text(text = category)
            }
        }

    }

}