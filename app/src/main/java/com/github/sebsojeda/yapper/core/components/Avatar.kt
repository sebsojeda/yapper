
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.github.sebsojeda.yapper.core.Constants
import com.github.sebsojeda.yapper.features.user.domain.model.User
import io.github.jan.supabase.storage.publicStorageItem

@Composable
fun Avatar(user: User, size: Int, modifier: Modifier = Modifier) {
    if (user.avatar != null) {
        AsyncImage(
            modifier = modifier
                .size(size.dp)
                .clip(CircleShape),
            model = publicStorageItem(Constants.BUCKET_PUBLIC_MEDIA, user.avatar.path),
            contentScale = ContentScale.Crop,
            contentDescription = user.username,
        )
    } else {
        val letter = user.username.first().uppercase()
        val backgroundColor = getUsernameBasedColor(user.username)
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
                .size(size.dp)
                .background(color = backgroundColor, shape = CircleShape)
        ) {
            Text(
                text = letter.toString(),
                fontSize = (size / 2).sp, // Dynamic font size based on the size of the avatar
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

private fun getUsernameBasedColor(username: String): Color {
    val hashCode = username.hashCode()
    return Color(
        red = (hashCode and 0xFF0000 shr 16) / 255.0f,
        green = (hashCode and 0x00FF00 shr 8) / 255.0f,
        blue = (hashCode and 0x0000FF) / 255.0f,
        alpha = 1f
    )
}

@Preview(showBackground = true)
@Composable
fun AvatarPreview() {
    Avatar(
        user = User(
            id = "1",
            username = "sebsojeda",
            name = "Sebastian Ojeda",
            createdAt = "",
            avatar = null
        ),
        size = 48,
    )
}
