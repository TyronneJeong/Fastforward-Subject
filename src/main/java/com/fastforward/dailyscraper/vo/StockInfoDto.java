package com.fastforward.dailyscraper.vo;

import lombok.*;

import java.math.BigInteger;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class StockInfoDto {
    private String postDate;
    private String stockCode;
    private BigInteger high;
    private BigInteger low;
    private BigInteger open;
    private BigInteger close;
    private BigInteger volume;
    private String corpName;
}
