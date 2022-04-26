package com.sbapp.todo.web.todo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sbapp.todo.ToDoUtil;
import com.sbapp.todo.model.ToDo;
import com.sbapp.todo.service.ToDoService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



/*
@WebMvcTest автоматически настраивает инфраструктуру Spring MVC и ограничивает список просматриваемых компонентов следующими:
@Controller,@ControllerAdvice, @JsonComponent, Converter, GenericConverter, Filter,WebMvcConfigurer и HandlerMethodArgumentResolver;
 */

@WebMvcTest(controllers = ToDoRestController.class)
@AutoConfigureMockMvc
@Slf4j
class ToDoRestControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    private ToDoService service;

    private ToDo testToDo;
    private ToDo testToDoWithId;
    private ToDo invalidToDo;


    @BeforeEach
    void setUp() throws JsonProcessingException {
        testToDo = ToDoUtil.getToDosTest().iterator().next();
        testToDoWithId = ToDoUtil.getToDoWithIdTest();
        invalidToDo = ToDoUtil.getInvalidToDo();


    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @DisplayName("GET ToDos")
    void getToDos() throws Exception {
        doReturn(List.of(testToDoWithId)).when(service).getAll("", null, null);
        this.mvc.perform(get("/api/todo?filter=")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));

    }

    @Test
    @DisplayName("Get ToDo by Id=1")
    void getToDoById() throws Exception {
        String testContent = objectMapper.writeValueAsString(testToDoWithId);
        doReturn(Optional.of(testToDoWithId)).when(service).getToDoById(1L);
        this.mvc.perform(get("/api/todo/{id}", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(testContent))
                .andExpect(jsonPath("id", is(1)))
                .andExpect(jsonPath("description", is("Read a book")))
                .andExpect(jsonPath("completed", is(false)));
    }

    @Test
    @DisplayName("Create ToDo")
    void createToDo() throws Exception {
        doReturn(testToDoWithId).when(service).updateToDo(any());
        this.mvc.perform(post("/api/todo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testToDo)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string(HttpHeaders.LOCATION, "http://localhost/api/todo"))
                .andExpect(jsonPath("id", is(1)))
                .andExpect(jsonPath("description", is("Read a book")))
                .andExpect(jsonPath("completed", is(false)));

    }

    @Test
    @DisplayName("Create invalid ToDo")
    void createInValidToDo() throws Exception {
        doReturn(invalidToDo).when(service).updateToDo(any());
        this.mvc.perform(post("/api/todo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidToDo)))
                .andDo(print())
                .andExpect(status().is4xxClientError());

    }

    @Test
    @DisplayName("Update ToDo")
    void updateToDo() throws Exception {
        String content = objectMapper.writeValueAsString(testToDoWithId);
        ToDo updateContent = objectMapper.readValue(content, ToDo.class);
        updateContent.setDescription("New Description");
        doReturn(updateContent).when(service).updateToDo(testToDoWithId);
        this.mvc.perform(put("/api/todo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testToDoWithId)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, "http://localhost/api/todo"))
                .andExpect(jsonPath("id", is(1)))
                .andExpect(jsonPath("description", is("New Description")))
                .andExpect(jsonPath("completed", is(false)));

    }

    @Test
    @DisplayName("Delete ToDo with id=1")
    void deleteToDo() throws Exception {
        this.mvc.perform(delete("/api/todo/{id}", 1))
                .andExpect(status().isNoContent())
                .andDo(print());
    }


}