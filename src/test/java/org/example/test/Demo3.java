package org.example.test;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.zookeeper.data.Stat;
import org.example.ZkUtil;
import org.junit.Test;

/**
 * @author : chy
 * @date: 2022-05-17 6:09 p.m.
 */
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
