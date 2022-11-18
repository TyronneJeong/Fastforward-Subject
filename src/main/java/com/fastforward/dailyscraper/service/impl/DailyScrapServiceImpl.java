package com.fastforward.dailyscraper.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fastforward.dailyscraper.dao.DailyScrapDao;
import com.fastforward.dailyscraper.service.DailyScrapService;
import com.fastforward.dailyscraper.vo.DailyMarketAmountDto;
import com.fastforward.dailyscraper.vo.StockInfoDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class DailyScrapServiceImpl implements DailyScrapService {
    @Value("${service.stock.info.url}")
    private String STOCK_INFO_GATHERING_URL;

    @Autowired
    DailyScrapDao dailyScrapDao;

    private final RestTemplate restTemplate = new RestTemplate();

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * getStockInfoLast5Days - 최근 5영업일의 주가정보를 조회 & 저장한다.
     *
     * @param stockCode
     * @return
     * @throws Exception
     */
    @Override
    public List<StockInfoDto> getStockInfoLast5Days(String stockCode) throws Exception {
        HttpEntity<String> response = this.getMarketInfo(stockCode);
        this.saveResponseDataToDB(response, stockCode);
        return this.getStockInfoListLast5Days(stockCode);
    }

    /**
     * getMarketInfo - 입력된 종목의 주가정보를 restAPI 를 이용하여 조회한다.
     *
     * @param corpName
     * @return
     */
    private HttpEntity<String> getMarketInfo(String corpName) throws Exception {
        return restTemplate.getForEntity(this.makeUrlByCode(corpName), String.class);
    }

    /**
     * saveResponseDataToDB - API 응답 정보를 DB에 저장한다.
     *
     * @param response
     * @param stockCode
     * @throws Exception
     */
    private void saveResponseDataToDB(HttpEntity<String> response, String stockCode) throws Exception {
        JsonNode node = objectMapper.readTree(response.getBody());
        JsonNode result = node.at("/chart/result").get(0);
        JsonNode timestamp = result.at("/timestamp");
        JsonNode quote = result.at("/indicators/quote").get(0);
        JsonNode low = quote.at("/low");
        JsonNode high = quote.at("/high");
        JsonNode open = quote.at("/open");
        JsonNode close = quote.at("/close");
        JsonNode volume = quote.at("/volume");

        for (int ix = 0; ix < timestamp.size(); ix++) {
            this.saveDailyMarketAmountWithoutDup(DailyMarketAmountDto.builder()
                    .postDate(this.convertEpochTimeToStrDate(timestamp.get(ix).asLong()))
                    .stockCode(stockCode)
                    .high(high.get(ix).bigIntegerValue())
                    .low(low.get(ix).bigIntegerValue())
                    .open(open.get(ix).bigIntegerValue())
                    .close(close.get(ix).bigIntegerValue())
                    .volume(volume.get(ix).bigIntegerValue())
                    .build());
        }
    }

    /**
     * convertEpochTimeToStrDate - Epoch time 을 문자열 로 변경한다.
     *
     * @param epochTime
     * @return
     */
    private String convertEpochTimeToStrDate(long epochTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd").withZone(ZoneId.systemDefault());
        return formatter.format(Instant.ofEpochSecond(epochTime));
    }

    /**
     * saveDailyMarketAmountWithoutDup : 중복건을 제외하고 데이터를 저장한다.
     *
     * @param dailyMarketAmountDto
     * @throws Exception
     */
    private void saveDailyMarketAmountWithoutDup(DailyMarketAmountDto dailyMarketAmountDto) throws Exception {
        if (this.checkUpdateableData(dailyMarketAmountDto) == 1) {
            dailyScrapDao.saveDailyMarketAmount(dailyMarketAmountDto);
        } else if (this.checkUpdateableData(dailyMarketAmountDto) == 0) {
            dailyScrapDao.updateDailyMarketAmount(dailyMarketAmountDto);
        }
    }

    /**
     * checkUpdateableTargetYn : 등록 가능 데이터 여부를 확인 한다.
     *
     * @param dailyMarketAmountDto
     * @return -1(DUPLICATED), 0(EXISTS), 1(NOT EXISTS)
     * @throws Exception
     */
    private int checkUpdateableData(DailyMarketAmountDto dailyMarketAmountDto) throws Exception {
        DailyMarketAmountDto originDto = dailyScrapDao.selectDailyMarketAmount(dailyMarketAmountDto);
        if(originDto != null){
            return originDto.equals(dailyMarketAmountDto) ? -1 : 0;
        }
        return 1; // not exists
    }

    /**
     * getStockInfoListLast5Days - 최근 5영업일의 주가정보를 조회한다.
     *
     * @param stockCode
     * @return
     * @throws Exception
     */
    private List<StockInfoDto> getStockInfoListLast5Days(String stockCode) throws Exception {
        return dailyScrapDao.selectListStockInfoLast5Days(stockCode);
    }

    /**
     * makeUrlByName - 종목정보 조회 URL 을 조립한다.
     *
     * @param stockCode
     * @return
     */
    private String makeUrlByCode(String stockCode) throws Exception {
        return STOCK_INFO_GATHERING_URL.replace("{stockCode}", stockCode);
    }
}