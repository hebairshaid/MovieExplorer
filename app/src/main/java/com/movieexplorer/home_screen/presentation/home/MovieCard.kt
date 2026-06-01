package com.movieexplorer.home_screen.presentation.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.movieexplorer.home_screen.domain.model.Movie
import com.movieexplorer.ui.theme.CardBlue
import com.movieexplorer.ui.theme.Gold

@Composable
fun MovieCard(
    movie: Movie,
    genre: String
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardBlue
        ),
        shape = RoundedCornerShape(20.dp)
    ) {

        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            AsyncImage(
                model = "https://image.tmdb.org/t/p/w500${movie.posterPath}",
                contentDescription = movie.title,
                modifier = Modifier
                    .width(95.dp)
                    .height(140.dp)
                    .clip(RoundedCornerShape(14.dp))
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {

                // TITLE
                Text(
                    text = movie.title,
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2
                )

                Spacer(modifier = Modifier.height(2.dp))

                // ⭐ RATING (WHITE + GRAY /10)
                Row(verticalAlignment = Alignment.CenterVertically) {

                    Text(
                        text = "⭐ ${movie.rating}",
                        color = Color.White,
                        fontSize = 12.sp
                    )

                    Text(
                        text = " /10",
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                }


                Spacer(modifier = Modifier.height(60.dp))

                AssistChip(
                    onClick = {},
                    label = {
                        Text(
                            text = genre,
                            color = Gold,
                            fontSize = 11.sp
                        )
                    },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = Color(0xFF1B2559)
                    ),
                    border = null,
                    shape = RoundedCornerShape(50),
                    modifier = Modifier.height(28.dp)
                )
            }
        }
    }
}