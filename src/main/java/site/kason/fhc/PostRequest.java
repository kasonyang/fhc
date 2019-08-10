package site.kason.fhc;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.util.function.Consumer;

public class PostRequest extends AbstractRequest {

    private NamedValueList headers = new NamedValueList();

    private NamedValueList fields = new NamedValueList();

    public PostRequest(OkHttpClient okHttpClient) {
        super(okHttpClient);
    }

    public PostRequest addField(String name, String value) {
        fields.add(new NamedValue(name, value));
        return this;
    }

    public PostRequest removeField(String name) {
        fields.remove(name);
        return this;
    }

    public PostRequest addHeader(String name, String value) {
        headers.add(new NamedValue(name, value));
        return this;
    }

    public PostRequest removeHeader(String name) {
        headers.remove(name);
        return this;
    }

    public Response execute(String url) {
        Request.Builder rb = new Request.Builder();
        for (NamedValue nv : headers.getAllNamedValues()) {
            rb.addHeader(nv.getName(), nv.getValue());
        }
        FormBody.Builder feb = new FormBody.Builder();
        for (NamedValue nv : fields.getAllNamedValues()) {
            feb.add(nv.getName(), nv.getValue());
        }
        Request req = rb.url(url).post(feb.build()).build();
        return execute(req);
    }

    public void execute(String url, Consumer<Response> consumer) {
        try(Response resp = execute(url)){
            consumer.accept(resp);
        }
    }

}
