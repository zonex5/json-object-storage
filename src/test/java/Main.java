import xyz.toway.tools.ObjectStorageService;

public class Main {
    public static void main(String[] args) throws Exception {
        /*  var db = new GObjectStorage("d:\\store\\file.json");

        var col = db.getCollection("myCol");
        col.insert(new Data1("petrunea", 765));
        col.insert(new Data1("ionika", 3));
        col.insert(new Data1("kotea", 345));
        col.commit(); */

        var db = new ObjectStorageService("d:\\store\\file.json", false);
        /*
        db.addObject("obj1", new Data1("petrunea", 765));
        db.addObject("obj2", new Data1("tolea", 2));
        db.commit();*/

        var col = db.getCollection("col1", Data1.class);
        //col.insert(new Data1("qweqwe qwe qwwe ", 345345));
        //col.insert(new Data1("ion", 2));
        //col.commit();

        /*col.getByUid("db870e75bd604af7bb0abcf1044ea53a")
                .ifPresent(obj -> {
                    obj.setData("tolik");
                    col.commit();
                });*/

        var all = col.getAll();

        var ttt = col.getAll(e -> e.getData().equals("ion"));

        System.out.println();
    }
}