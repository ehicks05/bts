package net.ehicks.bts;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@PropertySource("classpath:/git.properties")
public class GitVersionProperties
{
    @Value("${version:0}")
    public String version;

    @Value("${revision:0}")
    public String revision;

    @Value("${revisionDate:0}")
    public String revisionDate;

    @Value("${author:0}")
    public String author;

    @Value("${buildDate:0}")
    public String buildDate;

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}