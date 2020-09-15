package com.unidt.milvus.config;

import org.bson.Document;

public class ReturnResult {
    public static Document createResult() {
        Document doc = new Document("status", 200).append("msg", "ok").append("data", new Document());
        return doc;
    }

    public static Document createResult(int code, String msg) {
        Document doc = new Document("status", code).append("msg", msg).append("data", new Document());
        return doc;
    }

    public static Document createResult(int code, String msg, Document data) {
        Document doc = new Document("status", code).append("msg", msg).append("data", data);
        return doc;
    }

    public static Document createResult(int code, String msg, Object obj) {
        Document doc = new Document("status", code).append("msg", msg).append("data", obj);
        return doc;
    }
}
