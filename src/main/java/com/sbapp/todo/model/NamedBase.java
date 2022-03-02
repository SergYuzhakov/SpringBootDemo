package com.sbapp.todo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@MappedSuperclass
@Access(value = AccessType.FIELD)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class NamedBase extends BaseToDo {

    @NotBlank(message = "Имя должно присутствовать!")
    @Size(min = 3, max = 100, message = "Имя должно содержать не менее 3-х,но не более 100 символов!")
    @Column(name = "name")
    protected String name;

}
