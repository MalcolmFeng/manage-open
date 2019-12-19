package com.inspur.bigdata.manage.open.service.util.http;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;

import java.net.URI;

public class HttpDelete extends HttpEntityEnclosingRequestBase {

    public static final String METHOD_NAME = "DELETE";

    @Override
    public String getMethod() {
        return METHOD_NAME;
    }

    public HttpDelete(final String uri) {
        super();
        setURI(URI.create(uri));
    }

    public HttpDelete(final URI uri) {
        super();
        setURI(uri);
    }

    public HttpDelete() {
        super();
    }

}
