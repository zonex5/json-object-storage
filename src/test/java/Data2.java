import xyz.toway.tools.IStorableObject;

public class Data2 implements IStorableObject {

    private String uid;
    private Integer data;

    public Integer getData() {
        return data;
    }

    public void setData(Integer data) {
        this.data = data;
    }

    public Data2(Integer data) {
        this.data = data;
    }

    public Data2() {
    }

    @Override
    public String getUid() {
        return uid;
    }

    @Override
    public void setUid(String uid) {
        this.uid = uid;
    }
}
