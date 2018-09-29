package org.xpande.stech.utils;

import org.apache.http.client.methods.HttpPost;

/**
 * Product: Adempiere ERP & CRM Smart Business Solution. Localization : Uruguay - Xpande
 * Xpande. Created by Gabriel Vila on 2/20/18.
 */
public class CustomHttpDelete extends HttpPost {

    public CustomHttpDelete(String uri) {
        super(uri);
    }

    @Override
    public String getMethod() {
        return "DELETE";
    }
}
