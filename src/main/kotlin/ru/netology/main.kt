package ru.netology

import ru.netology.service.NoteService

fun main() {
    val noteService = NoteService()
    noteService.add("Title1", "Text1")
    noteService.add("Title2", "Text2")
    noteService.add("Title3", "Text3")
    noteService.add("Title4", "Text4")
//  println(noteService.get(listOf(1),1234))
//  println(noteService.get(listOf(2),1234))
//  println(noteService.get(listOf(3),1234))
    noteService.createComment(1,1234,null,"Comment11")
    noteService.createComment(1,1234,null,"Comment21")
    noteService.createComment(2,1234,null,"Comment12")
//  println(noteService.get(listOf(1),1234))
//  println(noteService.get(listOf(2),1234))

    noteService.delete(4)
    noteService.deleteComment(2,1234)
    noteService.restoreComment(2,1234)
    noteService.edit(1, "New Title1", "New Text1")
    noteService.editComment(2,1234,"New Comment")
    noteService.get(listOf(1,3), 1234)
    noteService.getById(1, 1234)
    noteService.getComments(1,1234)
//    println(noteService.get(listOf(1),1234))
//    println(noteService.get(listOf(2),1234))
//    println(noteService.get(listOf(3),1234))
}