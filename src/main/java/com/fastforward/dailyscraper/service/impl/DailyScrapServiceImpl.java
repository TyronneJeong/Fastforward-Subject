package com.fastforward.dailyscraper.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fastforward.dailyscraper.constant.CommConst;
import com.fastforward.dailyscraper.repository.DailyScrapDao;
import com.fastforward.dailyscraper.service.DailyScrapService;
import com.fastforward.dailyscraper.dto.DailyMarketAmountDto;
import com.fastforward.dailyscraper.dto.StockInfoDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    private final String STOCK_INFO_GATHERING_URL;

    private final DailyScrapDao dailyScrapDao;

    private final RestTemplate restTemplate;

    private final ObjectMapper objectMapper;

    private enum DataExistsType {
        EXISTS, NOT_EXISTS, DUPLICATED
    }

    /**
     * getStockInfoLast5Days
     * 주식 종목 코드를 입력 받아, 최근 5영업일의 주가정보를 스크랩 후 조회 & 저장한다.
     *
     * @param stockCode
     * @return
     */
    @Override
    @Transactional
    public List<StockInfoDto> getStockInfoLast5Days(String stockCode) {
        saveResponseDataToDB(getMarketInfoByStockCode(stockCode), stockCode);
        return this.getStockInfoListLast5Days(stockCode);
    }

    /**
     * getMarketInfo
     * 입력된 종목코드를 이용하여 최근 5일치의 주가정보를 스크랩 하기 위한 REST URL 정보를 생성한다.
     *
     * @param stockCode
     * @return String
     */
    private String getMarketInfoByStockCode(String stockCode) {
        return restTemplate.getForEntity(makeUrlByCode(stockCode), String.class).getBody();
    }

    /**
     * saveResponseDataToDB
     * 조회된 주가정보 호출 API 응답 정보를 파싱 하여 DB에 저장한다.
     *
     * @param responseBody
     * @return void
     */
    private void saveResponseDataToDB(String responseBody, String stockCode) {
        JsonNode node = null;
        try {
            node = objectMapper.readTree(responseBody);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        // FIXME 오브젝트 매퍼 호출 1개로 줄이기. 고민필요.
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
                    .postDate(convertEpochTimeToStrDate(timestamp.get(ix).asLong()))
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
     * convertEpochTimeToStrDate
     * Long 타입의 Epoch time 을 문자 타입의 Format 스트링으로 변환한다.
     *
     * @param epochTime
     * @return String
     */
    private String convertEpochTimeToStrDate(long epochTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(CommConst.DEFAULT_DATE_FORMAT).withZone(ZoneId.systemDefault());
        return formatter.format(Instant.ofEpochSecond(epochTime));
    }

    /**
     * saveDailyMarketAmountWithoutDup
     * 조회된 주가정보를 기 등록건이 존재하는 경우 갱신을, 최초 등록인 경우 등록 절차를 수행한다.
     *
     * @param dailyMarketAmountDto
     * @return void
     */
    private void saveDailyMarketAmountWithoutDup(DailyMarketAmountDto dailyMarketAmountDto) {
        if (this.checkUpdateableData(dailyMarketAmountDto) == DataExistsType.NOT_EXISTS) {
            dailyScrapDao.saveDailyMarketAmount(dailyMarketAmountDto);
        } else if (this.checkUpdateableData(dailyMarketAmountDto) == DataExistsType.EXISTS) {
            dailyScrapDao.updateDailyMarketAmount(dailyMarketAmountDto);
        }
    }

    /**
     * checkUpdateableTargetYn
     * 기 등록건 존재 여부를 확인 하여 해당 케이스를 리턴한다.
     *
     * @param dailyMarketAmountDto
     * @return DataExistsType
     */
    private DataExistsType checkUpdateableData(DailyMarketAmountDto dailyMarketAmountDto) {
        DailyMarketAmountDto originDto = dailyScrapDao.selectDailyMarketAmount(dailyMarketAmountDto);
        if (originDto != null) {
            return originDto.equals(dailyMarketAmountDto) ? DataExistsType.DUPLICATED : DataExistsType.EXISTS;
        }
        return DataExistsType.NOT_EXISTS;
    }

    /**
     * getStockInfoListLast5Days
     * DB에 저장된 최근 5영업일의 주가정보를 조회한다.
     *
     * @param stockCode
     * @return List<StockInfoDto>
     */
    private List<StockInfoDto> getStockInfoListLast5Days(String stockCode) {
        return dailyScrapDao.selectListStockInfoLast5Days(stockCode);
    }

    /**
     * makeUrlByCode
     * 입력된 종목 코드를 이용하여 종목정보 조회 URL 을 조립한다.
     *
     * @param stockCode
     * @return String
     */
    private String makeUrlByCode(String stockCode) {
        return STOCK_INFO_GATHERING_URL.replace(CommConst.URL_REPLACE_KEYWORD, stockCode);
    }
}