package com.bingo.study.common.es.config;

import com.bingo.study.common.core.utils.StringUtil;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

@ConditionalOnMissingBean(ElasticSearchConfig.class)
public class ElasticSearchConfig {

    /**
     * 集群配置格式，";"分号分割
     * http:0.0.0.0:8080;http:0.0.0.0:8080;
     */
    @Value("${es.hostUrls}")
    private String hostUrls;

    @Bean
    public RestHighLevelClient restHighLevelClient() {
        return new RestHighLevelClient(
                RestClient.builder(initHost())
                /**
                 * net.ipv4.tcp_keepalive_time = 7200
                 * net.ipv4.tcp_keepalive_intvl = 75
                 * net.ipv4.tcp_keepalive_probes = 9
                 * You should normally set net.ipv4.tcp_keepalive_time to 300. The default of 7200 seconds (i.e. 2
                 * hours) is almost certainly too long to wait to send the first keepalive.
                 */
                // .setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder.
                //         setDefaultIOReactorConfig(IOReactorConfig.custom().
                //                 setSoKeepAlive(true)
                //                 .build()
                //         )
                // )
        );
    }

    private HttpHost[] initHost() {
        List<HttpHost> hostList = new ArrayList<>();
        String[] hostUrlArr = hostUrls.split(StringUtil.SEPARATOR_SEMICOLON);
        for (String hostUrl : hostUrlArr) {
            String[] split = hostUrl.split(StringUtil.SEPARATOR_COLON);
            hostList.add(new HttpHost(split[1], Integer.parseInt(split[2]), split[0]));
        }
        return hostList.toArray(new HttpHost[0]);
    }
}
