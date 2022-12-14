package com.gulimall.product.vo;

import javax.validation.constraints.*;

import org.hibernate.validator.constraints.URL;

import com.gulimall.common.valid.AddGroup;
import com.gulimall.common.valid.ListValue;
import com.gulimall.common.valid.UpdateGroup;
import com.gulimall.common.valid.UpdateStatusGroup;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 品牌
 * @author aqiang9  2020-07-29
 */
@ApiModel
@Data
public class BrandVo   {
    /**
     * 品牌id
     */
    @ApiModelProperty(name = "brandId" , value = "品牌id")
    @Null(message = "不能指定品牌id", groups = {AddGroup.class})
    @NotNull(message = "品牌id不能为空",groups = {UpdateGroup.class, UpdateStatusGroup.class})
    private Long brandId;
    /**
     * 品牌名
     */

    @ApiModelProperty(name = "name" , value = "品牌id")
    @NotEmpty(message = "品牌名不能为空",groups = {AddGroup.class})
    private String name;
    /**
     * 品牌logo地址
     */
    @URL(message = "logo必须是一个合法的地址",groups = {AddGroup.class , UpdateGroup.class})
    @NotNull(message = "logo图片缺失" ,groups = {AddGroup.class})
    private String logo;
    /**
     * 介绍
     */
    private String descript;
    /**
     * 显示状态[0-不显示；1-显示]
     */
    @NotNull(message = "请指定显示状态", groups = {UpdateStatusGroup.class})
    @ListValue(value = {0,1} ,groups = {AddGroup.class ,UpdateGroup.class , UpdateStatusGroup.class})
    private Integer showStatus;
    /**
     * 检索首字母
     */
    @NotEmpty(message = "请指定检索首字母")

    @Pattern(regexp = "^[a-zA-Z]$", message = "请指定检索首字母a-zA-Z", groups = {AddGroup.class, UpdateGroup.class})
    private String firstLetter;
    /**
     * 排序
     */
    @PositiveOrZero(message = "排序字段必须大于等于0",groups = {AddGroup.class , UpdateGroup.class})
    private Integer sort;

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo == null ? null : logo.trim();
    }

    public String getDescript() {
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript == null ? null : descript.trim();
    }

    public Integer getShowStatus() {
        return showStatus;
    }

    public void setShowStatus(Integer showStatus) {
        this.showStatus = showStatus;
    }

    public String getFirstLetter() {
        return firstLetter;
    }

    public void setFirstLetter(String firstLetter) {
        this.firstLetter = firstLetter == null ? null : firstLetter.trim();
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}

