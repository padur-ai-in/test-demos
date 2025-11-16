package com.example.demoproject.model;



public class FieldMapping {
    private String source;
    private String target;
    private Object transform;
    private Condition condition;
    private String defaultValue;
    private CollectionMapping collection;

    public boolean isScalar() {
        return source != null && target != null;
    }

    public boolean isCollection() {
        return collection != null;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public void setTransform(Object transform) {
        this.transform = transform;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public void setCollection(CollectionMapping collection) {
        this.collection = collection;
    }

    // Getters
    public String getSource() { return source; }
    public String getTarget() { return target; }
    public Object getTransform() { return transform; }
    @Override
    public String toString() {
        return "FieldMapping [source=" + source + ", target=" + target + ", transform=" + transform + ", condition="
                + condition + ", defaultValue=" + defaultValue + ", collection=" + collection + "]";
    }

    public Condition getCondition() { return condition; }
    public String getDefaultValue() { return defaultValue; }
    public CollectionMapping getCollection() { return collection; }
}