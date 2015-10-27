/*************************
 * 版权声明 **********************************
 * 版权所有：Copyright (c) 2DFIRE Co., Ltd. ${year}
 * <p/>
 * 工程名称： spring-multi-source-demo
 * 创建者： yunClass
 * 创建日期： 15-10-27
 * 创建记录： 创建类结构。
 * <p/>
 * ************************* 变更记录 ********************************
 * 修改者：
 * 修改日期：
 * 修改记录：
 **/

package com.yunclass.demo.multi.data;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * @ClassName：DetermineDataSourceAspectAdvice
 * @Description：
 * @Date：2015-10-27 下午2:10
 * @Author yunClass
 * @Version 1.0
 */
@Component
@Aspect
public class DetermineDataSourceAspectAdvice {

    private static final Logger LOG = LoggerFactory
            .getLogger(DetermineDataSourceAspectAdvice.class);

    @Pointcut("execution(public * com.meizu.logisticscenter.sf.service..*.*(..))")
    public void serverStoreData() {}

    @Before("serverStoreData()")
    public void before(JoinPoint point) {

        LOG.debug("开始设置数据源----");
        RequireDataSource datasource = ((MethodSignature) point.getSignature())
                .getMethod().getAnnotation(RequireDataSource.class);
        try {
            Assert.isNull(datasource, "有事务操作，交给MultipleDataSourceAspectAdvice处理");
            DataSourceManager.set(DataSources.SLAVE);
            LOG.debug("结束设置数据源，数据源设为：{}", DataSourceManager.get());
        } catch (IllegalArgumentException e) {
            LOG.debug(e.getMessage());
        }
    }

    @After("serverStoreData()")
    public void after() {
        LOG.debug("恢复数据源为MASTER");
        DataSourceManager.set(DataSources.MASTER);
    }
}
