package com.fastforward.dailyscraper.service.impl;

import com.fastforward.dailyscraper.service.DailyScrapService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DailyScrapServiceImplTest {

  @Autowired
  private DailyScrapService dailyScrapService;

  @DisplayName("삼전전자 최근 5일간 주식 시장 가격 조회")
  @Test
  void getStockInfoLast5Days() {
    Assertions.assertDoesNotThrow(() ->
        dailyScrapService.getStockInfoLast5Days("005930"));
  }
}