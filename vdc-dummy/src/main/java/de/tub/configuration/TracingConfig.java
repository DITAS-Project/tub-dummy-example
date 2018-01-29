package de.tub.configuration;

import de.tub.trace.TracingInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Configuration
@EnableWebMvc
public class TracingConfig extends WebMvcConfigurerAdapter  {

        @Autowired
        private TracingInterceptor interceptor;

        @Override
        public void addInterceptors(InterceptorRegistry registry) {
                System.out.println("loaded interceptor");
                registry.addInterceptor(interceptor);
        }
}
