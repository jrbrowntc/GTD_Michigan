package coders.mich.gtdapp.data.dao;

/**
 * Created by drew on 2/27/2018.
 */

public class Bucket {

    public Bucket(String name) {
        setName(name);
    }

    public Bucket(String name, Integer iconId) {
        setName(name);
        setIconId(iconId);
    }

    private String name;
    private Integer iconId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getIconId() {
        return iconId;
    }

    public void setIconId(Integer iconId) {
        this.iconId = iconId;
    }
}
