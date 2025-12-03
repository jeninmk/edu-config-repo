package org.com.education.course.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;

/**
 * JMS Configuration for course-service
 */
@Configuration
@EnableJms
public class JmsConfig {

    @Value("${spring.jms.activemq.broker-url}")
    private String brokerUrl;

    @Value("${spring.jms.activemq.user}")
    private String user;

    @Value("${spring.jms.activemq.password}")
    private String password;

    /**
     * Create ActiveMQ Connection Factory
     */
    @Bean
    public ActiveMQConnectionFactory activeMQConnectionFactory() {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory();
        factory.setBrokerURL(brokerUrl);
        factory.setUserName(user);
        factory.setPassword(password);
        return factory;
    }

    /**
     * Create JmsTemplate for sending messages
     */
    @Bean
    public JmsTemplate jmsTemplate() {
        return new JmsTemplate(activeMQConnectionFactory());
    }

    /**
     * Create JmsListenerContainerFactory for receiving messages
     */
    @Bean
    public JmsListenerContainerFactory<?> jmsListenerContainerFactory() {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(activeMQConnectionFactory());
        factory.setConcurrency("3-10");
        factory.setSessionTransacted(true);
        return factory;
    }
}
