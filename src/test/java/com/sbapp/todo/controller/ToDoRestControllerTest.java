package com.sbapp.todo.controller;

import com.sbapp.todo.model.ToDo;
import com.sbapp.todo.repo.ToDoJpaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@SpringBootTest
@AutoConfigureMockMvc
@JsonTest
class ToDoRestControllerTest {
/*
    @Autowired
    private MockMvc mvc;

    @MockBean
    private ToDoJpaRepository repository;


 */

    @Autowired
    private JacksonTester<ToDo> jacksonTester;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }
/*
    @Test
    void getToDos() throws Exception {

        this.mvc
                .perform(MockMvcRequestBuilders.get("/api/todo"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));

    }


 */


    @Test
    public void toDoSerializeTest() throws Exception {
        ToDo toDo = new ToDo() ;
        toDo.setDescription("Read a book");
        toDo.setCreated(LocalDateTime.parse("2022-02-21T21:19:32"));
        toDo.setCompleted(true);
        assertThat(this.jacksonTester.write(toDo))
                .extractingJsonPathStringValue("$.description").isEqualTo("Read a book");

        assertThat(this.jacksonTester.write(toDo))
                .hasJsonPathBooleanValue("$.completed");
    }

    @Test
    void getToDoById() {

    }

    @Test
    void setCompleted() {
    }

    @Test
    void createToDo() {
    }

    @Test
    void updateToDo() {
    }

    @Test
    void deleteToDo() {
    }

    @Test
    void testDeleteToDo() {
    }
}