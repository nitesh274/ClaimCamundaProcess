package com.nitesh274.claimcamundaprocess.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProcessApiConfig {

	public static final String CCP_EXCHANGE_NAME = "ClainCamundaProcess";
	public static final String CCP_CLAIMPROCESS_ROUTING_KEY = "claimProcess";
	public static final String CCP_CLAIMPROCESS_QUEUE_NAME = "Ccp.claimProcess";

	@Bean
	TopicExchange getCcpExchange() {
		return new TopicExchange(CCP_EXCHANGE_NAME);
	}

	@Bean
	Queue getClaimProcessQueue() {
		return new Queue(CCP_CLAIMPROCESS_QUEUE_NAME, false);
	}

	@Bean
	Binding declareBindingClaimProcess() {
		return BindingBuilder.bind(getClaimProcessQueue()).to(getCcpExchange()).with(CCP_CLAIMPROCESS_ROUTING_KEY);
	}

}