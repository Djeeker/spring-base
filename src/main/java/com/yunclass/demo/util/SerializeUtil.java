package com.yunclass.demo.util;

import com.caucho.hessian.io.Hessian2StreamingInput;
import com.caucho.hessian.io.Hessian2StreamingOutput;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 项目名称：mini
 *
 * @Description：
 * @Date：20:45
 * @Author kongjun
 * @Version 1.0
 */
public class SerializeUtil {

    public static byte[] serialize(Object object) {

        ByteArrayOutputStream baos = null;
        try {
            // 序列化
            baos = new ByteArrayOutputStream();
            Hessian2StreamingOutput oos = new Hessian2StreamingOutput(baos);
            oos.writeObject(object);
            byte[] bytes = baos.toByteArray();
            return bytes;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }finally {
            try {
                baos.close();
            } catch (IOException e) {
                //ignore
            }
        }
    }

    public static Object unserialize(byte[] bytes) {

        ByteArrayInputStream bais = null;
        try {
            // 反序列化
            bais = new ByteArrayInputStream(bytes);
            Hessian2StreamingInput ois = new Hessian2StreamingInput(bais);
            return ois.readObject();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }finally {
            try {
                bais.close();
            } catch (IOException e) {
                //ignore
            }
        }
    }
}
