import xyz.toway.tools.GObjectStorage;

public class Main {
    public static void main(String[] args) throws Exception {
        var db = new GObjectStorage("d:\\store\\file.json");

        //var tt = db.setObject("test", new Data1("kolea", 123));
        //db.commit();

     /*   var col = db.getCollection("myCol");
        col.insert(new Data1("petrunea", 765));
        col.insert(new Data1("ionika", 3));
        col.insert(new Data1("kotea", 345));
        col.commit();

        col = db.getCollection("intCollection");
        col.insert(new Data2(123));
        col.insert(new Data2(45));
        col.insert(new Data2(5656));
        col.commit();*/

        var dd = db.getObject("test", Data1.class);
        dd.ifPresent(d -> {
            d.setData("555555555");
            d.setNumber(900009);

            db.setObject("test", d);
            db.commit();
        });



        //var col = db.getCollection("intCollection");
        //var aa = (Data2) col.getFirst(Data2.class).get();


        //db.removeObject("test");
        //db.commit();

        System.out.println();
    }
}