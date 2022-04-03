package cn.qaq.valveqqrobot.utils;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;

/**
 * @program: proto
 * @description: Byte操作工具类
 * @author: QAQ
 * @create: 2020-10-07 15:47
 **/
@Slf4j
public class ByteTools {
    @SneakyThrows
    public static final Double getDouble(Object var1, String nulls){

        if(var1 ==null) return null;
        if(var1 instanceof Double) return (Double) var1;
        if(var1 instanceof String) {
            if(nulls!=null && nulls.equals(var1)) return null;
            else return Double.valueOf(String.valueOf(var1));
        }
        if(var1 instanceof Integer) return Double.valueOf((Integer)var1);
        if(var1 instanceof Long) return Double.valueOf((long)var1);
        if(var1 instanceof Float) return Double.valueOf((float)var1);
        if(var1 instanceof BigDecimal) return ((BigDecimal)var1).doubleValue();
        log.error("数据类型异常:{}",var1.getClass().getName());
        throw new IllegalArgumentException("数据类型异常");
    }
    @SneakyThrows
    public static final Double getDouble(Object var1)
    {
        return getDouble(var1,null);
    }

    public static final Float getFloat(byte[] b) {
        int accum = 0;
        accum = accum|(b[0] & 0xff) << 0;
        accum = accum|(b[1] & 0xff) << 8;
        accum = accum|(b[2] & 0xff) << 16;
        accum = accum|(b[3] & 0xff) << 24;
        return Float.intBitsToFloat(accum);
    }
    public static final byte[] hexStrToBinaryStr(String hexString) {
        if(hexString==null) return null;
        String[] tmp = hexString.split(" ");
        byte[] tmpBytes = new byte[tmp.length];
        int i = 0;
        for (String b : tmp) {
            if (b.equals("FF")) {
                tmpBytes[i++] = -1;
            } else {
                tmpBytes[i++] = Integer.valueOf(b, 16).byteValue();
            }
        }
        return tmpBytes;
    }
    public static final void arraycopy(byte[] src,int srcPos,byte[] dest,int destPos,int length)
    {
        for(int i=0;i<length;i++)
        {
            dest[destPos+i]=src[srcPos+i];
        }
    }
}
