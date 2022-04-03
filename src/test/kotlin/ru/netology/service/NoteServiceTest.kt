package ru.netology.service

import org.junit.jupiter.api.Assertions.*
import org.junit.Test

class NoteServiceTest {

    @Test
    fun addNoteToEmptyList() {
        val noteService = NoteService()
        val lastNote = noteService.add("Title", "Text")
        assertTrue(lastNote > 0)
    }

    @Test
    fun addNoteToExistingList() {
        val noteService = NoteService()
        noteService.add("Title", "Text")
        val lastNote = noteService.add("Title", "Text")
        assertTrue(lastNote > 0)
    }


    @Test
    fun addCommentToEmptyList() {
        val noteService = NoteService()
        noteService.add("Title", "Text", listOf("all"), listOf("all"))
        val lastComment = noteService.createComment(1,1234,null,"message")
        assertTrue(lastComment > 0)
    }

    @Test
    fun addCommentToExistingList() {
        val noteService = NoteService()
        noteService.add("Title", "Text", listOf("all"), listOf("all"))
        noteService.createComment(1,1234,null,"message")
        val lastComment = noteService.createComment(1,1234,null,"message")
        assertTrue(lastComment > 0)
    }

    @Test(expected = NoteNotFoundException::class)
    fun addCommentNoteNotFoundException() {
        val noteService = NoteService()
        noteService.add("Title", "Text", listOf("all"), listOf("all"))
        noteService.createComment(2,1234,null,"message")
    }

    @Test(expected = UserNotFoundException::class)
    fun addCommentUserNotFoundException() {
        val noteService = NoteService()
        noteService.add("Title1", "Text1", listOf("all"), listOf("all"))
        noteService.add("Title2", "Text2", listOf("all"), listOf("all"))
        noteService.createComment(2,1235,null,"message")
    }

    @Test(expected = AddCommentException::class)
    fun addCommentAddCommentException() {
        val noteService = NoteService()
        noteService.createComment(1,1234,null,"message")
    }

    @Test
    fun getComments() {
        val noteService = NoteService()
        noteService.add("Title", "Text", listOf("all"), listOf("all"))
        noteService.createComment(1,1234,null,"message")
        assertTrue(noteService.getComments(1,1234).isNotEmpty())
    }

    @Test(expected = NoCommentException::class)
    fun getCommentsThrowNoCommentException() {
        val noteService = NoteService()
        noteService.add("Title", "Text", listOf("all"), listOf("all"))
        noteService.getComments(1,1234)
    }

    @Test(expected = NoteNotFoundException::class)
    fun getCommentsThrowNoteNotFoundException() {
        val noteService = NoteService()
        noteService.add("Title", "Text", listOf("all"), listOf("all"))
        noteService.getComments(2,1234)
    }

    @Test(expected = UserNotFoundException::class)
    fun getCommentThrowUserNotFoundException() {
        val noteService = NoteService()
        noteService.add("Title", "Text", listOf("all"), listOf("all"))
        noteService.getComments(1,1235)
    }
    @Test(expected = GetCommentException::class)
    fun getCommentsThrowGetCommentException() {
        val noteService = NoteService()
        noteService.getComments(1,1234)
    }

    @Test
    fun deleteComment() {
        val noteService = NoteService()
        noteService.add("Title", "Text", listOf("all"), listOf("all"))
        noteService.createComment(1,1234,null,"message")
        assertTrue(noteService.deleteComment(1,1234))
    }

    @Test
    fun deleteNote() {
        val noteService = NoteService()
        noteService.add("Title", "Text", listOf("all"), listOf("all"))
        assertTrue(noteService.delete(1))
    }

    @Test
    fun deleteNoteFail() {
        val noteService = NoteService()
        noteService.add("Title", "Text", listOf("all"), listOf("all"))
        assertFalse(noteService.delete(2))
    }

    @Test(expected = NoCommentException::class)
    fun throwNoCommentException() {
        val noteService = NoteService()
        noteService.add("Title", "Text", listOf("all"), listOf("all"))
        noteService.createComment(1,1234,null,"message")
        noteService.deleteComment(3,1234)
    }

    @Test
    fun getNote() {
        val noteService = NoteService()
        noteService.add("Title", "Text", listOf("all"), listOf("all"))
        noteService.get(listOf(1), 1234)
    }

    @Test(expected = GetNoteException::class)
    fun getGetNoteException() {
        val noteService = NoteService()
        noteService.get(listOf(1), 1234)
    }
    @Test
    fun getById() {
        val noteService = NoteService()
        noteService.add("Title", "Text", listOf("all"), listOf("all"))
        noteService.getById(1, 1234)
    }

    @Test
    fun editNote() {
        val noteService = NoteService()
        noteService.add("Title", "Text")
        assertTrue(noteService.edit(1, "New Title", "New Text"))
    }

    @Test(expected = NoteNotFoundException::class)
    fun editThrowNoteNotFoundException() {
        val noteService = NoteService()
        noteService.add("Title", "Text", listOf("all"), listOf("all"))
        noteService.edit(2, "New title", "New text")
    }

    @Test
    fun editComment() {
        val noteService = NoteService()
        noteService.add("Title", "Text", listOf("all"), listOf("all"))
        noteService.createComment(1,1234,null,"message")
        assertTrue(noteService.editComment(1,1234,"New message"))
    }

    @Test(expected = NoCommentException::class)
    fun editCommentThrowNoCommentException() {
        val noteService = NoteService()
        noteService.add("Title", "Text", listOf("all"), listOf("all"))
        noteService.createComment(1,1234,null,"message")
        noteService.editComment(2,1234,"New message")
    }

    @Test(expected = UserNotFoundException::class)
    fun editCommentThrowUserNotFoundException() {
        val noteService = NoteService()
        noteService.add("Title", "Text", listOf("all"), listOf("all"))
        noteService.createComment(1,1234,null,"message")
        noteService.editComment(1,1235,"New message")
    }

    @Test
    fun restoreComment() {
        val noteService = NoteService()
        noteService.add("Title", "Text", listOf("all"), listOf("all"))
        noteService.createComment(1,1234,null,"message")
        noteService.deleteComment(1,1234)
        assertTrue(noteService.restoreComment(1,1234))
    }
    @Test(expected = NoCommentException::class)
    fun restoreCommentThrowNoCommentException() {
        val noteService = NoteService()
        noteService.add("Title", "Text", listOf("all"), listOf("all"))
        noteService.createComment(1,1234,null,"message")
        noteService.deleteComment(1,1234)
        noteService.restoreComment(2,1234)
    }

    @Test(expected = UserNotFoundException::class)
    fun restoreCommentThrowUserNotFoundException() {
        val noteService = NoteService()
        noteService.add("Title", "Text")
        noteService.createComment(1,1234,null,"message")
        noteService.deleteComment(1,1234)
        noteService.restoreComment(1,1234)
    }
}