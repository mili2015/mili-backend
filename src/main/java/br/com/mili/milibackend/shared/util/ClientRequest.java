package br.com.mili.milibackend.shared.util;


import jakarta.servlet.http.HttpServletRequest;

public final class ClientRequest {

    private HttpServletRequest request;
    public ClientRequest(HttpServletRequest request) {
        this.request = request;
    }

    public String getIp() {
        String remoteAddr = "";

        if (request != null) {
            remoteAddr = request.getHeader("X-FORWARDED-FOR");
            if (remoteAddr == null || "".equals(remoteAddr)) {
                remoteAddr = request.getRemoteAddr();
            }
        }

        return remoteAddr;
    }
}
