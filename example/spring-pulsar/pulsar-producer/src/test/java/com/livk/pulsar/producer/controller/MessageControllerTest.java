package com.livk.pulsar.producer.controller;

import com.livk.commons.jackson.JacksonUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * <p>
 * MessageControllerTest
 * </p>
 *
 * @author livk
 */
@SpringBootTest({
        "spring.pulsar.client.service-url=pulsar://livk.com:6650",
        "spring.pulsar.consumer.topics=livk-topic",
        "spring.pulsar.consumer.subscription-name=consumer"
})
@AutoConfigureMockMvc
class MessageControllerTest {
    @Autowired
    MockMvc mockMvc;


    @Test
    void testSend() throws Exception {
        Map<String, String> map = Map.of("username", "livk", "password", "123456");
        mockMvc.perform(post("/producer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JacksonUtils.writeValueAsString(map)))
                .andExpect(status().isOk())
                .andDo(print());
    }
}

