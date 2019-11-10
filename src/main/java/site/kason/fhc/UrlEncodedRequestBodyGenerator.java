package site.kason.fhc;

import okhttp3.FormBody;
import okhttp3.RequestBody;

/**
 * @author KasonYang
 */
public class UrlEncodedRequestBodyGenerator implements RequestBodyGenerator {

    private NamedValueList fields = new NamedValueList();

    public UrlEncodedRequestBodyGenerator addField(String name, String value) {
        fields.add(new NamedValue(name, value));
        return this;
    }

    public UrlEncodedRequestBodyGenerator removeField(String name) {
        fields.remove(name);
        return this;
    }

    @Override
    public RequestBody generateRequestBody() {
        FormBody.Builder feb = new FormBody.Builder();
        for (NamedValue nv : fields.getAllNamedValues()) {
            feb.add(nv.getName(), nv.getValue());
        }
        return feb.build();
    }
}
