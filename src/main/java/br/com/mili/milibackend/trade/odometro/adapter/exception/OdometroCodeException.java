package br.com.mili.milibackend.trade.odometro.adapter.exception;

import lombok.Getter;

@Getter
public enum OdometroCodeException {

    TRADE_MCD_ODOMETRO_IMAGEM_NAO_ENCONTRADO("TRADE_MCD_ODOMETRO_IMAGEM_NAO_ENCONTRADO", "A imagem não foi encontrada"),
    TRADE_MCD_ODOMETRO_NAO_ENCONTRADO("TRADE_MCD_ODOMETRO_NAO_ENCONTRADO", "O odômetro não foi encontrado");

    private final String code;
    private final String mensagem;

    OdometroCodeException(String code, String mensagem) {
        this.code = code;
        this.mensagem = mensagem;
    }

}