package site.kason.fhc;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class PostRequest extends AbstractRequest {

    private NamedValueList headers = new NamedValueList();

    private RequestBodyGenerator requestBodyGenerator;

    public PostRequest(OkHttpClient okHttpClient,@Nullable RequestBodyGenerator requestBodyGenerator) {
        super(okHttpClient);
        this.requestBodyGenerator = requestBodyGenerator;
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
        RequestBody body = null;
        if (requestBodyGenerator != null) {
            body = requestBodyGenerator.generateRequestBody();
        }
        Request req = rb.url(url).post(body).build();
        return execute(req);
    }

    public void execute(String url, Consumer<Response> consumer) {
        try(Response resp = execute(url)){
            consumer.accept(resp);
        }
    }

}
