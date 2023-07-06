package xyz.toway.tools;

public class Collection implements IStorable{
    private String uid;

    @Override
    public String getUid() {
        return uid;
    }

    @Override
    public void setUid(String uid) {
        this.uid = uid;
    }
}
