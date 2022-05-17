# 1. Zookeeper Introduction

## 1.1 Introduction

ZooKeeper is a distributed co-ordination service to manage large set of hosts. Co-ordinating and managing a service in a distributed environment is a complicated process.

ZooKeeper allows developers to focus on core application logic without worrying about the distributed nature of the application.

## 1.2 Overview

Apache ZooKeeper is a service used by a cluster (group of nodes) to coordinate between themselves and maintain shared data with robust synchronization techniques. 

ZooKeeper is itself a **distributed application** providing services for writing a distributed application.

Common services provided:

- Naming service
- Configuration management
- Cluster management
- Leader election
- Locking and synchronization service
- Highly reliable data registry

ZooKeeper is a component of Hadoop, ( Hbase), is a file system + watcher & notification mechanism

# 2. Zookeeper Installation

> pull image from docker: `docker pull zookeeper`
>
> Run image
>
> enter image directory: `docker exec -it containerID bash`
>
> Zookeeper commands are in `/bin/zkCli.sh`
>
> Enter `./zkCli.sh`,  then installed successfully

# 3. Zookeeper Framework

To perform ZooKeeper CLI operations, first turn on your ZooKeeper server (*“bin/zkServer.sh start”*) and then, ZooKeeper client (*“bin/zkCli.sh”*). 

## 3.1 zone type

> Four types of znode (以 key/value 形式存储数据)
>
> 1. **Ephemeral znodes**: it will be deleted, when client-side stored disconnects with zookeeper
> 2. **Ephemeral Sequential znodes**: it will be deleted, when client-side stored disconnects with zookeeper, provides this node with a Order number
> 3. **Persistent znodes**:  Persistently stored in Zookeeper
> 4. **Persistent Sequential znodes**:  Persistently stored in Zookeeper, provides thisnode with a Order number

## 3.2 Zookeeper Watcher 

> Client-side can monitor znodes in Zookeeper.
>
> When znode changes, it will notify the client-side which is monitoring the current znode.

# 4. Zookeeper Commands

> 1. Search

```
# search all sub-znodes of current znode
ls znode_name/path
# path: znode path
# eg: ls /
```

```
# search all data and stats of current znode
get znode_name/path [watch]
# [watch]：对节点进行事件监听。
# eg: get /zookeeper
```

> 2. Create znodes

```
create [-s][-e] znode_name/path znode_data
# [-s] [-e]：-s and -e are potional，-s 代表顺序节点， -e 代表临时节点，注意其中 -s 和 -e 可以同时使用的，并且临时节点不能再创建子节点
# eg: create -s -e /runoob 0
# eg: get /runoob
```

> 3. Modify znodes

```
set znode_name/path new_znode_data
```

> 4. Delete znode

```
delete znode_name/path      # znode without sub-znodes
deleteall znode_name/path   # delete current znode and all sub-znodes
```



# 5. Zookeeper server-side

## 5.1 zookeeper services

![Screen Shot 2022-05-17 at 5.08.27 PM](/Users/chy/Library/Application Support/typora-user-images/Screen Shot 2022-05-17 at 5.08.27 PM.png)

## 5.2 roles in zookeeper services

> 1. Leader: Master lead node
>
> 2.  Follower: sub-node, attend new leader election
> 3. Observer: sub-node, never attend voting
> 4. Looking: is looking leader node

## 5.3  Zookeeper - Leader Election

> 1. Every  zookeeper service will be assigned a global unique **myid**, which is a number
> 2. When Zookeeper is executing writing data, each znode has own FIFO queue. To ensure the sequence is ordered when writing each data, Zookeeper also assigns a global unique **zxid** to each data, the newest data, the bigger zxid.

> Elect Leader:
>
> 1. select the biggest zxid as leader
> 2. When zxides are the same,  select the znode with the largest myid as leader

## 5.4 Zookeeper services

Create three containers

```
version: "3.1"
services:
   zk1:
      image: zookeeper
      restart: always
      container_name: zk1
      ports:
         - 2181:2181
      environment:
         ZOO_MY_ID: 1
         ZOO_SERVERS: server.1=zk1:2888:3888:2181 server.2=zk2:2888:3888:2181 server.3=zk3:2888:3888:2181
   zk2:  
      image: zookeeper
      restart: always
      container_name: zk2
      ports:
         - 2182:2181
      environment:
         ZOO_MY_ID: 2
         ZOO_SERVERS: server.1=zk1:2888:3888:2181 server.2=zk2:2888:3888:2181 server.3=zk3:2888:3888:2181
   zk3:  
      image: zookeeper
      restart: always
      container_name: zk3
      ports:
         - 2183:2181
      environment:
         ZOO_MY_ID: 3
         ZOO_SERVERS: server.1=zk1:2888:3888:2181 server.2=zk2:2888:3888:2181 server.3=zk3:2888:3888:2181
```



# 6. Zookeeper with Java

## 6.1 Java connect Zookeeper

> 1. Create Maven project
> 2. Import dependency

```
    <dependencies>
        <dependency>
            <groupId>org.apache.zookeeper</groupId>
            <artifactId>zookeeper</artifactId>
            <version>3.8.0</version>
        </dependency>

        <!-- https://mvnrepository.com/artifact/org.apache.curator/curator-recipes -->
        <dependency>
            <groupId>org.apache.curator</groupId>
            <artifactId>curator-recipes</artifactId>
            <version>4.2.0</version>
        </dependency>

        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.12</version>
            <scope>test</scope>
        </dependency>

    </dependencies>
```

> 3. Write utils connecting Zookeeper services

```
public class ZkUtil {
    public static CuratorFramework cf(){
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(3000,2);
        CuratorFramework cf = CuratorFrameworkFactory.builder()
                .connectString("0.0.0.0:55000,0.0.0.0:1,0.0.0.0:8080")
                .retryPolicy(retryPolicy)
                .build();
        cf.start();
        return  cf;
    }

}
```

> 4. Test

```
public class Demo1 {
    @Test
    public void connect() {
        CuratorFramework cf = ZkUtil.cf();
    }
}
```

## 6.2 Java operating znode

> 1. search

```
public class Demo2 {

    CuratorFramework cf = ZkUtil.cf();

    @Test
    public void selectZnode() throws Exception {
        List<String> strings = cf.getChildren().forPath("/");

        for (String string : strings) {
            System.out.println(string);
        }
    }

    @Test
    public void getData() throws Exception {
        byte[] bytes = cf.getData().forPath("/org");

        System.out.println(new String(bytes, "UTF-8"));
    }
}
```



> 2. add

```
    @Test
    public void create() throws Exception {
        cf.create().withMode(CreateMode.PERSISTENT).forPath("/org2", "UUU".getBytes());
    }
```



> 3. modify

```
    @Test
    public void update() throws Exception {
        cf.setData().forPath("/org2", "XXX".getBytes());
    }
```



> 4. delete

```
    @Test
    public void delete() throws Exception {
        cf.delete().deletingChildrenIfNeeded().forPath("org2");
    }
```



> 5. Check status

```
    @Test
    public void stat() throws Exception {
        Stat stat = cf.checkExists().forPath("/org");
        System.out.println(stat);
    }
```



## 6.3 Java operating Zookeeper Watcher

```
public class Demo3 {

    CuratorFramework cf = ZkUtil.cf();

    @Test
    public void listen() throws Exception {
        // 1. create NodeCache object, designate znode listened
        NodeCache nodeCache = new NodeCache(cf, "/org");

        nodeCache.start();

        //2. add a listener
        nodeCache.getListenable().addListener(new NodeCacheListener() {
            @Override
            public void nodeChanged() throws Exception {
                byte[] data = nodeCache.getCurrentData().getData();
                Stat stat = nodeCache.getCurrentData().getStat();
                String path = nodeCache.getCurrentData().getPath();

                System.out.println("node listened: " + path);
                System.out.println("data of current node: " + new String(data, "UTF-8"));
                System.out.println("status of current node: " + stat);
            }
        });

        System.out.println(" start listen!!!");
        // 3. System.in.read();
        System.in.read();
    }
}
```

