package com.leo.newsreaderapp.ui.news

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.BrokenImage
import androidx.compose.material.icons.outlined.Cloud
import androidx.compose.material.icons.outlined.CloudOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.HtmlCompat
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import com.leo.newsreaderapp.database.Article
import com.leo.newsreaderapp.network.ConnectionState
import com.leo.newsreaderapp.util.connectivityState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun NewsDashboard(
    newsViewModel: NewsViewModel = hiltViewModel()
) {
    val allArticles by newsViewModel.getAllArticles().observeAsState()
    val searchedArticles by newsViewModel.getAllSearchedArticles().observeAsState()
    val isSearched = remember { mutableStateOf(false) }
    val state by newsViewModel.viewStateLiveData
    val context = LocalContext.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val searchedText = remember { mutableStateOf(TextFieldValue("")) }
        ConnectivityStatus()
        SearchView(state = searchedText, newsViewModel = newsViewModel, isSearched = isSearched)
        if (isSearched.value) {
            searchedArticles?.let { list ->
                LazyColumn {
                    items(list) { article ->
                        ArticleContent(article = article, context = context)
                    }
                }
            }
        } else {
            allArticles?.let { list ->
                LazyColumn {
                    items(list) { article ->
                        ArticleContent(article = article, context = context)
                    }
                }
            }
        }

        if (state.isLoading) {
            CircularProgressIndicator()
        }
    }
}

@Composable
fun ArticleContent(article: Article, context: Context) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
            .clickable {
                viewArticle(
                    context = context,
                    url = article.url ?: ""
                )
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.background)
        ) {
            SubcomposeAsyncImage(
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.Crop,
                loading = {
                    Column(
                        modifier = Modifier
                            .defaultMinSize(minHeight = 200.dp)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(modifier = Modifier.wrapContentSize())
                    }
                },
                error = {
                    Column(
                        modifier = Modifier
                            .defaultMinSize(minHeight = 200.dp)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.BrokenImage,
                            contentDescription = ""
                        )
                    }
                },
                model = ImageRequest.Builder(LocalContext.current)
                    .data(article.urlToImage)
                    .crossfade(true)
                    .scale(Scale.FIT)
                    .build(),
                contentDescription = null
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.weight(1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                text = article.author ?: "",
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start
            )
            Text(
                modifier = Modifier.weight(1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                text = article.formattedPublishedAt,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.End
            )
        }
        Text(
            style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(horizontal = 4.dp),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            text = article.title,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            modifier = Modifier.padding(start = 4.dp, top = 8.dp, end = 4.dp),
            text = HtmlCompat.fromHtml(article.description ?: "", HtmlCompat.FROM_HTML_MODE_COMPACT).toString(),
            maxLines = 4,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun SearchView(
    state: MutableState<TextFieldValue>,
    newsViewModel: NewsViewModel,
    isSearched: MutableState<Boolean>
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    TextField(
        value = state.value,
        onValueChange = { value ->
            state.value = value
        },
        modifier = Modifier
            .padding(horizontal = 12.dp)
            .padding(vertical = 8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        textStyle = TextStyle(fontSize = 18.sp),
        leadingIcon = {
            Icon(
                Icons.Default.Search,
                contentDescription = "",
                modifier = Modifier.size(24.dp)
            )
        },
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        trailingIcon = {
            if (state.value != TextFieldValue("")) {
                IconButton(
                    onClick = {
                        state.value = TextFieldValue("")
                        newsViewModel.deleteAllSearchedArticles()
                        isSearched.value = false
                    }
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        },
        singleLine = true,
        placeholder = {
            Text(
                text = "Search news",
                textAlign = TextAlign.Center
            )
        },
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.None,
            autoCorrect = true,
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                if (state.value != TextFieldValue("")) {
                    isSearched.value = true
                    newsViewModel.getSearchedArticles(state.value.text)
                } else {
                    isSearched.value = false
                    newsViewModel.getAllArticles()
                }
                keyboardController?.hide()
            }
        )
    )
}

fun viewArticle(context: Context, url: String) {
    runCatching {
        val packageName = "com.android.chrome"
        val builder = CustomTabsIntent.Builder()
        builder.setShowTitle(true)
        builder.setInstantAppsEnabled(true)
        val customBuilder = builder.build()
        customBuilder.intent.setPackage(packageName)
        customBuilder.launchUrl(context, Uri.parse(url))
    }.getOrElse {
        it.localizedMessage?.let { it1 -> Log.e("viewArticle", it1) }
    }
}

@ExperimentalCoroutinesApi
@Composable
fun ConnectivityStatus() {
    var showText by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val connection by connectivityState()

    val isConnected = connection === ConnectionState.Available

    if (isConnected) {
        LaunchedEffect(Unit) {
            coroutineScope.launch {
                showText = true
                delay(3000)
                showText = false
            }
        }
        if (showText) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp)
                    .background(Color(0xFF549E57)),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Outlined.Cloud, "")
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Online",
                    textAlign = TextAlign.Center
                )
            }
        }
    } else {
        LaunchedEffect(Unit) {
            coroutineScope.launch {
                showText = true
                delay(3000)
                showText = false
            }
        }

        if (showText) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp)
                    .background(Color(0xFFDB473C)),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.CloudOff,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Offline",
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}