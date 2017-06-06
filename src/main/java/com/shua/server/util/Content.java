package com.shua.server.util;

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

    //操作码
    public static Byte UP_LOGIN_U=(byte)0x10;//设备注册，DATA 为设备 ID
    public static Byte UP_CNT_U=(byte)0x11;//上报人数，用于设备主动上报场内人数。
    public static Byte DAT_HBEAT_W=(byte)0x20;//服务器下发心跳包，DATA 固定为 0x55AA
    public static Byte DAT_CNT_R=(byte)0x30;//查询人数，用于通知设备上报场内人数。
    public static Byte DAT_VER_R=(byte)0x31;//读取下控程序版本信息
    public static Byte CMD_ODOOR_C=(byte)0x40;//开门命令，用于通知设备开启门禁。
    public static Byte CMD_CDOOR_C=(byte)0x41;//关门命令，用于通知设备关闭门禁。
    public static Byte CMD_OLAMP_C=(byte)0x42;//开灯命令，用于通知设备开启照明。
    public static Byte CMD_CLAMP_C=(byte)0x43;//关灯命令，用于通知设备关闭照明。
    public static Byte CMD_OBRDCST_C=(byte)0x44;//开广播命令，用于通知设备开启广播。
    public static Byte CMD_CBRDCST_C=(byte)0x45;//关广播命令，用于通知设备关闭广播。
    public static Byte CMD_OCAMERA_C=(byte)0x46;//开摄像头命令，用于通知设备开启摄像头。
    public static Byte CMD_CCAMERA_C=(byte)0x47;//关摄像头命令，用于通知设备关闭摄像头。



}
