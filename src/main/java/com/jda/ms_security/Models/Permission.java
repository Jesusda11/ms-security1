package com.jda.ms_security.Models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document

public class Permission {

    @Id
    String _id;
    String url;
    String method;

    public Permission(String url, String method) {
        this.url = url;
        this.method = method;
    }

    public String get_id() {
        return _id;
    }

    public String getUrl() {
        return url;
    }

    public String getMethod() {
        return method;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
