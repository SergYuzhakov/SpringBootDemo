package com.sbapp.todo.model;


import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.Valid;
import java.io.Serializable;
import java.util.*;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Client client = (Client) o;
        return homeAddress.equals(client.homeAddress) && elAddress.equals(client.elAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), homeAddress, elAddress);
    }
}
