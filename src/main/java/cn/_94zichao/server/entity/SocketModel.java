package cn._94zichao.server.entity;




/**
 * Created by SHUA on 2017/5/15.
 */
public class SocketModel  {
    private String channelId;
    private byte[] data;


    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
