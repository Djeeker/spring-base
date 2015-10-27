/*************************
 * 版权声明 **********************************
 * 版权所有：Copyright (c) 2DFIRE Co., Ltd. ${year}
 * <p/>
 * 工程名称： spring-multi-source-demo
 * 创建者： kongjun
 * 创建日期： 15-10-27
 * 创建记录： 创建类结构。
 * <p/>
 * ************************* 变更记录 ********************************
 * 修改者：
 * 修改日期：
 * 修改记录：
 **/

package com.yunclass.demo.multi.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 项目名称：spring-multi-source-demo
 * @ClassName：MultiTest
 * @Description：
 * @Date：2015-10-27 下午3:30
 * @Author kongjun
 * @Version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:application-context.xml")
public class MultiTest {

    @Test
    public void test(){

    }
}
