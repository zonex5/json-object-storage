import xyz.toway.tools.JsonObjectStorage;

public class Main {
    public static void main(String[] args) throws Exception {
        var db = new JsonObjectStorage("d:\\store\\file.json");

        Data1 d1 = new Data1();
        d1.setData("kolea");
        d1.setNumber(123);
        db.setObject("suka", d1);

        var tt = db.getObject("suka", Data1.class);

        System.out.println(tt);
    }
}