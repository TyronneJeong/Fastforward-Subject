package com.fastforward.dailyscraper.dto;

import java.math.BigInteger;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@Builder
public class DailyMarketAmountDto {

  private String postDate;
  private String stockCode;
  private BigInteger high;
  private BigInteger low;
  private BigInteger open;
  private BigInteger close;
  private BigInteger volume;

  @Override
  public boolean equals(Object o) {
      if (this == o) {
          return true;
      }
      if (o == null || getClass() != o.getClass()) {
          return false;
      }
    DailyMarketAmountDto that = (DailyMarketAmountDto) o;
    return postDate.equals(that.postDate) && stockCode.equals(that.stockCode) && high.equals(
        that.high) && low.equals(that.low) && open.equals(that.open) && close.equals(that.close)
        && volume.equals(that.volume);
  }

  @Override
  public int hashCode() {
    return Objects.hash(postDate, stockCode, high, low, open, close, volume);
  }
}
