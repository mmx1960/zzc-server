package cn._94zichao.server.util;

/**
 * Created by SHUA on 2017/5/16.
 */
public class Content {
    //起止码
    public static Byte REQ=(byte)0xF6;//通信请求方命令起始码。
    public static Byte ANS=(byte)0xF1;//回复数据时的起始码。
    public static Byte ACK=(byte)0xF2;//应答（不回复数据）时的起始码。
    public static Byte NAK=(byte)0xF3;//接收到不合法的命令时的起始码。
    public static Byte END=(byte)0xF4;//终止码。

    //错误码
    public static Byte NORMAL=(byte)0x00;//无异常
    public static Byte Err_Fram=(byte)0x01;//帧数据错误，校验失败
    public static Byte Err_Ins=(byte)0x02;//指令码错误，无此定义
    public static Byte Err_Data=(byte)0x03;//数据码错误，超出有效取值范围
    public static Byte Err_WrPr=(byte)0x04;//写保护，收到非法写指令
    public static Byte Err_RdPr=(byte)0x05;//读保护，收到非法读指令




}
