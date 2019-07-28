package site.kason.fhc;

import okhttp3.OkHttpClient;

import java.util.Map;

public class HttpClient {

    private final OkHttpClient okHttpClient;

    private final NamedValueList headers = new NamedValueList();

    /**
     * create an new HttpClient instance
     * @return
     */
    public static HttpClient instance() {
        return new HttpClient();
    }

    private HttpClient() {
        okHttpClient = new OkHttpClient();
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
