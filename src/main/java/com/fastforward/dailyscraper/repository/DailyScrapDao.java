package com.fastforward.dailyscraper.repository;

import com.fastforward.dailyscraper.dto.StockInfoDto;
import com.fastforward.dailyscraper.dto.DailyMarketAmountDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DailyScrapDao {
    /**
     * selectListStockInfoLast5Days
     * - retrive the stock information using parameter.
     *
     * @param stockCode
     * @return StockInfoDto
     */
    public List<StockInfoDto> selectListStockInfoLast5Days(String stockCode);

    /**
     * selectDailyMarketAmount
     * - Retrive Daily Market Amount by parameter.
     *
     * @param dailyMarketAmountDto
     * @return DailyMarketAmountDto
     */
    public DailyMarketAmountDto selectDailyMarketAmount(DailyMarketAmountDto dailyMarketAmountDto);

    /**
     * saveStockInfo
     * - Insert Daily Market Amount by parameter.
     *
     * @param dailyMarketAmountDto
     * @return void
     */
    public void saveDailyMarketAmount(DailyMarketAmountDto dailyMarketAmountDto);

    /**
     * updateDailyMarketAmount
     * - Update Daily Market Amount by parameter.
     *
     * @param dailyMarketAmountDto
     * @return void
     */
    public void updateDailyMarketAmount(DailyMarketAmountDto dailyMarketAmountDto);

}
