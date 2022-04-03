package ru.netology.service

import ru.netology.data.Comment
import ru.netology.data.Note
import ru.netology.data.GenericPair
import java.lang.RuntimeException

class NoteNotFoundException(message: String) : RuntimeException(message)
class UserNotFoundException(message: String) : RuntimeException(message)
class AddCommentException(message: String) : RuntimeException(message)
class GetCommentException(message: String) : RuntimeException(message)
class NoCommentException(message: String) : RuntimeException(message)
class GetNoteException(message: String) : RuntimeException(message)

class NoteService {

    private var records = mutableListOf<GenericPair<Note, MutableList<Comment>>>()
    private var deletedRecords = mutableListOf<GenericPair<Note, MutableList<Comment>>>()
    private var deletedComments = mutableListOf<Comment>()

    // Создает новую заметку у текущего пользователя
    fun add(
        title: String,
        text: String,
        privacyView: List<String> = listOf("all"),
        privacyComment: List<String> = listOf("all")
    ) : Int {
        if(records.isEmpty()) {
            records.add(GenericPair(Note(1, 1234, title, text, 991985, 0, 0, "viewUrl"), mutableListOf()))
        } else {
            records.add(GenericPair(Note(records.last().first.id + 1, 1234, title, text, 991985, 0, 0, "viewUrl"), mutableListOf()))
        }
        return records.last().first.id
    }

    // Добавляет новый комментарий к заметке
    fun createComment(
        noteId: Int,
        ownerId: Int,
        replyTo: Int?,
        message: String
    ) : Int {
        if (records.isNotEmpty()) {
            for (record in records) {
                if (record.first.id == noteId && record.first.ownerId == ownerId) {
                    if (record.second.isEmpty()) {
                        record.second.add(Comment(1,1, noteId, ownerId, message, replyTo))
                    } else {
                        record.second.add(Comment(record.second.last().commentId + 1, 1, noteId, ownerId, message, replyTo))
                    }
                    record.first = record.first.copy(comments = record.first.comments + 1)
                    return record.second.last().commentId
                }
            }
            throwUserNotFoundException (ownerId)
            throwNoteNotFoundException (noteId)
        }
        throw AddCommentException("Нельзя добавить комментарий к несуществующей заметке")
    }

    // Удаляет заметку текущего пользователя
    fun delete(noteId: Int) : Boolean {
        for(record in records){
            if(record.first.id == noteId){
                deletedRecords.add(record)
                records.remove(record)
                return true
            }
        }
        return false
    }

    // Удаляет комментарий к заметке.
    fun deleteComment(commentId: Int, ownerId: Int) : Boolean {
        for(i in records.indices){
            for(j in records[i].second.indices) {
                if(records[i].second[j].ownerId == ownerId && records[i].second[j].commentId == commentId){
                    deletedComments.add(records[i].second[j])
                    records[i].second.remove(records[i].second[j])
                    return true
                }
            }
        }
        throwUserNotFoundException(ownerId)
        throwNoCommentException(commentId)
        return false
    }

    //  Редактирует заметку текущего пользователя.
    fun edit(
        noteId: Int,
        title: String,
        text: String,
        privacyView: List<String> = listOf("all"),
        privacyComment: List<String> = listOf("all"),
    ) : Boolean {
        if(records.isNotEmpty()){
            for(record in records){
                if(record.first.id == noteId){
                    record.first = record.first.copy(title = title, text = text)
                    return true
                }
            }
            throwNoteNotFoundException(noteId)
        }
        throwNoteNotFoundException(noteId)
        return false
    }

    //  Редактирует указанный комментарий у заметки.
    fun editComment(commentId: Int, ownerId: Int, message: String) : Boolean {
        for(i in records.indices){
            for(j in records[i].second.indices) {
                if(records[i].second[j].ownerId == ownerId && records[i].second[j].commentId == commentId){
                    records[i].second[j] = records[i].second[j].copy(message = message)
                    return true
                }
            }
        }
        throwUserNotFoundException(ownerId)
        throwNoCommentException(commentId)
        return false
    }

    //  Возвращает список заметок, созданных пользователем.
    fun get(noteIds: List<Int>, userId: Int, offset: Int = 0, count: Int = 20, sort: Int = 0): MutableList<Note> {
        val list = mutableListOf<Note>()

        if(records.isNotEmpty()){
            for(i in noteIds.indices){
                for(j in records.indices) {
                    if(records[j].first.ownerId == userId && records[j].first.id == noteIds[i]) {
                        list.add(records[j].first)
                        break
                    }
                }
            }
            throwUserNotFoundException(userId)
            noteIds.forEach { throwNoteNotFoundException(it) }
            return list
        }
        throw GetNoteException("Список заметок пуст!")
    }

    //  Возвращает заметку по её id.
    fun getById(noteId: Int, ownerId: Int, needWiki: Boolean = false): MutableList<Note> {
        return get(listOf(noteId), ownerId)
    }

    //  Возвращает список комментариев к заметке.
    fun getComments(
        noteId: Int,
        ownerId: Int,
        sort: Int = 0,
        offset: Int = 0,
        count: Int = 20
    ): MutableList<Comment> {
        val list: MutableList<Comment>
        if (records.isNotEmpty()) {
            for (record in records) {
                if (record.first.id == noteId && record.first.ownerId == ownerId) {
                    if (record.second.isEmpty()) {
                        throw NoCommentException("У заметки с таким id нет комметариев")
                    } else {
                        list = record.second
                    }
                    return list
                }
            }
            throwUserNotFoundException(ownerId)
            throwNoteNotFoundException(noteId)
        }
        throw GetCommentException("Нельзя получить список комментариев к несуществующей заметке")
    }

    //  Восстанавливает удалённый комментарий.
    fun restoreComment(commentId: Int, ownerId: Int): Boolean {
        for(i in deletedComments.indices) {
            if(deletedComments[i].ownerId == ownerId && deletedComments[i].commentId == commentId) {
                for(j in records.indices){
                    if(records[i].first.ownerId == ownerId) {
                        records[i].second.add(deletedComments[i])
                        deletedComments.remove(records[i].second[j])
                        return true
                    }
                }
            }
        }
        throwUserNotFoundException(ownerId)
        throwNoCommentException(commentId)
        return false
    }

    private fun throwNoteNotFoundException(noteId: Int) {
        var isIdEqual = false
        for (record in records) {
            if (record.first.id == noteId) isIdEqual = true
        }
        if (!isIdEqual) throw NoteNotFoundException("Заметка с id $noteId не существует")
    }

    private fun throwUserNotFoundException(ownerId: Int) {
        var isOwnerIdEqual = false
        for (record in records) {
            if (record.first.ownerId == ownerId) isOwnerIdEqual = true
        }
        if (!isOwnerIdEqual) throw UserNotFoundException("Пользователя с id $ownerId не существует")
    }

    private fun throwNoCommentException(commentId: Int) {
        var isCommentIdEqual = false
        for(i in records.indices){
            for(j in records[i].second.indices) {
                if(records[i].second[j].commentId == commentId) isCommentIdEqual = true
            }
        }
        if (!isCommentIdEqual) throw NoCommentException("Комментария с id $commentId не существует")
    }
}