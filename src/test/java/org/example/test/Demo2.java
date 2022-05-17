package org.example.test;

import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.example.ZkUtil;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author : chy
 * @date: 2022-05-17 5:58 p.m.
 */
public class Demo2 {

    CuratorFramework cf = ZkUtil.cf();

    @Test
    public void stat() throws Exception {
        Stat stat = cf.checkExists().forPath("/org");
        System.out.println(stat);
    }

    @Test
    public void delete() throws Exception {
        cf.delete().deletingChildrenIfNeeded().forPath("org2");
    }

    @Test
    public void update() throws Exception {
        cf.setData().forPath("/org2", "XXX".getBytes());
    }

    @Test
    public void create() throws Exception {
        cf.create().withMode(CreateMode.PERSISTENT).forPath("/org2", "UUU".getBytes());
    }

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
