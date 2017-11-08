package nl.conspect.todo;

import java.util.Arrays;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.ViewResolverRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.server.adapter.WebHttpHandlerBuilder;
import org.thymeleaf.spring5.ISpringWebFluxTemplateEngine;
import org.thymeleaf.spring5.SpringWebFluxTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.reactive.ThymeleafReactiveViewResolver;
import org.thymeleaf.templatemode.TemplateMode;

import reactor.ipc.netty.http.server.HttpServer;

public class TodoApplication {

    public static void main(String[] args) {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(TodoConfiguration.class)) {

            TodoRepository todos = context.getBean(TodoRepository.class);
            todos.save(Arrays.asList(new Todo("Prepare Presentation for Spring MeetUp"), new Todo("Get Milk"))).subscribe();

            HttpHandler handler = WebHttpHandlerBuilder.applicationContext(context).build();
            ReactorHttpHandlerAdapter adapter = new ReactorHttpHandlerAdapter(handler);
            HttpServer httpServer = HttpServer.create("localhost", 8080);
            httpServer.newHandler(adapter).block();
//            context.getBean(NettyContext.class).onClose().block();

        }
    }

    @Configuration
    @EnableWebFlux
    @ComponentScan
    public static class TodoConfiguration implements WebFluxConfigurer {

        @Bean
        public SpringResourceTemplateResolver thymeleafTemplateResolver() {

            final SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
            resolver.setPrefix("classpath:/templates/");
            resolver.setSuffix(".html");
            resolver.setTemplateMode(TemplateMode.HTML);
            return resolver;
        }

        @Bean
        public ISpringWebFluxTemplateEngine thymeleafTemplateEngine() {

            final SpringWebFluxTemplateEngine templateEngine = new SpringWebFluxTemplateEngine();
            templateEngine.setTemplateResolver(thymeleafTemplateResolver());
            return templateEngine;
        }

        @Bean
        public ThymeleafReactiveViewResolver thymeleafReactiveViewResolver() {

            final ThymeleafReactiveViewResolver viewResolver = new ThymeleafReactiveViewResolver();
            viewResolver.setTemplateEngine(thymeleafTemplateEngine());
            return viewResolver;
        }

        @Override
        public void configureViewResolvers(ViewResolverRegistry registry) {
            registry.viewResolver(thymeleafReactiveViewResolver());
        }

    }
}
