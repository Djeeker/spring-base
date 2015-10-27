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
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**     
 * @ClassName：MultipleDataSourceAspectAdvice
 * @Description：
 * @date：2015年10月8日 下午1:10:30  
 * @author "yunClass"
 * @version 1.0
 */
public class MultipleDataSourceAspectAdvice {

    private static final Logger LOG = LoggerFactory.getLogger(MultipleDataSourceAspectAdvice.class);

    public void before(JoinPoint point) {

        LOG.debug("开始设置数据源----");
        RequireDataSource datasource = ((MethodSignature) point.getSignature())
                .getMethod().getAnnotation(RequireDataSource.class);
        DataSourceManager.set(datasource.name());
        LOG.debug("结束设置数据源，数据源设为：{}",DataSourceManager.get());
    }
    
    public void after(){
        LOG.debug("恢复数据源为MASTER");
        DataSourceManager.set(DataSources.MASTER);
    }
}
