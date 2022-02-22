package com.sbapp.todo.model;


import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Embeddable
@Data
public class Address implements Serializable {

    @Column(name = "city")
    @Size(max = 128)
    protected String city;

    @Size(max = 128)
    @Column(name = "street")
    protected String street;

    @Column(name = "house_number")
    protected Integer houseNumber;


}
