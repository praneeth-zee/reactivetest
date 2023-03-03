package com.praneeth.reactivetest;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.reactive.config.EnableWebFlux;

import reactor.ipc.netty.NettyContext;

@ComponentScan(basePackages = {"com.praneeth.reactivetest.security"})
@EnableWebFlux
public class ReactivetestApplication {

	public static void main(String[] args) {
		try (AnnotationConfigApplicationContext context =
					new AnnotationConfigApplicationContext(ReactivetestApplication.class)) {
            context.getBean(NettyContext.class).onClose().block();
        }
	}

}
