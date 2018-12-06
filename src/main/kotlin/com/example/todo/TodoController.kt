package com.example.todo

import com.example.todo.exception.NotFoundException
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("todos")
class TodoController(
        val todoRepository: TodoRepository
) {

    @ExceptionHandler(NotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleNotFoundException(): String = "todos/not_found"

    @GetMapping("")
    fun index(model: Model): String {
        val todos = todoRepository.findAll()
        model.addAttribute("todos", todos)
        return "todos/index"
    }

    @GetMapping("new")
    fun new(form: TodoCreateForm): String {
        return "todos/new"
    }

    @PostMapping("")
    fun create(@Validated form: TodoCreateForm,
               bindingResult: BindingResult): String {
        if(bindingResult.hasErrors()) {
            return "todos/new"
        }
        val c = requireNotNull(form.content)
        todoRepository.create(c)
        return "redirect:/todos"
    }

    @GetMapping("{id}/edit")
    fun edit(@PathVariable("id") id: TodoId,
             form: TodoUpdateForm): String {
        val todo = todoRepository.findById(id) ?: throw NotFoundException()
        form.content = todo.content
        form.done = todo.done
        return "todos/edit"
    }

    @PatchMapping("{id}")
    fun update(@PathVariable("id") id:TodoId,
               @Validated form: TodoUpdateForm,
               bindingResult: BindingResult): String {
        if (bindingResult.hasErrors()) {
            return "todos/edit"
        }
        val todo = todoRepository.findById(id) ?: throw NotFoundException()
        val newTodo = todo.copy(id, requireNotNull(form.content), form.done)
        todoRepository.update(newTodo)
        return "redirect:/todos"
    }
}