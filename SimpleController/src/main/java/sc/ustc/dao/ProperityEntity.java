package sc.ustc.dao;

public class ProperityEntity {
    private String name;
    private String column;
    private String type;
    private String lazy;
    public ProperityEntity(){}
    public ProperityEntity(String name, String column, String type, String lazy) {
        this.name = name;
        this.column = column;
        this.type = type;
        this.lazy = lazy;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLazy() {
        return lazy;
    }

    public void setLazy(String lazy) {
        this.lazy = lazy;
    }
}
