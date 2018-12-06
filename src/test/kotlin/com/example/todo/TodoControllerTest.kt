package com.example.todo

import org.hamcrest.Matchers
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@RunWith(SpringRunner::class)
@WebMvcTest(TodoController::class)
class TodoControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var todoRepository: TodoRepository

    @MockBean
    private lateinit var commandLineRunner: CommandLineRunner

    @Test
    fun index_保存されているタスクを全件表示すること() {
        val todos = listOf(
                Todo(123, "hoge", false),
                Todo(234, "fuga", false)
        )
        Mockito.`when`(todoRepository.findAll())
                .thenReturn(todos)
        mockMvc.perform(MockMvcRequestBuilders.get("/todos"))
                .andExpect(view().name("todos/index"))
                .andExpect(model().attribute("todos", todos))
                .andExpect(content().string(Matchers.containsString("<span>hoge</span>")))
                .andExpect(content().string(Matchers.containsString("<span>fuga</span>")))
    }

    @Test
    fun create_ポストされた内容でタスクを新規作成すること() {
        mockMvc.perform(MockMvcRequestBuilders.post("/todos")
                .param("content", "piyo"))
                .andExpect(redirectedUrl("/todos"))

        Mockito.verify(todoRepository).create("piyo")

    }
}