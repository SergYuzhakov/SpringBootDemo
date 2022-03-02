package com.sbapp.todo.model;


import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity // аннотируем класс модели если используем Data JPA, по полям класса Hibernate создает таблицу
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "todo")
@ToString(callSuper = true)
public class ToDo extends BaseToDo implements Serializable {


    @NotBlank(message = "Description must be!")
    @Size(min = 10, max = 100, message = "Описание должно содержать не менее 10-и,но не более 100 символов!")
    private String description;

    @Column(insertable = true, updatable = false)
    private LocalDateTime created;

    private LocalDateTime modified;

    private boolean completed;

    @Valid
    @ManyToOne(fetch = FetchType.LAZY) // на запрос GET ToDo без клиента ничего не получим если сделаем ленивую инициализацию
    @JoinColumn(name = "clients_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull(message = "Клиент должен присутсвовать")
    private Client client;


    @PrePersist
    public void onCreate() {
        this.setCreated(LocalDateTime.now());
        this.setModified(LocalDateTime.now());
    }

    @PreUpdate
    public void onUpdate() {
        this.setModified(LocalDateTime.now());
    }

}
