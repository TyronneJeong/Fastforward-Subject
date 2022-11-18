package com.fastforward.dailyscraper.dao;

import com.fastforward.dailyscraper.vo.StockInfoDto;
import com.fastforward.dailyscraper.vo.DailyMarketAmountDto;
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
     * @throws Exception
     */
    public List<StockInfoDto> selectListStockInfoLast5Days(String stockCode) throws Exception;

    /**
     * selectDailyMarketAmount
     * - Retrive Daily Market Amount by parameter.
     *
     * @param dailyMarketAmountDto
     * @return DailyMarketAmountDto
     * @throws Exception
     */
    public DailyMarketAmountDto selectDailyMarketAmount(DailyMarketAmountDto dailyMarketAmountDto) throws Exception;

    /**
     * saveStockInfo
     * - Insert Daily Market Amount by parameter.
     *
     * @param dailyMarketAmountDto
     * @return void
     * @throws Exception
     */
    public void saveDailyMarketAmount(DailyMarketAmountDto dailyMarketAmountDto) throws Exception;

    /**
     * updateDailyMarketAmount
     * - Update Daily Market Amount by parameter.
     *
     * @param dailyMarketAmountDto
     * @return void
     * @throws Exception
     */
    public void updateDailyMarketAmount(DailyMarketAmountDto dailyMarketAmountDto) throws Exception;

}
