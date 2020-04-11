package site.kason.fhc;

import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import site.kason.fhc.internal.MemoryCookieJar;
import site.kason.fhc.internal.TrustUtil;

import javax.annotation.Nullable;
import java.io.IOException;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.SocketAddress;
import java.net.URI;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class HttpClient {

    private final OkHttpClient okHttpClient;

    private final NamedValueList headers = new NamedValueList();

    /**
     * Create a new HttpClient instance.
     * @return the new client instance
     */
    public static HttpClient newClient() {
        return config().newClient();
    }

    /**
     * Create a new Configuration instance.
     * @return the new configuration instance
     */
    public static Configuration config() {
        return new Configuration();
    }

    HttpClient(Configuration conf) {
        OkHttpClient.Builder ob = new OkHttpClient.Builder();
        Proxy httpProxy = conf.httpProxy();
        Proxy httpsProxy = conf.httpsProxy();
        ob.proxySelector(new ProxySelector() {
            @Override
            public List<Proxy> select(URI uri) {
                List<Proxy> proxies = new LinkedList<>();
                String sch = uri.getScheme();
                if ("https".equals(sch)) {
                    if (httpsProxy != null) {
                        proxies.add(httpsProxy);
                    }
                } else if ("http".equals(sch)) {
                    if (httpProxy != null) {
                        proxies.add(httpProxy);
                    }
                }
                return proxies;
            }

            @Override
            public void connectFailed(URI uri, SocketAddress sa, IOException ioe) {
            }
        });
        if (conf.trustAll()) {
            ob.sslSocketFactory(TrustUtil.createTrustAllSSLSocketFactory());
        }
        if (conf.cookieEnabled()) {
            ob.cookieJar(new MemoryCookieJar());
        }
        okHttpClient = ob.build();
    }

    /**
     * Add header for all requests.
     * @param name header name
     * @param value header value
     */
    public void addHeader(String name,String value) {
        headers.add(new NamedValue(name,value));
    }

    /**
     * Add headers for all requests.
     * @param headers headers
     */
    public void addHeaders(Map<String,String> headers) {
        for (Map.Entry<String, String> h: headers.entrySet()) {
            addHeader(h.getKey(), h.getValue());
        }
    }

    /**
     * Remove header added by addHeader/addHeaders.
     * @param name header name
     */
    public void removeHeader(String name) {
        headers.remove(name);
    }

    /**
     * Make a post request
     * @param url the url to request
     * @param data the request body
     * @return the response
     */
    public Response post(String url, @Nullable Map<String, String> data) {
        return createPost(data).execute(url);
    }

    /**
     * Make a post request
     * @param url the url to request
     * @param data the request body
     * @param consumer the response consumer
     */
    public void post(String url, @Nullable Map<String, String> data, Consumer<Response> consumer) {
        createPost(data).execute(url, consumer);
    }

    /**
     * Make a post request
     * @param url the url to request
     * @param data the request body
     * @return the response
     */
    public Response post(String url,@Nullable byte[] data) {
        return createPost(data).execute(url);
    }

    /**
     * Make a post request
     * @param url the url to request
     * @param data the request body
     * @param consumer the response consumer
     */
    public void post(String url, @Nullable byte[] data, Consumer<Response> consumer) {
        createPost(data).execute(url, consumer);
    }

    /**
     * Make a post request
     * @param url the url to request
     * @return the response
     */
    public Response post(String url) {
        return post(url, (byte[])null);
    }

    /**
     * Make a post request
     * @param url the url to request
     * @param consumer the response
     */
    public void post(String url, Consumer<Response> consumer) {
        createPost((RequestBodyGenerator) null).execute(url, consumer);
    }

    private GetRequest get() {
        GetRequest gr = new GetRequest(okHttpClient);
        for(NamedValue nv:headers.getAllNamedValues()) {
            gr.addHeader(nv.getName(),nv.getValue());
        }
        return gr;
    }

    /**
     * Make a get request
     * @param url the url to request
     * @return the response
     */
    public Response get(String url) {
        return get().execute(url);
    }

    /**
     * Make a get request
     * @param url the url to request
     * @param consumer the response consumer
     */
    public void get(String url, Consumer<Response> consumer) {
        get().execute(url, consumer);
    }

    private PostRequest createPost(@Nullable Map<String, String> data) {
        UrlEncodedRequestBodyGenerator rbg = new UrlEncodedRequestBodyGenerator();
        if (data != null) {
            data.forEach(rbg::addField);
        }
        return createPost(rbg);
    }

    private PostRequest createPost(@Nullable byte[] data) {
        RequestBodyGenerator rbg = () -> RequestBody.create(null, data);
        return createPost(rbg);
    }

    private PostRequest createPost(RequestBodyGenerator requestBodyGenerator) {
        PostRequest pr = new PostRequest(okHttpClient, requestBodyGenerator);
        for (NamedValue nv : headers.getAllNamedValues()) {
            pr.addHeader(nv.getName(), nv.getValue());
        }
        return pr;
    }

}
