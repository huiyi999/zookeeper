package org.example.test;

import org.apache.curator.framework.CuratorFramework;
import org.example.ZkUtil;
import org.junit.Test;

/**
 * @author : chy
 * @date: 2022-05-17 5:50 p.m.
 */
public class Demo1 {

    @Test
    public void connect() {
        CuratorFramework cf = ZkUtil.cf();
    }
}
