package com.demo.circle.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.widget.Toast
import com.demo.circle.data.DummyData
import com.demo.circle.data.Post
import com.demo.circle.data.User
import kotlinx.coroutines.delay

@Composable
fun FeedScreen(
    onOpenMessages: () -> Unit,
    onOpenComments: (Post) -> Unit,
    onOpenUser: (User) -> Unit,
    onOpenStory: (User) -> Unit,
) {
    Column(Modifier.fillMaxSize()) {
        Row(
            Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text("Circle", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = Brand, modifier = Modifier.weight(1f))
            BadgedBox(badge = { Badge(containerColor = Brand) { Text("${DummyData.conversations.sumOf { it.unread }}") } }) {
                Icon(Icons.AutoMirrored.Outlined.Send, "Messages", Modifier.size(26.dp).clickable { onOpenMessages() })
            }
        }
        HorizontalDivider(color = Divider)
        LazyColumn(Modifier.fillMaxSize()) {
            item { StoriesRow(onOpenStory) ; HorizontalDivider(color = Divider) }
            items(DummyData.posts, key = { it.id }) { post ->
                PostCard(post, onOpenComments, onOpenUser)
            }
            item { Spacer(Modifier.height(24.dp)) }
        }
    }
}

@Composable
private fun StoriesRow(onOpenStory: (User) -> Unit) {
    LazyRow(contentPadding = PaddingValues(horizontal = 12.dp, vertical = 10.dp), horizontalArrangement = Arrangement.spacedBy(14.dp)) {
        item {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box {
                    Avatar(DummyData.me, 64.dp, Modifier.padding(4.dp))
                    Surface(shape = androidx.compose.foundation.shape.CircleShape, color = Color(0xFF3897F0),
                        modifier = Modifier.align(Alignment.BottomEnd).size(22.dp)) {
                        Box(contentAlignment = Alignment.Center) { Text("+", color = Color.White, fontWeight = FontWeight.Bold) }
                    }
                }
                Text("Your story", fontSize = 12.sp, modifier = Modifier.padding(top = 4.dp))
            }
        }
        items(DummyData.users) { user ->
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable { onOpenStory(user) }) {
                Avatar(user, 64.dp, ring = true)
                Text(user.handle, fontSize = 12.sp, maxLines = 1, overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.width(72.dp).padding(top = 4.dp), textAlign = androidx.compose.ui.text.style.TextAlign.Center)
            }
        }
    }
}

@Composable
fun PostCard(post: Post, onOpenComments: (Post) -> Unit, onOpenUser: (User) -> Unit) {
    val context = LocalContext.current
    var showHeart by remember { mutableStateOf(false) }
    val heartScale by animateFloatAsState(if (showHeart) 1f else 0f, spring(dampingRatio = 0.4f), label = "heart")
    LaunchedEffect(showHeart) { if (showHeart) { delay(700); showHeart = false } }

    Column(Modifier.fillMaxWidth()) {
        Row(Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
            Avatar(post.user, 34.dp, Modifier.clickable { onOpenUser(post.user) })
            Column(Modifier.weight(1f).padding(start = 10.dp).clickable { onOpenUser(post.user) }) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(post.user.handle, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                    if (post.user.verified) VerifiedBadge()
                }
                post.location?.let { Text(it, fontSize = 12.sp, color = Muted) }
            }
            Icon(Icons.Filled.MoreHoriz, null)
        }
        Box(contentAlignment = Alignment.Center) {
            PostArt(post, Modifier.fillMaxWidth().aspectRatio(1f).pointerInput(post.id) {
                detectTapGestures(onDoubleTap = {
                    if (!post.liked.value) post.toggleLike()
                    showHeart = true
                })
            })
            Icon(Icons.Filled.Favorite, null, tint = Color.White, modifier = Modifier.size(110.dp).scale(heartScale))
        }
        Row(Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { post.toggleLike() }) {
                if (post.liked.value) Icon(Icons.Filled.Favorite, "Unlike", tint = Color(0xFFED4956))
                else Icon(Icons.Outlined.FavoriteBorder, "Like")
            }
            IconButton(onClick = { onOpenComments(post) }) { Icon(Icons.Outlined.ChatBubbleOutline, "Comment") }
            IconButton(onClick = { Toast.makeText(context, "Share link copied", Toast.LENGTH_SHORT).show() }) {
                Icon(Icons.AutoMirrored.Outlined.Send, "Share")
            }
            Spacer(Modifier.weight(1f))
            IconButton(onClick = { post.saved.value = !post.saved.value }) {
                Icon(if (post.saved.value) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder, "Save")
            }
        }
        Column(Modifier.padding(horizontal = 14.dp)) {
            Text("${"%,d".format(post.likes.value)} likes", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            Text(buildAnnotatedString {
                withStyle(SpanStyle(fontWeight = FontWeight.SemiBold)) { append(post.user.handle) }
                append("  ")
                append(post.caption)
            }, fontSize = 14.sp, modifier = Modifier.padding(top = 2.dp))
            if (post.comments.isNotEmpty()) {
                Text("View all ${post.comments.size} comments", color = Muted, fontSize = 14.sp,
                    modifier = Modifier.padding(top = 4.dp).clickable { onOpenComments(post) })
            }
            Text(post.time + " ago", color = Muted, fontSize = 11.sp, modifier = Modifier.padding(top = 4.dp, bottom = 14.dp))
        }
    }
}
