package kr.co.lotte.config;

import kr.co.lotte.interceptor.AppInfoIntercepter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMVCConfig implements WebMvcConfigurer {

    @Autowired
    private AppInfo appInfo;
    @Autowired
    private ResourceLoader resourceLoader;

    @Value("${file.upload.path}")
    private String resourcePath;


        @Override
        public void addInterceptors(InterceptorRegistry registry) {
            registry.addInterceptor(new AppInfoIntercepter(appInfo));
        }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/admin/**").addResourceLocations("classpath:/admin/");
        registry.addResourceHandler("/static/cs/**").addResourceLocations("classpath:/cs/");
        registry.addResourceHandler("/static/my/**").addResourceLocations("classpath:/my/");
        registry.addResourceHandler("/static/review/**").addResourceLocations("classpath:/review/");
        registry.addResourceHandler("/static/member/**").addResourceLocations("classpath:/member/");
        registry.addResourceHandler("/static/product/**").addResourceLocations("classpath:/product/");
        registry.addResourceHandler("/static/policy/**").addResourceLocations("classpath:/policy/");
        registry.addResourceHandler("/uploads/**").addResourceLocations("file:" + resourcePath);


        //registry.addResourceHandler("/uploads/**").addResourceLocations(resourceLoader.getResource(resourcePath));

    }
}
