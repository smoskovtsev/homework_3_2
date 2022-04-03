package ru.netology.data

data class Note(
    val id: Int, // идентификатор заметки
    val ownerId: Int, // идентификатор владельца заметки
    val title: String, // заголовок заметки
    val text: String, // текст заметки
    val date: Int, // дата создания заметки в формате Unixtime
    var comments: Int, // количество комментариев
    val readComments: Int, // количество прочитанных комментариев (только при запросе информации о заметке текущего пользователя)
    val viewUrl: String // URL страницы для отображения заметки
)