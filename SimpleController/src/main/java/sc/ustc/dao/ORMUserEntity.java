package sc.ustc.dao;

import java.util.List;

public class ORMUserEntity {
    private String name;
    private String table;
    private List<ProperityEntity> pclist;
    public ORMUserEntity(){}

    public ORMUserEntity(String name, String table, List<ProperityEntity> pclist) {
        this.name = name;
        this.table = table;
        this.pclist = pclist;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public List<ProperityEntity> getPclist() {
        return pclist;
    }

    public void setPclist(List<ProperityEntity> pclist) {
        this.pclist = pclist;
    }
}
