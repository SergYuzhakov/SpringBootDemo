package com.sbapp.todo.web.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sbapp.ProfilingInterceptor;
import com.sbapp.annotations.TimeLogger;
import com.sbapp.todo.ClientUtil;
import com.sbapp.todo.ToDoUtil;
import com.sbapp.todo.model.Client;
import com.sbapp.todo.repo.ClientsJpaRepository;
import com.sbapp.todo.service.ClientService;
import com.sbapp.todo.util.DtoUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {ClientRestController.class, DtoUtil.class})
@AutoConfigureMockMvc
@Slf4j
class ClientRestControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ClientService clientService;


    private Client client;
    private Client clientWithId;
    private Client updateClient;
    private String jsonClient;

    @BeforeEach
    public void setUp() throws JsonProcessingException {
        client = ClientUtil.getClientTest();
        jsonClient = objectMapper.writeValueAsString(client);
        updateClient = objectMapper.readValue(jsonClient, Client.class);
        clientWithId = objectMapper.readValue(jsonClient, Client.class);
        clientWithId.setId(2L);

    }

    @Test
    void createClient() throws Exception {
        doReturn(clientWithId).when(clientService).updateClient(any());
        log.info("Client: {}", jsonClient);
        this.mvc.perform(post("/api/clients")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(jsonClient))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON));
    }

    @Test
    void updateClient() throws Exception {
        updateClient.setId(2L);
        updateClient.setName("Jonns");
        doReturn(updateClient).when(clientService).updateClient(any());
        this.mvc.perform(put("/api/clients")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientWithId)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON));
    }


}