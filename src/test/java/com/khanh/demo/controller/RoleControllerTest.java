package com.khanh.demo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@Slf4j
@AutoConfigureMockMvc
@TestPropertySource("/test.properties")
public class RoleControllerTest {
    @Autowired
    private MockMvc mockMvc;
}
