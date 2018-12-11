package sc.ustc.ioc;

import java.util.List;

public class Bean {
    private String id;
    private String clazz;
    private List<Field> fields;

    public Bean(){}

    public Bean(String id, String clazz) {
        this.id = id;
        this.clazz = clazz;
    }

    public Bean(String id, String clazz, List<Field> fields) {
        this.id = id;
        this.clazz = clazz;
        this.fields = fields;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }
}
