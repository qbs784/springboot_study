package com.example.entity;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;

/**
 * 通用响应实体类，用于封装 API 的响应数据。
 *
 * @param <T> 响应数据的类型。
 */
public record RestBean<T>(int code, T data, String message) {
    /**
     * 创建一个成功响应的实例，包含指定的数据。
     *
     * @param data 成功响应的数据。
     * @param <T>  数据的类型。
     * @return 一个成功响应的 RestBean 实例。
     */
    public static <T> RestBean<T> success(T data) {
        return new RestBean<>(200, data, "请求成功!");
    }

    /**
     * 创建一个成功响应的实例，不包含数据。
     *
     * @param <T> 数据的类型。
     * @return 一个成功响应的 RestBean 实例。
     */
    public static <T> RestBean<T> success() {
        return success(null);
    }

    /**
     * 创建一个未授权响应的实例，包含指定的消息。
     *
     * @param message 未授权响应的消息。
     * @param <T>     数据的类型。
     * @return 一个未授权响应的 RestBean 实例。
     */
    public static <T> RestBean<T> unauthorized(String message) {
        return failure(401, message);
    }

    /**
     * 创建一个失败响应的实例，包含指定的状态码和消息。
     *
     * @param code    失败响应的状态码。
     * @param message 失败响应的消息。
     * @param <T>     数据的类型。
     * @return 一个失败响应的 RestBean 实例。
     */
    public static <T> RestBean<T> failure(int code, String message) {
        return new RestBean<>(code, null, message);
    }

    /**
     * 创建一个禁止访问响应的实例，包含指定的消息。
     *
     * @param message 禁止访问响应的消息。
     * @param <T>     数据的类型。
     * @return 一个禁止访问响应的 RestBean 实例。
     */
    public static <T> RestBean<T> forbidden(String message) {
        return new RestBean<>(403, null, message);
    }

    /**
     * 将当前的 RestBean 实例转换为 JSON 字符串。
     *
     * @return 当前实例的 JSON 字符串表示。
     */
    public String asJson() {
        return JSONObject.toJSONString(this, JSONWriter.Feature.WriteNulls);
    }
}
