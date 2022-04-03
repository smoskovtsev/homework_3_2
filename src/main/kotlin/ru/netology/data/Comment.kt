package ru.netology.data

data class Comment(
    val commentId: Int, // Идентификатор комментария
    val userId: Int, // Идентификатор автора комментария
    val id: Int, // Идентификатор заметки
    val ownerId: Int,  // Идентификатор владельца заметки
    val message: String, // Текст комментария
    val replyTo: Int?, // Идентификатор пользователя, в ответ на комментарий которого был оставлен комментарий
)