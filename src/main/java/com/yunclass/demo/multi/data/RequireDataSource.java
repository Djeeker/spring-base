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

import java.lang.annotation.*;

/**
 * @ClassName：RequireDataSource
 * @Description：
 * @Date：2015-10-27 上午11:21
 * @Author yunClass
 * @Version 1.0
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface RequireDataSource {

    DataSources name() default DataSources.MASTER;
}
