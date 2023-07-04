<b>Library for storing objects in a file in json format</b>

<b>Install:</b>

```XML

<repository>
  <id>reposilite-repository</id>
  <name>Reposilite Repository</name>
  <url>http://vkgames.xyz:8089/<repository></url>
</repository>

<dependencies>
    <dependency>
      <groupId>xyz.toway.tools</groupId>
      <artifactId>json-object-storage</artifactId>
      <version>0.1</version>
    </dependency>
</dependencies>
```

<b>Usage:</b>
```java
JsonObjectStorage storage = new JsonObjectStorage(dbFileName);

//save object
storage.setObject("obj1", new Object());

//load object
storage.getObject("obj", Object.class);
```