package cn._94zichao.server.bootstrap;

import cn._94zichao.server.annotation.ZzcService;
import cn._94zichao.server.server.ZzcServer;
import cn._94zichao.server.util.Content;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by zzc on 2017/6/17 0017.
 */
public class ZzcServerBootstrap implements ApplicationContextAware,InitializingBean {
    private final Logger logger = LoggerFactory.getLogger(ZzcServerBootstrap.class);
    private Map<Object,Method[]> methodsMap;
    private Integer backLog = 128;
    private Boolean keepAlive = true;
    private Integer listenPort = 9999;
    private Integer bossGroupThread = 1;
    private Integer serverSelectorThreads = 1;
    private Boolean useEpoll = false;
    private boolean skipEnd = true;
    private byte    endFrame= Content.END;
    private long    readerIdleTime = 0;
    private long    writerIdleTime = 0;
    private TimeUnit idleType = TimeUnit.SECONDS;
    private int     serverWorkerThread = 5;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String,Object> map =applicationContext.getBeansWithAnnotation(ZzcService.class);
        for (Object serviceBean : map.values()) {
            try {
                //拿到类下面的所有方法
                Method[] methods = serviceBean.getClass().getDeclaredMethods();
                if (methodsMap == null){
                    methodsMap = new HashMap<>();
                }
                methodsMap.put(serviceBean, methods);

            } catch (Exception e) {
                logger.error("初始化失败",e);
            }
        }
    }
    @Override
    public void afterPropertiesSet(){
        final ZzcServer zzcServer = new ZzcServer();
        initServer(zzcServer);
        new Thread(new Runnable() {
            @Override
            public void run() {
                zzcServer.startServer();
            }
        }).start();
    }

    private void initServer(ZzcServer zzcServer){
        zzcServer.setBackLog(backLog);
        zzcServer.setBossGroupThread(bossGroupThread);
        zzcServer.setKeepAlive(keepAlive);
        zzcServer.setListenPort(listenPort);
        zzcServer.setServerSelectorThreads(serverSelectorThreads);
        zzcServer.setUseEpoll(useEpoll);
        ZzcServer.TcpChannelInitializer channel = zzcServer.buildTcpInitializer();
        initChannel(channel);
        zzcServer.setInitializer(channel);
    }
    private void initChannel(ZzcServer.TcpChannelInitializer channel){
        channel.setMethodsMap(methodsMap);
        channel.setEndFrame(endFrame);
        channel.setIdleType(idleType);
        channel.setReaderIdleTime(readerIdleTime);
        channel.setSkipEnd(skipEnd);
        channel.setWriterIdleTime(writerIdleTime);
    }

    public Integer getBackLog() {
        return backLog;
    }

    public void setBackLog(Integer backLog) {
        this.backLog = backLog;
    }

    public Boolean getKeepAlive() {
        return keepAlive;
    }

    public void setKeepAlive(Boolean keepAlive) {
        this.keepAlive = keepAlive;
    }

    public Integer getListenPort() {
        return listenPort;
    }

    public void setListenPort(Integer listenPort) {
        this.listenPort = listenPort;
    }

    public Integer getBossGroupThread() {
        return bossGroupThread;
    }

    public void setBossGroupThread(Integer bossGroupThread) {
        this.bossGroupThread = bossGroupThread;
    }

    public Integer getServerSelectorThreads() {
        return serverSelectorThreads;
    }

    public void setServerSelectorThreads(Integer serverSelectorThreads) {
        this.serverSelectorThreads = serverSelectorThreads;
    }

    public Boolean getUseEpoll() {
        return useEpoll;
    }

    public void setUseEpoll(Boolean useEpoll) {
        this.useEpoll = useEpoll;
    }

    public boolean isSkipEnd() {
        return skipEnd;
    }

    public void setSkipEnd(boolean skipEnd) {
        this.skipEnd = skipEnd;
    }

    public byte getEndFrame() {
        return endFrame;
    }

    public void setEndFrame(byte endFrame) {
        this.endFrame = endFrame;
    }

    public long getReaderIdleTime() {
        return readerIdleTime;
    }

    public void setReaderIdleTime(long readerIdleTime) {
        this.readerIdleTime = readerIdleTime;
    }

    public long getWriterIdleTime() {
        return writerIdleTime;
    }

    public void setWriterIdleTime(long writerIdleTime) {
        this.writerIdleTime = writerIdleTime;
    }

    public TimeUnit getIdleType() {
        return idleType;
    }

    public void setIdleType(TimeUnit idleType) {
        this.idleType = idleType;
    }

    public int getServerWorkerThread() {
        return serverWorkerThread;
    }

    public void setServerWorkerThread(int serverWorkerThread) {
        this.serverWorkerThread = serverWorkerThread;
    }
}
