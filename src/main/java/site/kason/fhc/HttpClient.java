package site.kason.fhc;

import okhttp3.OkHttpClient;
import site.kason.fhc.internal.MemoryCookieJar;
import site.kason.fhc.internal.TrustUtil;

import java.io.IOException;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.SocketAddress;
import java.net.URI;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class HttpClient {

    private final OkHttpClient okHttpClient;

    private final NamedValueList headers = new NamedValueList();

    /**
     * create an new HttpClient instance
     * @return
     */
    public static HttpClient instance() {
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
                    proxies.add(httpsProxy);
                } else if ("http".equals(sch)) {
                    proxies.add(httpProxy);
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
        PostRequest pr = new PostRequest(okHttpClient);
        for(NamedValue nv:headers.getAllNamedValues()) {
            pr.addField(nv.getName(),nv.getValue());
        }
        return pr;
    }

    public Response post(String url, Map<String,String> data) {
        PostRequest p = post();
        for(NamedValue nv:headers.getAllNamedValues()) {
            p.addHeader(nv.getName(),nv.getValue());
        }
        data.forEach(p::addField);
        return p.execute(url);
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

}
