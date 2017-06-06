package cn._94zichao.server.entity;


import cn._94zichao.server.util.ByteUtil;
import cn._94zichao.server.util.Content;

/**
 * Created by SHUA on 2017/5/15.
 */
public class SocketModel  {
    //起始符
    private byte head;
    //操作码
    private byte type;
    //数据
    private byte[] data;
    //CRC校验码
    private byte[] crcData;
    //终止符
    private byte end;
    //错误码 默认正常
    private byte errorCode= Content.NORMAL;

    public byte getHead() {
        return head;
    }

    public void setHead(byte head) {
        this.head = head;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public byte getEnd() {
        return end;
    }

    public void setEnd(byte end) {
        this.end = end;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public byte[] getCrcData() {
        return crcData;
    }

    public void setCrcData(byte[] crcData) {
        this.crcData = crcData;
    }

    public byte getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(byte errorCode) {
        this.errorCode = errorCode;
    }


    /**
     * 得到一个初始化的Model
     * @return
     */
    public SocketModel getModel(){
        return new SocketModel();
    }

    public SocketModel ack(){
        this.setHead(Content.ACK);
        return this;
    }
    public SocketModel nak(byte... error){
        this.setHead(Content.NAK);
        this.setData(error);
        return this;
    }
    public SocketModel req(byte... data){
        this.setHead(Content.REQ);
        this.setData(data);
        return this;
    }
    public SocketModel ans(byte... data){
        this.setHead(Content.ANS);
        this.setData(data);
        return this;
    }

    public SocketModel inscode(byte inscode){
        this.setType(inscode);
        return this;
    }

    public SocketModel crc(){
        byte[] all = new byte[this.data.length+1];
        all[0]=this.type;
        System.arraycopy(this.data, 0, all, 1, this.data.length);
        this.setCrcData(ByteUtil.genCRC(all));
        return this;
    }
    public byte[] retAllData(){
        byte[] all = new byte[this.data.length+1];
        all[0]=this.type;
        System.arraycopy(this.data, 0, all, 1, this.data.length);
        return all;
    }


}
