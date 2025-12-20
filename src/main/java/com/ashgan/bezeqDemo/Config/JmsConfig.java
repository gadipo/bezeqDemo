package com.ashgan.bezeqDemo.Config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.jms.connection.JmsTransactionManager;
import jakarta.jms.ConnectionFactory;
import org.apache.activemq.ActiveMQConnectionFactory;

@EnableJms
@Configuration
public class JmsConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(JmsConfig.class);

    @Value("${spring.activemq.broker-url}")
    private String brokerUrl;

    @Value("${jms.session-cache-size}")
    private int sessionCacheSize;

    @Value("${jms.type-id-property-name}")
    private String typeIdPropertyName;

    @Value("${jms.listener.concurrency.min}")
    private int listenerConcurrencyMin;

    @Value("${jms.listener.concurrency.max}")
    private int listenerConcurrencyMax;

    @Value("${jms.queue.usage-events}")
    public String usageEventQueue;

    @Value("${jms.queue.outage-alerts}")
    public String outageAlertsQueue;

    @Value("${jms.queue.billing-events}")
    public String billingQueue;

    @Bean
    public ConnectionFactory cachingConnectionFactory() {
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
        activeMQConnectionFactory.setBrokerURL(brokerUrl);
        activeMQConnectionFactory.setExceptionListener(throwable -> LOGGER.error("JMS Connection Exception occurred", throwable));
        
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(activeMQConnectionFactory);
        cachingConnectionFactory.setCacheConsumers(true);
        cachingConnectionFactory.setCacheProducers(true);
        cachingConnectionFactory.setSessionCacheSize(sessionCacheSize);
        
        return cachingConnectionFactory;
    }

    @Bean
    public MessageConverter jacksonJmsMessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName(typeIdPropertyName);
        return converter;
    }

    @Bean
    public JmsTemplate jmsTemplate(ConnectionFactory cachingConnectionFactory) {
        JmsTemplate jmsTemplate = new JmsTemplate(cachingConnectionFactory);
        jmsTemplate.setMessageConverter(jacksonJmsMessageConverter());
        jmsTemplate.setSessionTransacted(true);
        jmsTemplate.setDeliveryPersistent(true);
        return jmsTemplate;
    }

    @Bean
    public PlatformTransactionManager jmsTransactionManager(ConnectionFactory cachingConnectionFactory) {
        return new JmsTransactionManager(cachingConnectionFactory);
    }

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(
            ConnectionFactory cachingConnectionFactory,
            PlatformTransactionManager jmsTransactionManager) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(cachingConnectionFactory);
        factory.setMessageConverter(jacksonJmsMessageConverter());
        factory.setConcurrency(listenerConcurrencyMin + "-" + listenerConcurrencyMax);
        factory.setSessionTransacted(true);
        factory.setTransactionManager(jmsTransactionManager);
        factory.setErrorHandler(throwable -> LOGGER.error("Error processing JMS message in listener", throwable));
        return factory;
    }
}
