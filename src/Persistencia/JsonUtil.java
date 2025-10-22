package Persistencia;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.text.SimpleDateFormat;

/** Utility helpers for minimal JSON building without external libs. */
public final class JsonUtil {
    private static final DateTimeFormatter ISO_DT = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private static final ThreadLocal<SimpleDateFormat> ISO_DATE = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ"));

    private JsonUtil() {}

    public static String esc(String s) {
        if (s == null) return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '"': sb.append("\\\""); break;
                case '\\': sb.append("\\\\"); break;
                case '\b': sb.append("\\b"); break;
                case '\f': sb.append("\\f"); break;
                case '\n': sb.append("\\n"); break;
                case '\r': sb.append("\\r"); break;
                case '\t': sb.append("\\t"); break;
                default:
                    if (c < 0x20) {
                        sb.append(String.format("\\u%04x", (int) c));
                    } else {
                        sb.append(c);
                    }
            }
        }
        return sb.toString();
    }

    public static String q(String s) { return '"' + esc(s == null ? "" : s) + '"'; }

    public static String fmt(LocalDateTime dt) {
        return dt == null ? "null" : q(ISO_DT.format(dt));
    }

    public static String fmt(Date d) {
        return d == null ? "null" : q(ISO_DATE.get().format(d));
    }
}

