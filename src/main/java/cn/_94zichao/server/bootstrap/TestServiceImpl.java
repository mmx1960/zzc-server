package cn._94zichao.server.bootstrap;

import cn._94zichao.server.annotation.ZzcService;

/**
 * Created by Administrator on 2017/6/17 0017.
 */
@ZzcService("TestService")
public class TestServiceImpl implements TestService {
    @Override
    public void test(byte[] bytes) {
        System.out.println("测试");
    }
}
