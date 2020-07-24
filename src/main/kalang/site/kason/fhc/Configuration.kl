package site.kason.fhc;

import java.net.InetSocketAddress;
import java.net.Proxy;


/**
 * @author lbqh
 */
public class Configuration {

    private Proxy httpProxy;

    private Proxy httpsProxy;

    private boolean trustAll = false;

    private boolean cookieEnabled = false;

    public Configuration httpProxy(String host, int port) {
        httpProxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, port));
        return this;
    }

    public Proxy httpProxy() {
        return httpProxy;
    }

    public Configuration httpsProxy(String host, int port) {
        httpsProxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, port));
        return this;
    }

    public Proxy httpsProxy() {
        return httpsProxy;
    }

    public Configuration trustAll(boolean trustAll) {
        this.trustAll = trustAll;
        return this;
    }

    public boolean trustAll() {
        return trustAll;
    }

    public Configuration cookieEnabled(boolean cookieEnabled) {
        this.cookieEnabled = cookieEnabled;
        return this;
    }

    public boolean cookieEnabled() {
        return this.cookieEnabled;
    }

    public HttpClient newClient() {
        return new HttpClient(this);
    }


}
