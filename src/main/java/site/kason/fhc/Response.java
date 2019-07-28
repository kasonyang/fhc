package site.kason.fhc;

import java.util.List;

public class Response {

    okhttp3.Response response;

    ResponseBody body;

    public Response(okhttp3.Response response) {
        this.response = response;
        this.body = new ResponseBody(response.body());
    }

    public int code() {
        return response.code();
    }

    public List<String> headers(String name) {
        return response.headers(name);
    }

    public String header(String name) {
        return response.header(name);
    }

    public String header(String name, String defaultValue) {
        return response.header(name, defaultValue);
    }

    public ResponseBody body() {
        return body;
    }


}
