
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import io.github.jan.supabase.storage.publicStorageItem
import kotlin.math.abs

@Composable
fun Avatar(
    imageUrl: String?,
    displayName: String,
    size: Int,
    modifier: Modifier = Modifier,
) {
    val colorList = remember {
        listOf(
            Color(0xFFEF4444),
            Color(0xFFF97316),
            Color(0xFFF59E0B),
            Color(0xFFEAB308),
            Color(0xFF84CC16),
            Color(0xFF22C55E),
            Color(0xFF10B981),
            Color(0xFF14B8A6),
            Color(0xFF06B6D4),
            Color(0xFF0EA5E9),
            Color(0xFF3B82F6),
            Color(0xFF8B5CF6),
            Color(0xFFA855F7),
            Color(0xFFD946EF),
            Color(0xFFEC4899),
            Color(0xFFF43F5E),
        )
    }
    fun getDynamicColor(string: String) = colorList[abs(string.hashCode()) % colorList.size]
    val backgroundColor = remember(displayName) { getDynamicColor(displayName) }

    if (imageUrl != null) {
        AsyncImage(
            modifier = modifier
                .size(size.dp)
                .clip(CircleShape),
            model = publicStorageItem(Constants.BUCKET_PUBLIC_MEDIA, imageUrl),
            contentScale = ContentScale.Crop,
            contentDescription = displayName,
        )
    } else {
        val letter = displayName.first().uppercase()
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
                .size(size.dp)
                .background(color = backgroundColor, shape = CircleShape)
        ) {
            Text(
                text = letter,
                fontSize = (size / 2).sp, // Dynamic font size based on the size of the avatar
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AvatarPreview() {
    Avatar(
        imageUrl = null,
        displayName = "John Doe",
        size = 48,
    )
}
