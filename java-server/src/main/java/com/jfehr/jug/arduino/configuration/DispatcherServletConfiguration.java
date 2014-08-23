package com.jfehr.jug.arduino.configuration;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages="com.jfehr.jug.arduino")
public class DispatcherServletConfiguration extends WebMvcConfigurerAdapter {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/static/**").addResourceLocations("/static-content/");
	}
	
	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
		configurer.defaultContentType(MediaType.TEXT_HTML);
		//configurer.ignoreAcceptHeader(true);
		configurer.mediaType("json", MediaType.APPLICATION_JSON);
	}

	@Bean
	public ViewResolver buildContentNegotiatingViewResolver(ContentNegotiationManager manager) {
		ContentNegotiatingViewResolver contentNegotiatingViewResolver;
		InternalResourceViewResolver internalResourceViewResolver;
		List<ViewResolver> resolvers = new ArrayList<ViewResolver>();
		List<View> defaultViews = new ArrayList<View>();
		
		internalResourceViewResolver = new InternalResourceViewResolver();
		internalResourceViewResolver.setPrefix("/jsp/");
		internalResourceViewResolver.setSuffix(".jsp");
		resolvers.add(internalResourceViewResolver);
		
		contentNegotiatingViewResolver = new ContentNegotiatingViewResolver();
		contentNegotiatingViewResolver.setViewResolvers(resolvers);
		contentNegotiatingViewResolver.setContentNegotiationManager(manager);
		defaultViews.add(new MappingJackson2JsonView());
		contentNegotiatingViewResolver.setDefaultViews(defaultViews);
		
		return contentNegotiatingViewResolver;
	}

	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("control_panel");
	}
	
}
