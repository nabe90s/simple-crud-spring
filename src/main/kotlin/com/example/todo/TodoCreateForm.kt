package com.example.todo

import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

class TodoCreateForm {

    @NotBlank
    @Size(max = 20)
    var content: String? = null

}