package br.com.mili.milibackend.shared;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class MyResponse {
    public static final int OK = 200;
    public static final int CREATED = 201;
    public static final int NOT_OK = 204;
    public static final int ERROR_500 = 500;
    public static final int BAD_REQUEST = 400;
    public static final int FORBIDDEN = 403;
    public static final int NOT_FOUND = 404;

    private int statusCode;
    private List<String> messages;
    private Object obj;

    public MyResponse(int statusCode) {
        this.statusCode = statusCode;
    }

    public MyResponse(int statusCode, String mensagem) {
        this.statusCode = statusCode;
        addMessage(mensagem);
    }

    public MyResponse(int statusCode, String mensagem, Object obj) {
        this.statusCode = statusCode;
        this.obj = obj;
        addMessage(mensagem);
    }

    public int getStatusCode() {
        return statusCode;
    }

    public List<String> getMessages() {
        return messages;
    }
    public String getMessage() {

        if (messages == null)
            return "";

        return messages.stream().collect(Collectors.joining());
    }

    /*public void setMessages(List<String> messages) {
        this.messages = messages;
    }*/

    public MyResponse addMessage(String message) {

        if (messages == null)
            messages = new ArrayList<>();

        messages.add(message);

        return this;
    }


    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", MyResponse.class.getSimpleName() + "[", "]")
                .add("statusCode=" + statusCode)
                .add("messages=" + messages)
                .toString();
    }
}
