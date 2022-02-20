package com.sbapp.todo.model;


import lombok.*;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "clients", uniqueConstraints = {@UniqueConstraint(columnNames = {"name"}, name = "client_unique_name_idx")})
@ToString(callSuper = true)
public class Client extends NamedBase {

    @Embedded
    protected Address homeAddress;


}
