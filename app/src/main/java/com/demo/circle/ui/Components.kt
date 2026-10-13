package com.demo.circle.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.demo.circle.data.Post
import com.demo.circle.data.User

@Composable
fun Avatar(user: User, size: Dp, modifier: Modifier = Modifier, ring: Boolean = false) {
    val inner = @Composable {
        Box(
            Modifier.size(size).clip(CircleShape).background(Brush.linearGradient(user.colors)),
            contentAlignment = Alignment.Center,
        ) {
            Text(user.initials, color = Color.White, fontWeight = FontWeight.Bold, fontSize = (size.value * 0.36f).sp)
        }
    }
    if (ring) {
        Box(
            modifier.border(2.5.dp, BrandGradient, CircleShape).padding(4.dp),
            contentAlignment = Alignment.Center,
        ) { inner() }
    } else {
        Box(modifier) { inner() }
    }
}

/** Placeholder "photo": a gradient tile with a large emoji, so the demo works fully offline. */
@Composable
fun PostArt(post: Post, modifier: Modifier = Modifier, emojiSize: Int = 96) {
    Box(modifier.background(Brush.linearGradient(post.colors)), contentAlignment = Alignment.Center) {
        Text(post.emoji, fontSize = emojiSize.sp)
    }
}

@Composable
fun VerifiedBadge() {
    Icon(Icons.Filled.Verified, null, tint = Color(0xFF3897F0), modifier = Modifier.padding(start = 3.dp).size(14.dp))
}
