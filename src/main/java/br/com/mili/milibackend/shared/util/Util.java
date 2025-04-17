package br.com.mili.milibackend.shared.util;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Date;
import java.util.regex.Pattern;

public class Util {

    private Util() {
    }

    public static LocalDateTime parseSimpleDateFormat(String date) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date parsed = format.parse(date);
        return parsed.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static LocalDateTime convertDtStringToLocalDateTime(String date)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return LocalDateTime.parse(date, formatter);
    }

    public static LocalDateTime convertDtStringToLocalDateTimeSegundos(String date)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return LocalDateTime.parse(date, formatter);
    }

    public static LocalDateTime convertDtStringToLocalDateTimeUtc(String date)
    {
        return convertDtStringToLocalDateTime(date, "yyyy-MM-dd'T'HH:mm:ssz");
    }

    public static LocalDateTime convertDtStringToLocalDateTime(String date, String format)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return LocalDateTime.parse(date, formatter);
    }

    public static String convertLocalDateTimeToDtString(LocalDateTime date)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return date.format(formatter);
    }

    public static String convertLocalDateTimeToDtStringSegundos(LocalDateTime date)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return date.format(formatter);
    }

    public static String convertLocalDateTimeToDtString(LocalDateTime date, String format)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return date.format(formatter);
    }

    public static LocalDate convertDtStringToLocalDate(String date, String pattern)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDate.parse(date, formatter);
    }

    public static String convertLocalDateToDtString(LocalDate date)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return date.format(formatter);
    }

    public static String getDataHojeAgora()
    {
        return convertLocalDateTimeToDtString(LocalDateTime.now());
    }

    public static String decodeBase64(String encoded) {
        byte[] decodedBytes = Base64.getDecoder().decode(encoded);
        return new String(decodedBytes);
    }

    public static String encodeBase64(String value) {
        return Base64.getEncoder().encodeToString(value.getBytes(StandardCharsets.UTF_8));
    }

    public static String decodificarPass(String passwd) {

        String sTextoCrip = "";
        String sChave1 = "8G6A79B5EQFKH01JNLMDCO3RPZWSIT4VXUY2 ";
        String sChave2 = " 0123456789ABCDEFGHIJKLMNOPQRSTUVXWYZ";
        int nTam;
        int nPos = 0;
        int nIndice;
        nTam = passwd.length();
        while (nPos < nTam) {
            nIndice = sChave1.indexOf(passwd.toUpperCase().substring(nPos,
                    nPos + 1));
            sTextoCrip = sTextoCrip + sChave2.substring(nIndice, nIndice + 1);
            nPos++;
        }

        return sTextoCrip.toUpperCase();
    }

    public static boolean isStringContainsOnlyDigits(String text) {
        return Pattern.matches("[0-9]+", text);
    }
}
