package site.kason.fhc;

import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.util.function.Consumer;

public class GetRequest extends AbstractRequest {

    private NamedValueList headers = new NamedValueList();

    public GetRequest(OkHttpClient okHttpClient) {
        super(okHttpClient);
    }

    public GetRequest addHeader(String name, String value) {
        headers.add(new NamedValue(name, value));
        return this;
    }

    public GetRequest removeHeader(String name) {
        headers.remove(name);
        return this;
    }

    public Response execute(String url) {
        Request.Builder rb = new Request.Builder();
        for (NamedValue nv : headers.getAllNamedValues()) {
            rb.addHeader(nv.getName(), nv.getValue());
        }
        Request req = rb.url(url).get().build();
        return execute(req);
    }

    public void execute(String url, Consumer<Response> consumer) {
        try(Response rsp = execute(url)) {
            consumer.accept(rsp);
        }
    }

}
