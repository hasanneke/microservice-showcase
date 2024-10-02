package com.mentoapp.searchservice.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
@Configuration
public class ElasticClientConfig extends ElasticsearchConfiguration {

    @Value("${spring.elastic.hostAndPort}")
    private String hostAndPort;
    @Value("${spring.elastic.username}")
    private String username;
    @Value("${spring.elastic.password}")
    private String password;

    @Override
    public ClientConfiguration clientConfiguration() {
        return ClientConfiguration.builder()
                .connectedTo(hostAndPort)
                .withBasicAuth(username, password)
                .build();
    }
}