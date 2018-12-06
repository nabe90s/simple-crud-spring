package com.example.todo

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest
@Sql(statements = arrayOf("DELETE FROM todo"))
class JdbcTodoRepositoryTest {

    @Autowired
    private lateinit var sut: JdbcTodoRepository

    @Test
    fun 何も作成しなければfindAllは空のリストを返す() {
        val got = sut.findAll()
        assertThat(got).isEmpty()
    }

    @Test
    fun createで作成したTodoをfindByIdで取得できる() {
        val todo = sut.create("TEST")
        val got = sut.findById(todo.id)
        assertThat(got).isEqualTo(todo)
    }
}