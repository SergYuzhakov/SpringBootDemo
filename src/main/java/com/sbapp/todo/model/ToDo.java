package com.sbapp.todo.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.AbstractPersistable;


import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Data /*
аннотация Lombok, которая генерирует конструктор по умолчанию (если у вас его нет) и все сеттеры, геттеры
и переопределения методов, например метода toString, чтобы сделать класс чище
*/
@Entity // аннотируем класс модели если используем Data JPA, по полям класса Hibernate создает таблицу
@NoArgsConstructor
@Table(name = "todo")
public class ToDo extends BaseToDo {

    @NotNull
    @NotBlank
    private String description;

    @Column(insertable = true, updatable = false)
    private LocalDateTime created;

    private LocalDateTime modified;

    private boolean completed;

    @PrePersist
    public void onCreate(){
        this.setCreated(LocalDateTime.now());
        this.setModified(LocalDateTime.now());
    }

    @PreUpdate
    public void onUpdate(){
        this.setModified(LocalDateTime.now());
    }

    /*
    public ToDo() {
        LocalDateTime date = LocalDateTime.now();
        this.id = UUID.randomUUID().toString();
        this.created = date;
        this.modified = date;
    }

    public ToDo(String description) {
        this();
        this.description = description;
    }
     */

}
