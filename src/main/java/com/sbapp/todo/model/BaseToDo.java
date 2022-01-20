package com.sbapp.todo.model;

import lombok.*;
import org.springframework.data.domain.Persistable;
import org.springframework.data.util.ProxyUtils;
import org.springframework.util.Assert;

import javax.persistence.*;

@MappedSuperclass
@Access(AccessType.FIELD) // объявляем, что Hibernate работает с Entity по полям
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

public abstract class BaseToDo implements Persistable<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;

    public int id(){
        Assert.notNull(id, "Must be not null");
        return id;
    }

    @Override
    public boolean isNew() {
        return id == null;
    }

    @Override
    public int hashCode() {
        return id == null ? 0 : id;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj){
            return true;
        }// Hibernate проксирует классы и перед сравнением их нужно развернуть
        if(obj == null || !getClass().equals(ProxyUtils.getUserClass(obj))){
            return false;
        }
        BaseToDo toDo = (BaseToDo) obj;
        return id != null && id.equals(toDo.id) ;
    }
}
