package com.sbapp.todo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ElAddress {

    @Column(name = "email", nullable = false, unique = true)
    protected String email;

    @Column(name = "phone", nullable = false, unique = true)
    protected String phoneNumber;
}
