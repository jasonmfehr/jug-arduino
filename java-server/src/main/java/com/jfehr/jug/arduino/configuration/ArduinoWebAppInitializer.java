package com.jfehr.jug.arduino.configuration;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class ArduinoWebAppInitializer implements WebApplicationInitializer {

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		AnnotationConfigWebApplicationContext dispatcherServletContext;
		
		servletContext.addListener(new ContextLoaderListener(new AnnotationConfigWebApplicationContext()));
		
		dispatcherServletContext = new AnnotationConfigWebApplicationContext();
		dispatcherServletContext.register(DispatcherServletConfiguration.class);
		ServletRegistration.Dynamic dispatcher = servletContext.addServlet("spring-dispatcher", new DispatcherServlet(dispatcherServletContext));
		dispatcher.setLoadOnStartup(1);
		dispatcher.addMapping("/");
	}

}
