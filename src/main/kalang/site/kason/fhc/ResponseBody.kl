package site.kason.fhc;

import com.fasterxml.jackson.databind.*;
import lombok.SneakyThrows;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.InputStream;
import java.io.Reader;

public class ResponseBody implements AutoCloseable {

    okhttp3.ResponseBody body;

    public ResponseBody(okhttp3.ResponseBody body) {
        this.body = body;
    }

    public long contentLength() {
        return body.contentLength();
    }

    public InputStream byteStream() {
        return body.byteStream();
    }

    @SneakyThrows
    public byte[] bytes() {
        return body.bytes();
    }

    public Reader charStream() {
        return body.charStream();
    }

    @SneakyThrows
    public String string() {
        return body.string();
    }

    @SneakyThrows
    public JsonNode json() {
        ObjectMapper om = new ObjectMapper();
        return om.readTree(bytes());
    }

    public Document jsoup() {
        return Jsoup.parse(string());
    }

    @Override
    public void close() throws Exception {
        body.close();
    }

}
