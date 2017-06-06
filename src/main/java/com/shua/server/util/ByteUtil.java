package com.shua.server.util;

import io.netty.buffer.ByteBuf;

import java.util.Arrays;


/**
 * Created by zzc on 2017/5/17.
 * 字节工具类，方便写字节和读取字节时的拆分和组装
 */
public class ByteUtil {
    /**
     * 读取一个字节，如果读到了拆分字节，则返回组装后的字节
     * @param buf
     * @return
     */
    public static byte readByte(ByteBuf buf){
        byte b = buf.readByte();
        if (b==(byte)0xf7){
            byte next = buf.readByte();
            next|=(byte)0xf0;
            return next;
        }
        return b;
    }

    /**
     * 读取bytebuf中所有字节，对头尾之外的做合并处理
     * @param buf
     * @return 合并后大小的字节数组
     */
    public static byte[] readAllBytes(ByteBuf buf){
        byte[] temp = new byte[buf.readableBytes()];
        temp[0] = buf.readByte();
        int i;
        for (i= 1;i<temp.length-1;i++){
            if (buf.readableBytes() < 1){
                break;
            }
            temp[i] = readByte(buf);
        }
        temp[i] = buf.readByte();

        return getBytes(temp,1,i-1);
    }

    /**
     * 写一个字节，如果该字节需要拆分，则拆分后在写
     * @param buf
     * @param bytes
     */
    public static void writeByte(ByteBuf buf,byte bytes) {
        if(((bytes^(byte)0xf0)>>3)==0){
            buf.writeByte((byte)0xf7);
            buf.writeByte(bytes & (byte)0x0f);
        }else {
            buf.writeByte(bytes);
        }
    }

    /**
     * CRC校验
     * @param data
     * @return
     */
    public static boolean isCRC(byte[] data,byte[] crc){
        return Arrays.equals(crc, genCRC(data));
    }


    /**
     * 生成CRC校验码
     * @param data
     * @return
     */
    public static byte[] genCRC(byte[] data){
        int reg_crc = 0xFFFF;
        int i = 0;
        int length = data.length;
        for (int j=0 ;j<length;j++){
            reg_crc ^=data[j];
            for (i=0;i<8;i++)
            {
                if ((reg_crc& 0x01)!=0)
                {
                    reg_crc = (reg_crc >> 1) ^ 0xA001;
                }
                else
                {
                    reg_crc = reg_crc >> 1;
                }
            }
        }
        System.out.println(Integer.toBinaryString(reg_crc));
        return new byte[]{(byte)(reg_crc>>>8),(byte)(reg_crc)};
    }


    /**
     * 获取指定索引间的字节数组
     * @param data
     * @param start
     * @param end
     * @return
     */
    public static byte[] getBytes(byte[] data,int start,int end){
        byte[] all = new byte[end-start-1];
        System.arraycopy(data, start + 1, all, 0, end - start - 1);
        return all;
    }

    public static void main(String[] args) {
//        System.out.println(Integer.toBinaryString( 0xFFFF));
//        System.out.println(Integer.toBinaryString(0xFFFF & 0x0000ffff));
//        System.out.println(Integer.toBinaryString(genCRC(new byte[]{0x10})[0]&0x000000ff));
//        System.out.println(Integer.toBinaryString(genCRC(new byte[]{0x10})[1]&0x000000ff));
//        System.out.println(isCRC(new byte[]{0x10},genCRC(new byte[]{0x10})));
        byte[] a = new byte[]{1,2,3,4};
        byte[] b = getBytes(a,-1,4);
        System.out.println(Arrays.equals(a, b));

    }
}
