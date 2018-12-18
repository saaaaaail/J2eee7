package sc.ustc.ioc.config;

public class Field {
    private String name;
    private String beanRef;

    public  Field(){}
    public Field(String name, String beanRef) {
        this.name = name;
        this.beanRef = beanRef;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBeanRef() {
        return beanRef;
    }

    public void setBeanRef(String beanRef) {
        this.beanRef = beanRef;
    }
}
