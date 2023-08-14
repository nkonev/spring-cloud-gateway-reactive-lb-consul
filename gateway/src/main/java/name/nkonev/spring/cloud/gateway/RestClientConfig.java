package name.nkonev.spring.cloud.gateway;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.web.client.RestClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {
    @Bean
    @Scope("prototype")
    public RestClient.Builder restClientBuilder(ObjectProvider<RestClientCustomizer> customizerProvider) {
        RestClient.Builder builder = RestClient.builder()
                .requestFactory(new JdkClientHttpRequestFactory());
        customizerProvider.orderedStream().forEach((customizer) -> customizer.customize(builder));
        return builder;
    }
}
