package site.kason.fhc;


import lombok.SneakyThrows;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public abstract class AbstractRequest {

    protected OkHttpClient okHttpClient;

    public AbstractRequest(OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
    }

    @SneakyThrows
    protected Response execute(Request req) {
        okhttp3.Response okResp = okHttpClient.newCall(req).execute();
        return new Response(okResp);
    }

}
