package com.gulimall.common.utils;

import java.util.HashMap;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.gulimall.common.exception.ErrorCode;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author aqiang9  2020-07-27
 */
@Getter
@Setter
@ApiModel("公共响应结果集")
public class CommonResult<T> extends HashMap<String, Object> {
	@ApiModelProperty("响应消息")
	private String	msg;
	/**
	 * 转态码
	 * 状态 0 为 成功 其他为失败
	 */
	@ApiModelProperty("响应状态码")
	private Integer	code;
	/**
	 * 返回的数据
	 */
	@ApiModelProperty("返回的数据")
	private T		data;

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg == null ? null : msg.trim();
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public CommonResult() {
	}

	public CommonResult(T data) {
		this.data = data;
	}

	public CommonResult(String msg, Integer code, T data) {
		this.msg = msg;
		this.code = code;
		this.data = data;
	}

	public static <T> CommonResult<T> ok() {
		return new CommonResult<>("success", 0, null);
	}

	public static <T> CommonResult<T> ok(T data) {
		return new CommonResult<>("success", 0, data);
	}

	public static <T> CommonResult<T> ok(String msg) {
		return new CommonResult<>(msg, 0, null);
	}

	public static <T> CommonResult<T> ok(String msg, T data) {
		return new CommonResult<>(msg, 0, data);
	}

	public static <T> CommonResult<T> fail() {
		return new CommonResult<>("fail", 1, null);
	}

	public static <T> CommonResult<T> fail(String msg) {
		return new CommonResult<>(msg, 1, null);
	}

	public static <T> CommonResult<T> fail(int code, String msg) {
		return new CommonResult<>(msg, code, null);
	}

	public static <T> CommonResult<T> fail(ErrorCode errorCode) {
		return new CommonResult<>(errorCode.getMsg(), errorCode.getCode(), null);
	}

	public static <T> CommonResult<T> fail(ErrorCode errorCode, T data) {
		return new CommonResult<>(errorCode.getMsg(), errorCode.getCode(), data);
	}

	public CommonResult<T> data(T data) {
		this.data = data;
		return this;
	}

	public boolean isOk() {
		return this.getCode() == 0;
	}

	public <T> T getData(TypeReference<T> typeReference) {
		Object data = get("data");
		String s = JSON.toJSONString(data);
		T t = JSON.parseObject(s, typeReference);
		return t;
	}
}