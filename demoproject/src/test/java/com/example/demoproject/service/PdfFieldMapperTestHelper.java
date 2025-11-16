package com.example.demoproject.service;


import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import java.util.Map;

public class PdfFieldMapperTestHelper {
    public static Object resolveValue(String sourcePath, DocumentContext rootCtx, Map<String, Object> contextCache) {
        if (sourcePath.startsWith("@")) {
            String[] parts = sourcePath.substring(1).split("\\.", 2);
            String contextName = parts[0];
            String subPath = parts.length > 1 ? parts[1] : null;

            Object contextValue = contextCache.get(contextName);
            if (contextValue == null) return null;

            if (subPath == null) {
                return contextValue;
            }

            try {
                String jsonPath = subPath.startsWith("$") ? subPath : "$." + subPath;
                return JsonPath.parse(contextValue).read(jsonPath);
            } catch (Exception e) {
                return null;
            }
        } else {
            try {
                String jsonPath = sourcePath.startsWith("$") ? sourcePath : "$." + sourcePath;
                return rootCtx.read(jsonPath);
            } catch (Exception e) {
                return null;
            }
        }
    }
}