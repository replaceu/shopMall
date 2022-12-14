package com.gulimall.ware.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author aqiang9  2020-08-08
 */
@Getter
@Setter
@ToString
public class PurchaseMergeVo {
    private Long purchaseId; //整单id
    private List<Long> items; //:[1,2,3,4] //合并项集合

    public Long getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(Long purchaseId) {
        this.purchaseId = purchaseId;
    }

    public List<Long> getItems() {
        return items;
    }

    public void setItems(List<Long> items) {
        this.items = items;
    }
}
