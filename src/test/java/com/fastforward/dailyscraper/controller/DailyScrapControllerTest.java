package com.fastforward.dailyscraper.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class DailyScrapControllerTest {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper objectMapper;

  @DisplayName("삼전전자 최근 5일간 주식 시장 가격 조회")
  @Test
  void getStockInfoLast5Days() throws Exception {
    mockMvc.perform(
            get("http://localhost:8080/api/v1/stock/scrap/daily/last5days?stockcode=005930"))
        .andDo(print())
        .andExpect(status().isOk());
  }
}