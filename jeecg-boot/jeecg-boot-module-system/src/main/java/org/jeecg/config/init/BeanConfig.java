/*
 * Copyright © 2022 bjfansr@cn.ibm.com Inc. All rights reserved
 * @description: org.jeecg.config.init.BeanConfig
 * @version V1.0
 */
package org.jeecg.config.init;

import com.bimface.sdk.BimfaceClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author Moses
 * @date 2022/4/20
 */
@Component
public class BeanConfig {
    @Value("${bimface.app-key}")
    private String APP_KEY;
    @Value("${bimface.app-secret}")
    private String APP_SECRET;

    //    Endpoint endPoint = new Endpoint("http://10.32.5.35:8011/", "http://10.32.5.35:8011/file/");
    // 初始化BimfaceClient
    @Bean
    public BimfaceClient bimfaceClient() {
        return new BimfaceClient(APP_KEY, APP_SECRET);
    }
}
