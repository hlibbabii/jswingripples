package org.incha.core.jswingripples.eig;

public class Status {
    private Mark mark;
    private JSwingRipplesEIGNode propagationSource;
    private String annotation;

    private Status(Mark mark, JSwingRipplesEIGNode propagationSource, String annotation, String probability) {
        this.mark = mark;
        this.propagationSource = propagationSource;
        this.annotation = annotation;
    }

    public static Status create(Mark mark, JSwingRipplesEIGNode propagationSource, String annotation) {
        return new Status(mark, propagationSource, annotation, null);
    }

    public static Status create(Mark mark) {
        return new Status(mark, null, null, null);
    }

    public Mark getMark() {
        return mark;
    }

    public void setMark(Mark mark) {
        this.mark = mark;
    }

    public JSwingRipplesEIGNode getPropagationSource() {
        return propagationSource;
    }

    public void setPropagationSource(JSwingRipplesEIGNode propagationSource) {
        this.propagationSource = propagationSource;
    }

    public String getAnnotation() {
        return annotation;
    }

    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }
}
