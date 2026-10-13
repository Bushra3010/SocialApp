package com.demo.circle

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.demo.circle.data.Conversation
import com.demo.circle.data.DummyData
import com.demo.circle.data.Post
import com.demo.circle.data.User
import com.demo.circle.ui.*

/** Screens pushed on top of the tabbed shell. */
sealed interface Overlay {
    data class PostDetail(val post: Post) : Overlay
    data class Comments(val post: Post) : Overlay
    data class Profile(val user: User) : Overlay
    data object Messages : Overlay
    data class Chat(val conv: Conversation) : Overlay
    data class Story(val user: User) : Overlay
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(statusBarStyle = SystemBarStyle.light(Color.Transparent.toArgb(), Color.Transparent.toArgb()))
        setContent { CircleTheme { App() } }
    }
}

@Composable
fun App() {
    var loggedIn by rememberSaveable { mutableStateOf(false) }
    if (!loggedIn) {
        LoginScreen(onLogin = { loggedIn = true })
        return
    }

    var tab by rememberSaveable { mutableIntStateOf(0) }
    val stack = remember { mutableStateListOf<Overlay>() }
    val push: (Overlay) -> Unit = { stack.add(it) }
    val pop: () -> Unit = { stack.removeLastOrNull() }

    BackHandler(enabled = stack.isNotEmpty() || tab != 0) {
        if (stack.isNotEmpty()) pop() else tab = 0
    }

    val openUser: (User) -> Unit = { if (it.id == DummyData.me.id) { stack.clear(); tab = 4 } else push(Overlay.Profile(it)) }
    val openComments: (Post) -> Unit = { push(Overlay.Comments(it)) }
    val openPost: (Post) -> Unit = { push(Overlay.PostDetail(it)) }

    when (val top = stack.lastOrNull()) {
        is Overlay.Story -> { StoryScreen(top.user, pop); return }
        else -> {}
    }

    Scaffold(
        containerColor = Color.White,
        bottomBar = {
            if (stack.isEmpty()) {
                HorizontalDivider(color = com.demo.circle.ui.Divider)
                NavigationBar(containerColor = Color.White, tonalElevation = 0.dp) {
                    val items = listOf(
                        Triple("Home", Icons.Filled.Home, Icons.Outlined.Home),
                        Triple("Explore", Icons.Filled.Search, Icons.Outlined.Search),
                        Triple("Create", Icons.Filled.AddBox, Icons.Outlined.AddBox),
                        Triple("Activity", Icons.Filled.Favorite, Icons.Outlined.FavoriteBorder),
                        Triple("Profile", Icons.Filled.AccountCircle, Icons.Outlined.AccountCircle),
                    )
                    items.forEachIndexed { i, (label, selectedIcon, icon) ->
                        NavigationBarItem(
                            selected = tab == i,
                            onClick = { tab = i },
                            icon = { Icon(if (tab == i) selectedIcon else icon, label, Modifier.size(27.dp)) },
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = Color.Transparent,
                                selectedIconColor = Color.Black,
                                unselectedIconColor = Color.Black,
                            ),
                        )
                    }
                }
            }
        },
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            when (val top = stack.lastOrNull()) {
                is Overlay.PostDetail -> PostDetailScreen(top.post, pop, openComments, openUser)
                is Overlay.Comments -> CommentsScreen(top.post, pop)
                is Overlay.Profile -> ProfileScreen(
                    user = top.user,
                    posts = DummyData.posts.filter { it.user.id == top.user.id } + DummyData.myPosts.shuffled(kotlin.random.Random(top.user.id.hashCode())).take(5),
                    isMe = false,
                    onBack = pop,
                    onOpenPost = openPost,
                    onMessage = {
                        val conv = DummyData.conversations.find { it.user.id == top.user.id }
                            ?: Conversation(top.user, emptyList()).also { DummyData.conversations.add(0, it) }
                        push(Overlay.Chat(conv))
                    },
                )
                Overlay.Messages -> MessagesScreen(pop) { push(Overlay.Chat(it)) }
                is Overlay.Chat -> ChatScreen(top.conv, pop)
                else -> when (tab) {
                    0 -> FeedScreen(
                        onOpenMessages = { push(Overlay.Messages) },
                        onOpenComments = openComments,
                        onOpenUser = openUser,
                        onOpenStory = { push(Overlay.Story(it)) },
                    )
                    1 -> ExploreScreen(openPost, openUser)
                    2 -> CreateScreen(onPosted = { tab = 0 })
                    3 -> ActivityScreen(openUser)
                    else -> ProfileScreen(
                        user = DummyData.me,
                        posts = DummyData.posts.filter { it.user.id == DummyData.me.id } + DummyData.myPosts,
                        isMe = true,
                        onBack = null,
                        onOpenPost = openPost,
                        onLogout = { stack.clear(); tab = 0; loggedIn = false },
                    )
                }
            }
        }
    }
}
