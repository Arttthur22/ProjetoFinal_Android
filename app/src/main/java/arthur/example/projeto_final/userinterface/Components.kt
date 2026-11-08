package arthur.example.projeto_final.userinterface

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.CloudOff
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Movie
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import arthur.example.projeto_final.data.Movie
import arthur.example.projeto_final.userinterface.theme.Accent
import arthur.example.projeto_final.userinterface.theme.AccentSoft
import arthur.example.projeto_final.userinterface.theme.Muted
import arthur.example.projeto_final.userinterface.theme.OutlineColor
import arthur.example.projeto_final.userinterface.theme.StarColor
import arthur.example.projeto_final.userinterface.theme.SurfaceDark
import arthur.example.projeto_final.userinterface.theme.SurfaceLight

@Composable
fun statusBarTop(): Dp = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

@Composable
fun BrandLogo(modifier: Modifier = Modifier) {
    Row(modifier, verticalAlignment = Alignment.CenterVertically) {
        Box(
            Modifier.size(39.dp).clip(RoundedCornerShape(11.dp)).background(Accent),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Outlined.Movie, contentDescription = null, tint = Color.White, modifier = Modifier.size(22.dp))
        }
        Spacer(Modifier.width(10.dp))
        Text(
            buildAnnotatedString {
                append("Cine")
                withStyle(SpanStyle(color = Color(0xFFEF3943), fontWeight = FontWeight.Bold)) { append("Catalog") }
            },
            fontSize = 21.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    loading: Boolean = false,
    enabled: Boolean = true,
    secondary: Boolean = false,
    destructive: Boolean = false,
    icon: ImageVector? = null
) {
    Button(
        onClick = onClick,
        enabled = enabled && !loading,
        modifier = modifier.heightIn(min = 48.dp),
        shape = RoundedCornerShape(13.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (secondary) SurfaceLight else Accent,
            contentColor = if (secondary && destructive) AccentSoft else Color.White,
            disabledContainerColor = if (secondary) SurfaceLight else Accent.copy(alpha = 0.6f),
            disabledContentColor = Color.White.copy(alpha = 0.85f)
        ),
        border = if (secondary) BorderStroke(1.dp, Color(0xFF3A4458)) else null
    ) {
        if (loading) {
            CircularProgressIndicator(Modifier.size(17.dp), color = Color.White, strokeWidth = 2.dp)
            Spacer(Modifier.width(9.dp))
        } else if (icon != null) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(9.dp))
        }
        Text(text, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    isPassword: Boolean = false,
    isError: Boolean = false,
    onDone: (() -> Unit)? = null
) {
    var visible by remember { mutableStateOf(false) }
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        label = { Text(label) },
        placeholder = { Text(placeholder, color = Muted.copy(alpha = 0.6f)) },
        singleLine = true,
        isError = isError,
        shape = RoundedCornerShape(12.dp),
        visualTransformation = if (isPassword && !visible) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(
            keyboardType = if (isPassword) KeyboardType.Password else keyboardType,
            imeAction = imeAction
        ),
        keyboardActions = KeyboardActions(onDone = { onDone?.invoke() }),
        trailingIcon = if (isPassword) {
            {
                IconButton(onClick = { visible = !visible }) {
                    Icon(
                        if (visible) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                        contentDescription = if (visible) "Ocultar senha" else "Mostrar senha"
                    )
                }
            }
        } else null,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = SurfaceDark,
            unfocusedContainerColor = SurfaceDark,
            errorContainerColor = SurfaceDark,
            focusedBorderColor = Accent,
            unfocusedBorderColor = OutlineColor,
            errorBorderColor = AccentSoft,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            errorTextColor = Color.White,
            cursorColor = Accent,
            focusedLabelColor = Accent,
            unfocusedLabelColor = Muted,
            errorLabelColor = AccentSoft,
            unfocusedTrailingIconColor = Muted,
            focusedTrailingIconColor = Muted,
            errorTrailingIconColor = Muted
        )
    )
}

@Composable
fun PageHeader(eyebrow: String, title: String, subtitle: String? = null, modifier: Modifier = Modifier) {
    Column(modifier) {
        Text(eyebrow, style = MaterialTheme.typography.labelSmall, color = AccentSoft)
        Spacer(Modifier.height(6.dp))
        Text(title, style = MaterialTheme.typography.headlineMedium, color = Color.White)
        if (subtitle != null) {
            Spacer(Modifier.height(4.dp))
            Text(subtitle, fontSize = 14.sp, color = Muted)
        }
    }
}

@Composable
fun FilterChip(label: String, selected: Boolean, onClick: () -> Unit) {
    Box(
        Modifier
            .clip(CircleShape)
            .background(if (selected) Accent else SurfaceDark)
            .border(1.dp, if (selected) Accent else Color(0xFF343E51), CircleShape)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 9.dp)
    ) {
        Text(label, fontSize = 12.sp, color = if (selected) Color.White else Color(0xFFC3C8D2), maxLines = 1)
    }
}

@Composable
fun InfoChip(label: String) {
    Box(
        Modifier.clip(CircleShape).background(SurfaceDark).padding(horizontal = 13.dp, vertical = 8.dp)
    ) {
        Text(label, fontSize = 12.sp, color = Color(0xFFC3C8D2), maxLines = 1)
    }
}

@Composable
fun RatingBadge(rating: String, modifier: Modifier = Modifier) {
    Row(
        modifier.clip(RoundedCornerShape(8.dp)).background(Color(0xDB0A0D14)).padding(horizontal = 7.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Filled.Star, contentDescription = null, tint = StarColor, modifier = Modifier.size(12.dp))
        Spacer(Modifier.width(4.dp))
        Text(rating, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
    }
}

@Composable
fun MovieCard(
    movie: Movie,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    posterHeight: Dp = 228.dp
) {
    Column(modifier.clickable(onClick = onClick)) {
        Box(
            Modifier.fillMaxWidth().height(posterHeight).clip(RoundedCornerShape(14.dp)).background(SurfaceDark)
        ) {
            AsyncImage(
                model = movie.image,
                contentDescription = "Pôster de ${movie.title}",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            RatingBadge(movie.rating, Modifier.align(Alignment.BottomStart).padding(8.dp))
            IconButton(
                onClick = onFavoriteClick,
                modifier = Modifier.align(Alignment.TopEnd).padding(4.dp).size(38.dp)
                    .clip(CircleShape).background(Color(0xDB0A0D14))
            ) {
                Icon(
                    if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = if (isFavorite) "Remover dos favoritos" else "Adicionar aos favoritos",
                    tint = if (isFavorite) Color(0xFFFF3943) else Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
        Spacer(Modifier.height(10.dp))
        Text(movie.title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White, maxLines = 1, overflow = TextOverflow.Ellipsis)
        Spacer(Modifier.height(4.dp))
        Text("${movie.year} · ${movie.director}", fontSize = 11.sp, color = Muted, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}

@Composable
fun LoadingState(modifier: Modifier = Modifier, message: String = "Carregando filmes...") {
    Column(
        modifier.fillMaxWidth().height(420.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(color = Accent)
        Spacer(Modifier.height(16.dp))
        Text(message, color = Muted, fontSize = 13.sp)
    }
}

@Composable
fun StateMessage(
    icon: ImageVector,
    title: String,
    text: String,
    modifier: Modifier = Modifier,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null
) {
    Column(
        modifier.fillMaxWidth().height(420.dp).padding(horizontal = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            Modifier.size(72.dp).clip(CircleShape).background(Accent.copy(alpha = 0.08f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = AccentSoft, modifier = Modifier.size(30.dp))
        }
        Spacer(Modifier.height(20.dp))
        Text(title, style = MaterialTheme.typography.titleLarge, color = Color.White, textAlign = TextAlign.Center)
        Spacer(Modifier.height(8.dp))
        Text(text, fontSize = 13.sp, lineHeight = 19.sp, color = Muted, textAlign = TextAlign.Center)
        if (actionLabel != null && onAction != null) {
            Spacer(Modifier.height(20.dp))
            PrimaryButton(actionLabel, onClick = onAction)
        }
    }
}

@Composable
fun ErrorState(message: String, onRetry: () -> Unit, modifier: Modifier = Modifier) {
    StateMessage(
        icon = Icons.Outlined.CloudOff,
        title = "Algo deu errado",
        text = message,
        modifier = modifier,
        actionLabel = "Tentar novamente",
        onAction = onRetry
    )
}

@Composable
fun gridPadding(extraTop: Dp = 24.dp) =
    PaddingValues(start = 20.dp, end = 20.dp, top = statusBarTop() + extraTop, bottom = 24.dp)
