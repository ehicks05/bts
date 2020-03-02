package net.ehicks.bts;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.resource.PathResourceResolver;

@Configuration
public class MvcConfig implements WebMvcConfigurer
{
    GlobalDataLoader globalDataLoader;

    public MvcConfig(GlobalDataLoader globalDataLoader)
    {
        this.globalDataLoader = globalDataLoader;
    }

    public void addViewControllers(ViewControllerRegistry registry)
    {
        RedirectViewControllerRegistration r = registry.addRedirectViewController("/", "/dashboard");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry)
    {
        registry.addInterceptor(new GlobalInterceptor(globalDataLoader))
                .addPathPatterns("/dashboard/**", "/issue/**", "/admin/**", "/profile/**",
                        "/search/**", "/settings/**", "/error/**");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/resources/**")
                .addResourceLocations("/resources")
                .setCachePeriod(3600)
                .resourceChain(true)
                .addResolver(new PathResourceResolver());
    }
}