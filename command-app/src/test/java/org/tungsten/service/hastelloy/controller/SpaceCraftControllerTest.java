package org.tungsten.service.hastelloy.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.HashMap;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SpaceCraftControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void createSpaceCraft() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/create", myAwesomeSpaceCraft()).contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isCreated());
    }

    private Object myAwesomeSpaceCraft() {
        final HashMap<String, String> spaceCraft = new HashMap<>();
        spaceCraft.put("name", "awesome1");
        return spaceCraft;
    }
}