package com.sbapp.todo.model;

import lombok.*;
import org.h2.tools.Server;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Embeddable
@Data
public class ElAddress implements Serializable {

    @Column(name = "email", nullable = false, unique = true)
    @Email
    @NotEmpty
    @Size(max = 128)
    protected String email;

    @Column(name = "phone", nullable = false, unique = true)
    protected String phoneNumber;
}
