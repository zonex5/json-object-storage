import xyz.toway.tools.IStorable;

public class Data1 implements IStorable {

    private String uid;

    private String data;

    private Integer number;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    @Override
    public String getUid() {
        return uid;
    }

    @Override
    public void setUid(String uid) {
        this.uid = uid;
    }

    public Data1() {
    }

    public Data1(String data, Integer number) {
        this.data = data;
        this.number = number;
    }
}
