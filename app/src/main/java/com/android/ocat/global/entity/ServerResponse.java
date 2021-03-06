package com.android.ocat.global.entity;

/**
 * ServerResponse
 */

public class ServerResponse<T> {
    // 0success;1failure
    private Integer status;

    // 当状态为0时，将返回的数据封装进此
    private T data;

    // 提示信息
    private String messages;

    private ServerResponse() {
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }

    @Override
    public String toString() {
        return "ServerResponse{" +
                "status=" + status +
                ", data=" + data +
                ", messages='" + messages + '\'' +
                '}';
    }
}
