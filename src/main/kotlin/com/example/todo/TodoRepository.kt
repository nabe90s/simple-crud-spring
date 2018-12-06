package com.example.todo

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import java.sql.ResultSet

interface TodoRepository {

    fun create(content: String): Todo

    fun update(todo: Todo)

    fun findAll(): List<Todo>

    fun findById(id: TodoId): Todo?

}

class InMemoryTodoRepository : TodoRepository {

    private val todos: MutableList<Todo> = mutableListOf()

    private val maxId: TodoId
        get() = todos.map(Todo::id).max() ?: 0

    override fun create(content: String): Todo {
        val t = Todo(maxId + 1, content, false)
        todos.add(t)
        return t
    }

    override fun update(todo: Todo) {
        todos.replaceAll { t ->
            if (t.id == todo.id) todo
            else t
        }
    }

    override fun findAll(): List<Todo> {
        return todos.toList()
    }

    override fun findById(id: TodoId): Todo? {
        return todos.find { it.id == id }
    }

}

@Repository
class JdbcTodoRepository(private val jdbcTemplate: JdbcTemplate) : TodoRepository {

    private val rowMapper = RowMapper<Todo> { resultSet: ResultSet, _: Int ->
        Todo(resultSet.getLong("id"), resultSet.getString("content"), resultSet.getBoolean("done"))
    }

    override fun create(content: String): Todo {
        jdbcTemplate.update("INSERT INTO todo(content) VALUES(?)", content)
        val id: Long = jdbcTemplate.queryForObject("SELECT last_insert_id()")
        return Todo(id, content, false)
    }

    override fun update(todo: Todo) {
        jdbcTemplate.update("UPDATE todo SET content=?, done=? WHERE id=?",
                todo.content,
                todo.done,
                todo.id
        )
    }

    override fun findAll(): List<Todo> {
        return jdbcTemplate.query("SELECT id, content, done FROM todo", rowMapper)
    }

    override fun findById(id: TodoId): Todo? {
        return jdbcTemplate.query("SELECT id, content, done FROM todo WHERE id = ?", rowMapper, id).firstOrNull()
    }

}