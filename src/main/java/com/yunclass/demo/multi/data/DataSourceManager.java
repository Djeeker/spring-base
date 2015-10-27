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


/**     
 * @ClassName：MultipleDataSource
 * @Description：
 * @date：2015年10月8日 上午11:31:00  
 * @author "yunClass"
 * @version 1.0
 */
public class DataSourceManager{
    
    private static final ThreadLocal<DataSources> dataSourceKey = new ThreadLocal<DataSources>(){

        @Override
        protected DataSources initialValue() {
            return DataSources.MASTER;
        }
    };
    
    public static DataSources get(){
        return dataSourceKey.get();
    }
     
    public static void set(DataSources dataSourceType){
        dataSourceKey.set(dataSourceType);
    }
     
    public static void reset(){
        dataSourceKey.set(DataSources.MASTER);
    }
}
