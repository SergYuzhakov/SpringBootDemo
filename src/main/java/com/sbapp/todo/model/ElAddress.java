package com.sbapp.todo.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.*;
import java.io.Serializable;

@Embeddable
@Data
public class ElAddress implements Serializable {

    @Email(message = "Должен быть email!")
    @NotBlank(message = "Должен быть email!")
    @Size(max = 128)
    @Column(name = "email", nullable = false, unique = true)
    protected String email;

    @NotBlank(message = "Должен быть телефон!")
    @Pattern(regexp = "^[0-9]{10}$", message = "Номер должен быть из 10 цифр")
    @Column(name = "phone", nullable = false, unique = true)
    protected String phoneNumber;
}
