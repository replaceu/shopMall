package com.gulimall.common.to;

import java.math.BigDecimal;

import lombok.Data;

/**
 * @author aqiang9  2020-08-02
 */
@Data
public class SpuBoundTo {
	private Long spuId;

	private BigDecimal	buyBounds;
	private BigDecimal	growBounds;

	public Long getSpuId() {
		return spuId;
	}

	public void setSpuId(Long spuId) {
		this.spuId = spuId;
	}

	public BigDecimal getBuyBounds() {
		return buyBounds;
	}

	public void setBuyBounds(BigDecimal buyBounds) {
		this.buyBounds = buyBounds;
	}

	public BigDecimal getGrowBounds() {
		return growBounds;
	}

	public void setGrowBounds(BigDecimal growBounds) {
		this.growBounds = growBounds;
	}
}
