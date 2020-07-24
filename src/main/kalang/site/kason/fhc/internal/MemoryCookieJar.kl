package site.kason.fhc.internal;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author lbqh
 */
public class MemoryCookieJar implements CookieJar {

    private Map<String,Cookie> cookiesMap = new HashMap<>();

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        for(Cookie c:cookies) {
            cookiesMap.put(getCookieKey(c),c);
        }
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> list = new LinkedList();
        cookiesMap.forEach( (k,c) -> {
            if (c.expiresAt() < System.currentTimeMillis()) {
                cookiesMap.remove(k);
            }
            if (c.matches(url)) {
                list.add(c);
            }
        });
        return list;
    }

    private String getCookieKey(Cookie cookie) {
        return (cookie.secure() ? "https:" : "http:") + cookie.domain() + "/" + cookie.path() + ":" + cookie.name();
    }
}
