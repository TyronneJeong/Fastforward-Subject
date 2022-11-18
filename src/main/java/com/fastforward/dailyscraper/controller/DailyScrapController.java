package com.fastforward.dailyscraper.controller;

import com.fastforward.dailyscraper.service.DailyScrapService;
import com.fastforward.dailyscraper.vo.StockInfoDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/stock/scrap/daily")
@Api(tags = {"Daily Scraper REST API"})
@RestController
public class DailyScrapController {
    private final DailyScrapService dailyScrapService;

    @ApiOperation(value = "최근5일간 스크랩된 주가정보 조회", response = List.class)
    @GetMapping("/last5days")
    public Response<List<StockInfoDto>> getStockInfoLast5Days(@RequestParam(name = "stockcode", required = true) String stockCode) throws Exception {
        return Response.success(dailyScrapService.getStockInfoLast5Days(stockCode));
    }
}
