## Deprecated! This documentation is not compatible with the current lib version ##

<b>Library for storing objects in a file in json format</b>

<b>Install:</b>

```XML

<repositories>
     <repository>
         <id>reposilite-repository</id>
         <name>Reposilite Repository</name>
         <url>http://vkgames.xyz:8089/releases</url>
     </repository>
</repositories>

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

<b>TODO:</b> 
* autocommit true/false
* reactive java (Project Reactor)
