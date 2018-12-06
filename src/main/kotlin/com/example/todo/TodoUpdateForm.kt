package com.example.todo

import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

class TodoUpdateForm {

    @NotBlank
    @Size(max = 20)
    var content: String? = null

    var done: Boolean = false
}