package com.sbapp.todo.model;


import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.Valid;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "clients", uniqueConstraints = {@UniqueConstraint(columnNames = {"email", "phone"}, name = "client_unique_email_phone_idx")})
@ToString(callSuper = true)
public class Client extends NamedBase implements Serializable {

    @Embedded
    protected Address homeAddress;

    @Embedded
    @Valid
    // need to add @Valid annotation on your embedded object if you want to validate the constraints defined in your @Embeddable object:
    protected ElAddress elAddress;

    @ElementCollection
    @CollectionTable(name = "clients_to_do_set",
    joinColumns = @JoinColumn(name = "to_do_set_id"))
    @OneToMany(fetch = FetchType.LAZY)
    protected Set<ToDo> toDoSet = new HashSet<>();




}
