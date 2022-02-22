package com.sbapp.todo.model;


import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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

    @NotNull
    @NotBlank
    private String description;

    @Column(insertable = true, updatable = false)
    private LocalDateTime created;

    private LocalDateTime modified;

    private boolean completed;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "clients_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
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
