package org.example;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * @author : chy
 * @date: 2022-05-17 5:41 p.m.
 */
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
