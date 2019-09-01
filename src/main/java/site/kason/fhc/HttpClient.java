package site.kason.fhc;

import okhttp3.OkHttpClient;
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
     * create an new HttpClient instance
     * @return
     */
    public static HttpClient newClient() {
        return config().newClient();
    }

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

    public void addHeader(String name,String value) {
        headers.add(new NamedValue(name,value));
    }

    public void removeHeader(String name) {
        headers.remove(name);
    }

    public PostRequest post() {
        return createPost(null);
    }

    public Response post(String url, @Nullable Map<String, String> data) {
        return createPost(data).execute(url);
    }

    public void post(String url, @Nullable Map<String, String> data, Consumer<Response> consumer) {
        createPost(data).execute(url, consumer);
    }

    public void post(String url, Consumer<Response> consumer) {
        createPost(null).execute(url, consumer);
    }

    public GetRequest get() {
        GetRequest gr = new GetRequest(okHttpClient);
        for(NamedValue nv:headers.getAllNamedValues()) {
            gr.addHeader(nv.getName(),nv.getValue());
        }
        return gr;
    }

    public Response get(String url) {
        return get().execute(url);
    }

    public void get(String url, Consumer<Response> consumer) {
        get().execute(url, consumer);
    }

    private PostRequest createPost(@Nullable Map<String, String> data) {
        PostRequest pr = new PostRequest(okHttpClient);
        for (NamedValue nv : headers.getAllNamedValues()) {
            pr.addField(nv.getName(), nv.getValue());
        }
        if (data != null) {
            data.forEach(pr::addField);
        }
        return pr;
    }

}
