package com.sbapp.todo;

import com.sbapp.todo.model.Address;
import com.sbapp.todo.model.Client;
import com.sbapp.todo.model.ElAddress;

public class ClientUtil {
    private static final Client client = new Client();
    private static final Client clientWithId = new Client();
    private static final ElAddress elAddress = new ElAddress();
    private static final Address address = new Address();

    public static Client getClientTest(){
        client.setName("Jon");
        client.setHomeAddress(getAddress());
        client.setElAddress(getElAddress());
        return client;
    }
    public static Client getClientWithIdTest(){
        clientWithId.setName("Jon");
        clientWithId.setHomeAddress(getAddress());
        clientWithId.setElAddress(getElAddress());
        clientWithId.setId(2L);
        return clientWithId;
    }

    public static Address getAddress(){
        address.setCity("Moscow");
        address.setHouseNumber(19);
        address.setStreet("Arbat st.");
        return address;
    }
    public static ElAddress getElAddress(){
        elAddress.setEmail("test@mail.ru");
        elAddress.setPhoneNumber("1234567891");
        return elAddress;
    }


}
