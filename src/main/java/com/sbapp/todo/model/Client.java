package com.sbapp.todo.model;


import lombok.*;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "clients", uniqueConstraints = {@UniqueConstraint(columnNames = {"email", "phone"}, name = "client_unique_email_phone_idx")})
@ToString(callSuper = true)
public class Client extends NamedBase {

    @Embedded
    protected Address homeAddress;

    @Embedded
    protected ElAddress elAddress;


}
