/*
 * Copyright © 2022 bjfansr@cn.ibm.com Inc. All rights reserved
 * @description: org.jeecg.config.init.BeanConfig
 * @version V1.0
 */
package org.jeecg.config.init;

import com.bimface.sdk.BimfaceClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Moses
 * @date 2022/4/20
 */
@Slf4j
@Configuration
public class BeanConfig {
    // 通过(bimface.com)创建应用，并拿到自己的appKey和appSecret
    private String APP_KEY = "Txyp85RELtjWg69tPniNInowYKjO0MtV";
    private String APP_SECRET = "TvDVcI0V12uOMjJEPoKVH1rNP9WPq34l";
//    Endpoint endPoint = new Endpoint("http://10.32.5.35:8011/", "http://10.32.5.35:8011/file/");

    // 初始化BimfaceClient
    @Bean
    public BimfaceClient bimfaceClient() {
        return new BimfaceClient(APP_KEY, APP_SECRET);
    }
}
