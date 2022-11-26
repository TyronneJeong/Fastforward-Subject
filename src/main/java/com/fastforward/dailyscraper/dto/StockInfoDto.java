package com.fastforward.dailyscraper.dto;

import java.math.BigInteger;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
