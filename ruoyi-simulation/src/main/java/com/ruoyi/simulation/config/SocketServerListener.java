package com.ruoyi.simulation.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class SocketServerListener implements ApplicationListener<ApplicationStartedEvent> {
    private static final Logger logger = LoggerFactory.getLogger(SocketServerListener.class);

    @Override
    public void onApplicationEvent(ApplicationStartedEvent e) {
        ApplicationContext applicationContext = e.getApplicationContext();
    }
}
