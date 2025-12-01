package com.example.flashcard // Убедись, что пакет совпадает с твоим проектом!

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// 1. Модель данных для Карточки
data class Flashcard(
    val original: String,
    val translated: String
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    FlashcardScreen()
                }
            }
        }
    }
}

@Composable
fun FlashcardScreen() {
    // 2. Список слов
    val flashcards = remember {
        listOf(
            Flashcard("Communication", "Общение"),
            Flashcard("Developer", "Разработчик"),
            Flashcard("Code", "Код"),
            Flashcard("Purpose", "Цель"),
            Flashcard("Memory", "Память"),
            Flashcard("Permission", "Разрешение"),
            Flashcard("Internet", "Интернет"),
            Flashcard("To ensure", "Обеспечивать"),
            Flashcard("Critical", "Критически важный")
        )
    }

    // Состояния экрана
    var currentIndex by remember { mutableIntStateOf(0) }
    var isFlipped by remember { mutableStateOf(false) }

    // Анимация поворота (0f - лицо, 180f - оборот)
    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = tween(durationMillis = 400),
        label = "CardRotation"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Счетчик
        Text(
            text = "Карточка ${currentIndex + 1} из ${flashcards.size}",
            fontSize = 18.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // 3. Сама карточка
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .graphicsLayer {
                    rotationY = rotation // Применяем поворот
                    cameraDistance = 12f * density // Эффект 3D
                }
                .clickable { isFlipped = !isFlipped } // Переворот по клику
                .background(
                    color = if (isFlipped) Color(0xFFE3F2FD) else Color(0xFFFFF3E0),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(16.dp)
        ) {
            // Текст внутри карточки
            Text(
                // Если повернуто больше чем на 90 градусов, показываем перевод
                text = if (rotation <= 90f) flashcards[currentIndex].original else flashcards[currentIndex].translated,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.graphicsLayer {
                    // Если карта перевернута, текст тоже нужно "отзеркалить" обратно, чтобы он читался нормально
                    if (rotation > 90f) {
                        rotationY = 180f
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(48.dp))

        // 4. Кнопки управления
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            // Кнопка "Назад"
            Button(
                onClick = {
                    if (currentIndex > 0) {
                        currentIndex--
                        isFlipped = false // Сбрасываем переворот
                    }
                },
                modifier = Modifier.weight(1f),
                enabled = currentIndex > 0
            ) {
                Text("Назад")
            }

            // Кнопка "Далее"
            Button(
                onClick = {
                    if (currentIndex < flashcards.size - 1) {
                        currentIndex++
                        isFlipped = false // Сбрасываем переворот
                    } else {
                        // Если дошли до конца, начинаем сначала
                        currentIndex = 0
                        isFlipped = false
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text(if (currentIndex < flashcards.size - 1) "Далее" else "Заново")
            }
        }
    }
}