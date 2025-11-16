package com.example.demoproject.model;

import java.util.List;
import java.util.Map;

public class MappingConfig {
    public void setContexts(Map<String, ContextDef> contexts) {
        this.contexts = contexts;
    }
    @Override
    public String toString() {
        return "MappingConfig [contexts=" + contexts + ", mappings=" + mappings + "]";
    }
    public void setMappings(List<FieldMapping> mappings) {
        this.mappings = mappings;
    }
    private Map<String, ContextDef> contexts;
    private List<FieldMapping> mappings;

    public Map<String, ContextDef> getContexts() { return contexts; }
    public List<FieldMapping> getMappings() { return mappings; }
}