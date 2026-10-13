package com.demo.circle.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.demo.circle.data.*
import kotlinx.coroutines.delay

// ---------------------------------------------------------------- Shared top bar

@Composable
fun BackBar(title: String, onBack: () -> Unit, actions: @Composable RowScope.() -> Unit = {}) {
    Column {
        Row(Modifier.fillMaxWidth().height(56.dp).padding(horizontal = 4.dp), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") }
            Text(title, fontWeight = FontWeight.Bold, fontSize = 18.sp, modifier = Modifier.weight(1f), maxLines = 1, overflow = TextOverflow.Ellipsis)
            actions()
        }
        HorizontalDivider(color = Divider)
    }
}

// ---------------------------------------------------------------- Login

@Composable
fun LoginScreen(onLogin: () -> Unit) {
    var email by remember { mutableStateOf("alex.morgan@demo.com") }
    var password by remember { mutableStateOf("password123") }
    var loading by remember { mutableStateOf(false) }
    LaunchedEffect(loading) { if (loading) { delay(900); onLogin() } }

    Box(Modifier.fillMaxSize().background(BrandGradient)) {
        Column(
            Modifier.fillMaxSize().systemBarsPadding().imePadding().padding(28.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(Modifier.size(84.dp).border(7.dp, Color.White, CircleShape), contentAlignment = Alignment.Center) {
                Box(Modifier.size(22.dp).background(Color.White, CircleShape))
            }
            Text("Circle", color = Color.White, fontSize = 42.sp, fontWeight = FontWeight.ExtraBold, modifier = Modifier.padding(top = 16.dp))
            Text("Share moments with your people", color = Color.White.copy(alpha = 0.85f), fontSize = 15.sp)
            Spacer(Modifier.height(40.dp))
            Surface(shape = RoundedCornerShape(24.dp), color = Color.White, shadowElevation = 8.dp) {
                Column(Modifier.padding(20.dp)) {
                    OutlinedTextField(email, { email = it }, label = { Text("Email") }, singleLine = true,
                        leadingIcon = { Icon(Icons.Outlined.Email, null) }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp))
                    Spacer(Modifier.height(12.dp))
                    OutlinedTextField(password, { password = it }, label = { Text("Password") }, singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        leadingIcon = { Icon(Icons.Outlined.Lock, null) }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp))
                    Text("Forgot password?", color = Brand, fontSize = 13.sp, modifier = Modifier.align(Alignment.End).padding(top = 8.dp))
                    Spacer(Modifier.height(16.dp))
                    Button(onClick = { loading = true }, modifier = Modifier.fillMaxWidth().height(50.dp), shape = RoundedCornerShape(14.dp)) {
                        if (loading) CircularProgressIndicator(color = Color.White, strokeWidth = 2.dp, modifier = Modifier.size(22.dp))
                        else Text("Log in", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                    }
                    Row(Modifier.fillMaxWidth().padding(top = 16.dp), horizontalArrangement = Arrangement.Center) {
                        Text("Don't have an account? ", fontSize = 13.sp, color = Muted)
                        Text("Sign up", fontSize = 13.sp, color = Brand, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}

// ---------------------------------------------------------------- Explore

@Composable
fun ExploreScreen(onOpenPost: (Post) -> Unit, onOpenUser: (User) -> Unit) {
    var query by remember { mutableStateOf("") }
    val matches = DummyData.users.filter { query.isNotBlank() && (it.name.contains(query, true) || it.handle.contains(query, true)) }
    Column(Modifier.fillMaxSize()) {
        TextField(
            query, { query = it },
            placeholder = { Text("Search people") },
            leadingIcon = { Icon(Icons.Outlined.Search, null) },
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Surface2, unfocusedContainerColor = Surface2,
                focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent,
            ),
            modifier = Modifier.fillMaxWidth().padding(12.dp),
        )
        if (query.isNotBlank()) {
            LazyColumn {
                items(matches) { UserRow(it, onClick = { onOpenUser(it) }) }
                if (matches.isEmpty()) item { Text("No results for \"$query\"", color = Muted, modifier = Modifier.padding(20.dp)) }
            }
        } else {
            val tags = listOf("Travel", "Food", "Art", "Fitness", "Music", "Fashion", "Tech")
            LazyVerticalGrid(GridCells.Fixed(3), horizontalArrangement = Arrangement.spacedBy(2.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                item(span = { GridItemSpan(3) }) {
                    Row(Modifier.padding(start = 12.dp, bottom = 10.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        tags.take(5).forEach {
                            Surface(shape = RoundedCornerShape(10.dp), border = androidx.compose.foundation.BorderStroke(1.dp, Divider)) {
                                Text(it, fontSize = 13.sp, modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp))
                            }
                        }
                    }
                }
                items(DummyData.explore) { post ->
                    PostArt(post, Modifier.aspectRatio(1f).clickable { onOpenPost(post) }, emojiSize = 40)
                }
            }
        }
    }
}

@Composable
fun UserRow(user: User, subtitle: String = user.name, onClick: () -> Unit, trailing: @Composable () -> Unit = {}) {
    Row(Modifier.fillMaxWidth().clickable(onClick = onClick).padding(horizontal = 16.dp, vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
        Avatar(user, 48.dp)
        Column(Modifier.weight(1f).padding(start = 12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(user.handle, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                if (user.verified) VerifiedBadge()
            }
            Text(subtitle, color = Muted, fontSize = 13.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
        trailing()
    }
}

// ---------------------------------------------------------------- Single post

@Composable
fun PostDetailScreen(post: Post, onBack: () -> Unit, onOpenComments: (Post) -> Unit, onOpenUser: (User) -> Unit) {
    Column(Modifier.fillMaxSize()) {
        BackBar("Post", onBack)
        LazyColumn { item { PostCard(post, onOpenComments, onOpenUser) } }
    }
}

// ---------------------------------------------------------------- Create post

@Composable
fun CreateScreen(onPosted: () -> Unit) {
    val context = LocalContext.current
    val options = listOf(
        "🌅" to listOf(Color(0xFFFF9966), Color(0xFFFF5E62)),
        "🌊" to listOf(Color(0xFF00C6FF), Color(0xFF0072FF)),
        "🌿" to listOf(Color(0xFF56AB2F), Color(0xFFA8E063)),
        "☕" to listOf(Color(0xFF8E6E53), Color(0xFFD7B899)),
        "🎉" to listOf(Color(0xFFDA22FF), Color(0xFF9733EE)),
        "🍕" to listOf(Color(0xFFF7971E), Color(0xFFFFD200)),
        "🐶" to listOf(Color(0xFFFF9A8B), Color(0xFFFF6A88)),
        "🌃" to listOf(Color(0xFF232526), Color(0xFF414345)),
    )
    var selected by remember { mutableIntStateOf(0) }
    var caption by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    val preview = Post(0, DummyData.me, caption, options[selected].first, options[selected].second, 0, "now")

    Column(Modifier.fillMaxSize()) {
        Row(Modifier.fillMaxWidth().height(56.dp).padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
            Text("New post", fontWeight = FontWeight.Bold, fontSize = 20.sp, modifier = Modifier.weight(1f))
            TextButton(onClick = {
                DummyData.posts.add(0, Post((System.currentTimeMillis() % 100000).toInt() + 1000, DummyData.me,
                    caption.ifBlank { "Just shared a new moment ✨" }, preview.emoji, preview.colors, 0, "now", location.ifBlank { null }))
                Toast.makeText(context, "Posted to your feed!", Toast.LENGTH_SHORT).show()
                caption = ""; location = ""
                onPosted()
            }) { Text("Share", fontWeight = FontWeight.Bold, fontSize = 16.sp) }
        }
        HorizontalDivider(color = Divider)
        LazyColumn(Modifier.fillMaxSize()) {
            item {
                PostArt(preview, Modifier.fillMaxWidth().aspectRatio(1.2f), emojiSize = 110)
                Text("Choose a photo", fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(16.dp, 14.dp, 16.dp, 8.dp))
                Row(Modifier.padding(horizontal = 12.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    options.forEachIndexed { i, (emoji, colors) ->
                        Box(
                            Modifier.weight(1f).aspectRatio(1f).clip(RoundedCornerShape(10.dp))
                                .background(Brush.linearGradient(colors))
                                .then(if (i == selected) Modifier.border(3.dp, Brand, RoundedCornerShape(10.dp)) else Modifier)
                                .clickable { selected = i },
                            contentAlignment = Alignment.Center,
                        ) { Text(emoji, fontSize = 20.sp) }
                    }
                }
                OutlinedTextField(caption, { caption = it }, placeholder = { Text("Write a caption...") },
                    modifier = Modifier.fillMaxWidth().padding(12.dp).heightIn(min = 100.dp), shape = RoundedCornerShape(14.dp))
                OutlinedTextField(location, { location = it }, placeholder = { Text("Add location") }, singleLine = true,
                    leadingIcon = { Icon(Icons.Outlined.LocationOn, null) },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp), shape = RoundedCornerShape(14.dp))
                Spacer(Modifier.height(40.dp))
            }
        }
    }
}

// ---------------------------------------------------------------- Activity / notifications

@Composable
fun ActivityScreen(onOpenUser: (User) -> Unit) {
    Column(Modifier.fillMaxSize()) {
        Text("Activity", fontWeight = FontWeight.Bold, fontSize = 24.sp, modifier = Modifier.padding(16.dp, 12.dp))
        HorizontalDivider(color = Divider)
        LazyColumn {
            item { SectionLabel("Today") }
            items(DummyData.notifications.take(5)) { NotificationRow(it, onOpenUser) }
            item { SectionLabel("This week") }
            items(DummyData.notifications.drop(5)) { NotificationRow(it, onOpenUser) }
        }
    }
}

@Composable
private fun SectionLabel(text: String) =
    Text(text, fontWeight = FontWeight.Bold, fontSize = 16.sp, modifier = Modifier.padding(16.dp, 14.dp, 16.dp, 4.dp))

@Composable
private fun NotificationRow(n: Notification, onOpenUser: (User) -> Unit) {
    Row(Modifier.fillMaxWidth().clickable { onOpenUser(n.user) }.padding(horizontal = 16.dp, vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
        Box {
            Avatar(n.user, 46.dp)
            val (icon, tint) = when (n.type) {
                NotifType.LIKE -> Icons.Filled.Favorite to Color(0xFFED4956)
                NotifType.COMMENT -> Icons.Filled.ChatBubble to Color(0xFF3897F0)
                NotifType.FOLLOW -> Icons.Filled.PersonAdd to BrandPurple
                NotifType.MENTION -> Icons.Filled.AlternateEmail to Color(0xFF00A86B)
            }
            Box(Modifier.align(Alignment.BottomEnd).size(20.dp).background(Color.White, CircleShape).padding(2.dp).background(tint, CircleShape), contentAlignment = Alignment.Center) {
                Icon(icon, null, tint = Color.White, modifier = Modifier.size(11.dp))
            }
        }
        Text(buildAnnotatedString {
            withStyle(SpanStyle(fontWeight = FontWeight.SemiBold)) { append(n.user.handle) }
            append(" ${n.text} ")
            withStyle(SpanStyle(color = Muted)) { append(n.time) }
        }, fontSize = 14.sp, modifier = Modifier.weight(1f).padding(horizontal = 12.dp))
        val idx = n.postIndex
        if (n.type == NotifType.FOLLOW) {
            FollowButton(n.user, compact = true)
        } else if (idx != null) {
            PostArt(DummyData.myPosts[idx], Modifier.size(44.dp).clip(RoundedCornerShape(6.dp)), emojiSize = 18)
        }
    }
}

@Composable
fun FollowButton(user: User, compact: Boolean = false) {
    val following = user.id in DummyData.following
    val mod = if (compact) Modifier.height(32.dp) else Modifier.fillMaxWidth().height(38.dp)
    val pad = PaddingValues(horizontal = if (compact) 14.dp else 0.dp)
    val toggle = { if (following) DummyData.following.remove(user.id) else DummyData.following.add(user.id); Unit }
    if (following) {
        OutlinedButton(onClick = toggle, modifier = mod, shape = RoundedCornerShape(10.dp), contentPadding = pad) {
            Text("Following", color = Color.Black, fontSize = 13.sp)
        }
    } else {
        Button(onClick = toggle, modifier = mod, shape = RoundedCornerShape(10.dp), contentPadding = pad) {
            Text(if (compact) "Follow back" else "Follow", fontSize = 13.sp)
        }
    }
}

// ---------------------------------------------------------------- Profile

@Composable
fun ProfileScreen(
    user: User,
    posts: List<Post>,
    isMe: Boolean,
    onBack: (() -> Unit)?,
    onOpenPost: (Post) -> Unit,
    onMessage: () -> Unit = {},
    onLogout: () -> Unit = {},
) {
    var tab by remember { mutableIntStateOf(0) }
    var menu by remember { mutableStateOf(false) }
    val followers = user.followers + if (!isMe && user.id in DummyData.following) 1 else 0
    val saved = DummyData.posts.filter { it.saved.value }

    Column(Modifier.fillMaxSize()) {
        if (onBack != null) BackBar(user.handle, onBack)
        else Row(Modifier.fillMaxWidth().height(56.dp).padding(start = 16.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(user.handle, fontWeight = FontWeight.Bold, fontSize = 20.sp, modifier = Modifier.weight(1f))
            Box {
                IconButton(onClick = { menu = true }) { Icon(Icons.Filled.Menu, "Menu") }
                DropdownMenu(menu, { menu = false }) {
                    DropdownMenuItem(text = { Text("Settings") }, leadingIcon = { Icon(Icons.Outlined.Settings, null) }, onClick = { menu = false })
                    DropdownMenuItem(text = { Text("Saved") }, leadingIcon = { Icon(Icons.Outlined.BookmarkBorder, null) }, onClick = { menu = false; tab = 1 })
                    DropdownMenuItem(text = { Text("Log out", color = Color(0xFFED4956)) }, leadingIcon = { Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color(0xFFED4956)) }, onClick = { menu = false; onLogout() })
                }
            }
        }
        LazyVerticalGrid(GridCells.Fixed(3), horizontalArrangement = Arrangement.spacedBy(2.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            item(span = { GridItemSpan(3) }) {
                Column(Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Avatar(user, 86.dp, ring = !isMe)
                        Row(Modifier.weight(1f), horizontalArrangement = Arrangement.SpaceEvenly) {
                            Stat(if (isMe) posts.size.toString() else (posts.size + 40).toString(), "Posts")
                            Stat(DummyData.formatCount(followers), "Followers")
                            Stat(DummyData.formatCount(user.following), "Following")
                        }
                    }
                    Row(Modifier.padding(top = 10.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text(user.name, fontWeight = FontWeight.SemiBold)
                        if (user.verified) VerifiedBadge()
                    }
                    Text(user.bio, fontSize = 14.sp)
                    Spacer(Modifier.height(14.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        if (isMe) {
                            OutlinedButton({}, Modifier.weight(1f).height(38.dp), shape = RoundedCornerShape(10.dp)) { Text("Edit profile", color = Color.Black, fontSize = 13.sp) }
                            OutlinedButton({}, Modifier.weight(1f).height(38.dp), shape = RoundedCornerShape(10.dp)) { Text("Share profile", color = Color.Black, fontSize = 13.sp) }
                        } else {
                            Box(Modifier.weight(1f)) { FollowButton(user) }
                            OutlinedButton(onMessage, Modifier.weight(1f).height(38.dp), shape = RoundedCornerShape(10.dp)) { Text("Message", color = Color.Black, fontSize = 13.sp) }
                        }
                    }
                }
            }
            item(span = { GridItemSpan(3) }) {
                if (isMe) {
                    TabRow(tab, containerColor = Color.White, contentColor = Color.Black, indicator = { pos ->
                        TabRowDefaults.SecondaryIndicator(Modifier.then(TabRowDefaults.run { Modifier.tabIndicatorOffset(pos[tab]) }), color = Color.Black, height = 1.5.dp)
                    }) {
                        Tab(tab == 0, { tab = 0 }, icon = { Icon(Icons.Filled.GridOn, "Posts") })
                        Tab(tab == 1, { tab = 1 }, icon = { Icon(Icons.Outlined.BookmarkBorder, "Saved") })
                    }
                } else HorizontalDivider(color = Divider)
            }
            val shown = if (isMe && tab == 1) saved else posts
            items(shown) { post -> PostArt(post, Modifier.aspectRatio(1f).clickable { onOpenPost(post) }, emojiSize = 40) }
            if (shown.isEmpty()) item(span = { GridItemSpan(3) }) {
                Text("Nothing saved yet.\nTap the bookmark on any post to save it.", color = Muted, textAlign = TextAlign.Center, modifier = Modifier.padding(40.dp))
            }
        }
    }
}

@Composable
private fun Stat(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, fontWeight = FontWeight.Bold, fontSize = 17.sp)
        Text(label, fontSize = 13.sp)
    }
}

// ---------------------------------------------------------------- Comments

@Composable
fun CommentsScreen(post: Post, onBack: () -> Unit) {
    var text by remember { mutableStateOf("") }
    Column(Modifier.fillMaxSize().imePadding()) {
        BackBar("Comments", onBack)
        LazyColumn(Modifier.weight(1f)) {
            item {
                CommentRow(Comment(post.user, post.caption, post.time))
                HorizontalDivider(color = Divider)
            }
            items(post.comments) { CommentRow(it) }
        }
        InputBar(text, { text = it }, "Add a comment...") {
            post.comments.add(Comment(DummyData.me, text, "now")); text = ""
        }
    }
}

@Composable
private fun CommentRow(c: Comment) {
    var liked by remember { mutableStateOf(false) }
    Row(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 10.dp)) {
        Avatar(c.user, 36.dp)
        Column(Modifier.weight(1f).padding(horizontal = 12.dp)) {
            Text(buildAnnotatedString {
                withStyle(SpanStyle(fontWeight = FontWeight.SemiBold)) { append(c.user.handle) }
                withStyle(SpanStyle(color = Muted)) { append("  ${c.time}") }
            }, fontSize = 13.sp)
            Text(c.text, fontSize = 14.sp)
            Text("Reply", color = Muted, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(top = 4.dp))
        }
        Icon(if (liked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder, null,
            tint = if (liked) Color(0xFFED4956) else Muted, modifier = Modifier.size(16.dp).clickable { liked = !liked })
    }
}

@Composable
fun InputBar(value: String, onChange: (String) -> Unit, hint: String, onSend: () -> Unit) {
    HorizontalDivider(color = Divider)
    Row(Modifier.fillMaxWidth().padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
        Avatar(DummyData.me, 36.dp)
        TextField(value, onChange, placeholder = { Text(hint) }, maxLines = 4,
            shape = RoundedCornerShape(22.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Surface2, unfocusedContainerColor = Surface2,
                focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent,
            ),
            modifier = Modifier.weight(1f).padding(horizontal = 8.dp))
        IconButton(onClick = onSend, enabled = value.isNotBlank()) {
            Icon(Icons.AutoMirrored.Filled.Send, "Send", tint = if (value.isNotBlank()) Brand else Muted)
        }
    }
}

// ---------------------------------------------------------------- Messages

@Composable
fun MessagesScreen(onBack: () -> Unit, onOpenChat: (Conversation) -> Unit) {
    Column(Modifier.fillMaxSize()) {
        BackBar(DummyData.me.handle, onBack) { IconButton({}) { Icon(Icons.Outlined.EditNote, "New message") } }
        LazyColumn {
            item {
                Row(Modifier.padding(12.dp), horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                    DummyData.users.take(5).forEach { u ->
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box {
                                Avatar(u, 56.dp)
                                Box(Modifier.align(Alignment.BottomEnd).size(16.dp).background(Color.White, CircleShape).padding(2.dp).background(Color(0xFF2ECC71), CircleShape))
                            }
                            Text(u.name.substringBefore(" "), fontSize = 12.sp, modifier = Modifier.padding(top = 4.dp))
                        }
                    }
                }
                Text("Messages", fontWeight = FontWeight.Bold, fontSize = 16.sp, modifier = Modifier.padding(16.dp, 8.dp))
            }
            items(DummyData.conversations) { conv ->
                val last = conv.messages.last()
                UserRow(conv.user, subtitle = (if (last.fromMe) "You: " else "") + last.text + " · " + last.time, onClick = { onOpenChat(conv) }) {
                    if (conv.unread > 0) Box(Modifier.size(10.dp).background(Brand, CircleShape))
                }
            }
        }
    }
}

@Composable
fun ChatScreen(conv: Conversation, onBack: () -> Unit) {
    var text by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val replies = listOf("Haha totally 😂", "Sounds good!", "Love that ❤️", "Let's do it!", "Omg yes 🙌")
    LaunchedEffect(conv.messages.size) {
        listState.animateScrollToItem(conv.messages.size)
        if (conv.messages.lastOrNull()?.fromMe == true) {
            delay(1400)
            conv.messages.add(Message(false, replies[conv.messages.size % replies.size], "now"))
        }
    }
    Column(Modifier.fillMaxSize().imePadding()) {
        Column {
            Row(Modifier.fillMaxWidth().height(60.dp).padding(horizontal = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") }
                Avatar(conv.user, 36.dp)
                Column(Modifier.weight(1f).padding(start = 10.dp)) {
                    Text(conv.user.name, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                    Text("Active now", color = Muted, fontSize = 12.sp)
                }
                IconButton({}) { Icon(Icons.Outlined.Call, "Call") }
                IconButton({}) { Icon(Icons.Outlined.Videocam, "Video") }
            }
            HorizontalDivider(color = Divider)
        }
        LazyColumn(Modifier.weight(1f), state = listState, contentPadding = PaddingValues(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            item {
                Column(Modifier.fillMaxWidth().padding(vertical = 20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Avatar(conv.user, 80.dp)
                    Text(conv.user.name, fontWeight = FontWeight.Bold, fontSize = 18.sp, modifier = Modifier.padding(top = 8.dp))
                    Text("${conv.user.handle} · ${DummyData.formatCount(conv.user.followers)} followers", color = Muted, fontSize = 13.sp)
                }
            }
            items(conv.messages) { m ->
                Row(Modifier.fillMaxWidth(), horizontalArrangement = if (m.fromMe) Arrangement.End else Arrangement.Start) {
                    Box(
                        Modifier.widthIn(max = 270.dp).clip(RoundedCornerShape(20.dp))
                            .then(if (m.fromMe) Modifier.background(Brush.linearGradient(listOf(BrandPurple, Brand))) else Modifier.background(Surface2))
                            .padding(horizontal = 14.dp, vertical = 9.dp)
                    ) { Text(m.text, color = if (m.fromMe) Color.White else Color.Black, fontSize = 15.sp) }
                }
            }
        }
        InputBar(text, { text = it }, "Message...") {
            conv.messages.add(Message(true, text, "now")); text = ""
        }
    }
}

// ---------------------------------------------------------------- Story viewer

@Composable
fun StoryScreen(user: User, onClose: () -> Unit) {
    var progress by remember { mutableFloatStateOf(0f) }
    LaunchedEffect(user) {
        while (progress < 1f) { delay(30); progress += 0.006f }
        onClose()
    }
    val emoji = listOf("🌴", "🎉", "🍦", "🌸", "🏖️", "🎸", "📚", "🚴")[DummyData.users.indexOf(user).coerceAtLeast(0) % 8]
    Box(Modifier.fillMaxSize().background(Brush.verticalGradient(user.colors)).clickable { onClose() }) {
        Text(emoji, fontSize = 160.sp, modifier = Modifier.align(Alignment.Center))
        Column(Modifier.fillMaxWidth().systemBarsPadding().padding(12.dp)) {
            LinearProgressIndicator({ progress }, Modifier.fillMaxWidth().height(3.dp).clip(RoundedCornerShape(2.dp)),
                color = Color.White, trackColor = Color.White.copy(alpha = 0.35f), drawStopIndicator = {})
            Row(Modifier.padding(top = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                Avatar(user, 36.dp)
                Text(user.handle, color = Color.White, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(start = 10.dp))
                Text("  3h", color = Color.White.copy(alpha = 0.7f))
                Spacer(Modifier.weight(1f))
                Icon(Icons.Filled.Close, "Close", tint = Color.White, modifier = Modifier.clickable { onClose() })
            }
        }
        Row(Modifier.align(Alignment.BottomCenter).fillMaxWidth().navigationBarsPadding().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(Modifier.weight(1f).height(46.dp), shape = RoundedCornerShape(23.dp), color = Color.Transparent,
                border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.7f))) {
                Box(contentAlignment = Alignment.CenterStart) { Text("Send message", color = Color.White, modifier = Modifier.padding(start = 18.dp)) }
            }
            Icon(Icons.Outlined.FavoriteBorder, null, tint = Color.White, modifier = Modifier.padding(start = 14.dp).size(28.dp))
        }
    }
}
