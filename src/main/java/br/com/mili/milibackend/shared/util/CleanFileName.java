package br.com.mili.milibackend.shared.util;

public final class CleanFileName {

    public static String clear(String fileName) {
        // Regex para UUID + hífen (ex: "d290f1ee-6c54-4b01-90e6-d701748f0851-")
        String uuidPattern = "[0-9a-f]{8}-[0-9a-f]{4}-4[0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}-";
        return fileName.replaceAll(uuidPattern, "");
    }
}