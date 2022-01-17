package com.sbapp.todo;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Data /*
аннотация Lombok, которая генерирует конструктор по умолчанию (если у вас его нет) и все сеттеры, геттеры
и переопределения методов, например метода toString, чтобы сделать класс чище
*/
public class ToDo {
    @NotNull
    private String id;

    @NotNull
    @NotBlank
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime created;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime modified;
    private boolean completed;

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

}
