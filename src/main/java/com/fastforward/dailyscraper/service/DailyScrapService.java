package com.fastforward.dailyscraper.service;

import com.fastforward.dailyscraper.vo.StockInfoDto;

import java.util.List;

public interface DailyScrapService {
    /**
     * getStockInfoLast5Days - 최근 5영업일의 주가정보를 조회 & 저장한다.
     *
     * @param stockCode
     * @return
     * @throws Exception
     */
    public List<StockInfoDto> getStockInfoLast5Days(String stockCode) throws Exception;
}
