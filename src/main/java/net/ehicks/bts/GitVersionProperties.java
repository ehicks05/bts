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
    @Value("${git.total.commit.count:0}")
    public String version;

    @Value("${git.commit.id:0}")
    public String revision;

    @Value("${git.commit.time:0}")
    public String revisionDate;

    @Value("${git.commit.user.name:0}")
    public String author;

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}