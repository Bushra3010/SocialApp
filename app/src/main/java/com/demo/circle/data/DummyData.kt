package com.demo.circle.data

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color

data class User(
    val id: String,
    val name: String,
    val handle: String,
    val bio: String,
    val colors: List<Color>,
    val followers: Int,
    val following: Int,
    val verified: Boolean = false,
) {
    val initials: String get() = name.split(" ").take(2).joinToString("") { it.first().uppercase() }
}

class Comment(val user: User, val text: String, val time: String)

class Post(
    val id: Int,
    val user: User,
    val caption: String,
    val emoji: String,
    val colors: List<Color>,
    likes: Int,
    val time: String,
    val location: String? = null,
    comments: List<Comment> = emptyList(),
) {
    var likes = mutableStateOf(likes)
    var liked = mutableStateOf(false)
    var saved = mutableStateOf(false)
    val comments = mutableStateListOf<Comment>().apply { addAll(comments) }

    fun toggleLike() {
        liked.value = !liked.value
        likes.value += if (liked.value) 1 else -1
    }
}

class Message(val fromMe: Boolean, val text: String, val time: String)

class Conversation(val user: User, messages: List<Message>, val unread: Int = 0) {
    val messages = mutableStateListOf<Message>().apply { addAll(messages) }
}

enum class NotifType { LIKE, COMMENT, FOLLOW, MENTION }

class Notification(val user: User, val type: NotifType, val text: String, val time: String, val postIndex: Int? = null)

private fun c(hex: Long) = Color(hex)

object DummyData {
    val me = User("me", "Alex Morgan", "alex.morgan", "Designer • Coffee lover ☕ • Exploring the world one city at a time 🌍",
        listOf(c(0xFF7B2FF7), c(0xFFE1306C)), 1284, 342)

    val users = listOf(
        User("u1", "Sophia Lee", "sophia.lee", "Travel photographer 📸", listOf(c(0xFFFF9A8B), c(0xFFFF6A88)), 24500, 410, true),
        User("u2", "Liam Carter", "liamcarter", "Chef & food blogger 🍝", listOf(c(0xFF43CEA2), c(0xFF185A9D)), 8930, 512),
        User("u3", "Mia Johnson", "mia.j", "Fitness | Yoga | Wellness", listOf(c(0xFFF7971E), c(0xFFFFD200)), 15200, 288, true),
        User("u4", "Noah Patel", "noahpatel", "Software engineer & gamer 🎮", listOf(c(0xFF4568DC), c(0xFFB06AB3)), 3120, 645),
        User("u5", "Emma Wilson", "emmaw", "Artist 🎨 Painting daily", listOf(c(0xFFEE0979), c(0xFFFF6A00)), 41800, 199, true),
        User("u6", "Oliver Brown", "oliver.b", "Music producer 🎧", listOf(c(0xFF00C6FF), c(0xFF0072FF)), 6740, 380),
        User("u7", "Ava Garcia", "ava.garcia", "Fashion & lifestyle ✨", listOf(c(0xFFDA22FF), c(0xFF9733EE)), 52100, 512, true),
        User("u8", "Ethan Kim", "ethankim", "Hiker • Mountains ⛰️", listOf(c(0xFF56AB2F), c(0xFFA8E063)), 2890, 410),
    )

    private val u = users

    val posts = mutableStateListOf(
        Post(1, u[0], "Golden hour in Santorini never gets old 🌅 #travel #greece", "🌅", listOf(c(0xFFFF9966), c(0xFFFF5E62)), 2843, "2h", "Santorini, Greece",
            listOf(Comment(u[4], "Absolutely stunning! 😍", "1h"), Comment(u[2], "Adding this to my bucket list", "45m"))),
        Post(2, u[1], "Homemade pasta night 🍝 Recipe dropping tomorrow!", "🍝", listOf(c(0xFFF7971E), c(0xFFFFD200)), 1204, "4h", "Rome, Italy",
            listOf(Comment(u[3], "That looks incredible", "3h"))),
        Post(3, u[2], "Morning flow complete ✅ Remember: progress, not perfection.", "🧘", listOf(c(0xFF43CEA2), c(0xFF185A9D)), 932, "6h", null,
            listOf(Comment(u[0], "Needed this today 🙏", "5h"), Comment(u[6], "So inspiring!", "4h"), Comment(u[7], "🔥🔥", "2h"))),
        Post(4, u[4], "New piece finished! 'Ocean Dreams' — acrylic on canvas 🎨", "🎨", listOf(c(0xFF00C6FF), c(0xFF0072FF)), 5621, "8h", "Studio",
            listOf(Comment(u[5], "The colors are unreal", "7h"))),
        Post(5, u[7], "Made it to the summit! 3,200m and worth every step ⛰️", "⛰️", listOf(c(0xFF56AB2F), c(0xFFA8E063)), 1780, "12h", "Swiss Alps"),
        Post(6, u[6], "Autumn fits are here 🍂 Which look is your fave?", "🍂", listOf(c(0xFFDA22FF), c(0xFF9733EE)), 8930, "1d", "Paris, France",
            listOf(Comment(u[0], "The second one for sure!", "20h"), Comment(u[4], "Obsessed 💜", "18h"))),
        Post(7, u[5], "Late night studio session 🎧 New track coming soon", "🎧", listOf(c(0xFF232526), c(0xFF414345)), 642, "1d", "Berlin"),
        Post(8, u[3], "Finally finished my setup upgrade 💻✨", "💻", listOf(c(0xFF4568DC), c(0xFFB06AB3)), 1103, "2d", null,
            listOf(Comment(u[1], "Clean setup!", "1d"))),
    )

    val myPosts = listOf(
        Post(101, me, "Weekend in Lisbon 🇵🇹", "🏙️", listOf(c(0xFFFF9A8B), c(0xFFFF6A88)), 312, "3d", "Lisbon"),
        Post(102, me, "Best latte art yet ☕", "☕", listOf(c(0xFF8E6E53), c(0xFFD7B899)), 198, "5d"),
        Post(103, me, "Sunset run 🏃", "🌇", listOf(c(0xFFF7971E), c(0xFFFFD200)), 254, "1w"),
        Post(104, me, "New design system shipped 🚀", "🚀", listOf(c(0xFF4568DC), c(0xFFB06AB3)), 421, "1w"),
        Post(105, me, "Beach day 🌊", "🌊", listOf(c(0xFF00C6FF), c(0xFF0072FF)), 389, "2w"),
        Post(106, me, "Plant parent life 🌿", "🌿", listOf(c(0xFF56AB2F), c(0xFFA8E063)), 167, "2w"),
        Post(107, me, "Concert night 🎶", "🎶", listOf(c(0xFFDA22FF), c(0xFF9733EE)), 276, "3w"),
        Post(108, me, "Sunday brunch 🥞", "🥞", listOf(c(0xFFFF9966), c(0xFFFF5E62)), 203, "1mo"),
        Post(109, me, "City lights 🌃", "🌃", listOf(c(0xFF232526), c(0xFF414345)), 340, "1mo"),
    )

    val explore: List<Post> = (posts + myPosts.reversed()).shuffled(kotlin.random.Random(7))

    val conversations = mutableStateListOf(
        Conversation(u[0], listOf(
            Message(false, "Hey! Loved your Lisbon photos 😍", "10:02"),
            Message(true, "Thank you!! It was such a great trip", "10:05"),
            Message(false, "We should plan a photo walk sometime", "10:06"),
        ), unread = 1),
        Conversation(u[1], listOf(
            Message(true, "That pasta looked amazing", "Yesterday"),
            Message(false, "Haha come over, I'll make you some 🍝", "Yesterday"),
        ), unread = 2),
        Conversation(u[3], listOf(
            Message(false, "Game night on Friday?", "Mon"),
            Message(true, "I'm in! 🎮", "Mon"),
        )),
        Conversation(u[6], listOf(
            Message(false, "Can I use your design for my next post?", "Sun"),
            Message(true, "Of course, just tag me ✨", "Sun"),
        )),
        Conversation(u[5], listOf(Message(false, "Sent you the new track 🎧 let me know!", "Sat"))),
    )

    val notifications = listOf(
        Notification(u[0], NotifType.LIKE, "liked your photo.", "2m", 0),
        Notification(u[4], NotifType.FOLLOW, "started following you.", "15m"),
        Notification(u[2], NotifType.COMMENT, "commented: \"This is so cool! 🔥\"", "1h", 1),
        Notification(u[6], NotifType.MENTION, "mentioned you in a comment.", "3h", 2),
        Notification(u[1], NotifType.LIKE, "and 24 others liked your photo.", "5h", 3),
        Notification(u[7], NotifType.FOLLOW, "started following you.", "1d"),
        Notification(u[5], NotifType.LIKE, "liked your comment.", "1d"),
        Notification(u[3], NotifType.COMMENT, "commented: \"Great shot!\"", "2d", 4),
        Notification(u[0], NotifType.FOLLOW, "started following you.", "3d"),
    )

    val following = mutableStateListOf("u1", "u2", "u5")

    fun formatCount(n: Int): String = when {
        n >= 1_000_000 -> String.format("%.1fM", n / 1_000_000.0)
        n >= 10_000 -> "${n / 1000}K"
        n >= 1_000 -> String.format("%.1fK", n / 1000.0)
        else -> n.toString()
    }
}
