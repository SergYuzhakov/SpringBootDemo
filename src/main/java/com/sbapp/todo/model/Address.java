package com.sbapp.todo.model;


import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
public class Address implements Serializable {

    @Column(name = "city")
    protected String city;
    @Column(name = "street")
    protected String street;
    @Column(name = "house_number")
    protected Integer houseNumber;
}
