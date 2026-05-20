package com.prakash.product_service.config;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.aop.Advice;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.RejectAndDontRequeueRecoverer;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@EnableRabbit
@Configuration
@EnableConfigurationProperties(RabbitMqProperties.class)
@Slf4j
public class RabbitMqConfig {

    @Bean
    DirectExchange productExchange(RabbitMqProperties properties) {
        return new DirectExchange(properties.productExchange());
    }

    @Bean
    DirectExchange deadLetterExchange(RabbitMqProperties properties) {
        return new DirectExchange(properties.deadLetterExchange());
    }

    @Bean
    Queue productCreatedQueue(RabbitMqProperties properties) {
        return new Queue(
                properties.productCreatedQueue(),
                true,
                false,
                false,
                Map.of(
                        "x-dead-letter-exchange", properties.deadLetterExchange(),
                        "x-dead-letter-routing-key", properties.productCreatedDeadLetterRoutingKey()
                )
        );
    }

    @Bean
    Queue productCreatedDeadLetterQueue(RabbitMqProperties properties) {
        return new Queue(properties.productCreatedDeadLetterQueue(), true);
    }

    @Bean
    Binding productCreatedBinding(Queue productCreatedQueue, DirectExchange productExchange, RabbitMqProperties properties) {
        return BindingBuilder.bind(productCreatedQueue)
                .to(productExchange)
                .with(properties.productCreatedRoutingKey());
    }

    @Bean
    Binding productCreatedDeadLetterBinding(
            Queue productCreatedDeadLetterQueue,
            DirectExchange deadLetterExchange,
            RabbitMqProperties properties
    ) {
        return BindingBuilder.bind(productCreatedDeadLetterQueue)
                .to(deadLetterExchange)
                .with(properties.productCreatedDeadLetterRoutingKey());
    }

    @Bean
    MessageConverter messageConverter() {
        return new JacksonJsonMessageConverter();
    }

    @Bean
    RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            String eventId = correlationData == null ? "unknown" : correlationData.getId();
            if (ack) {
                log.info("RabbitMQ confirmed product event {}", eventId);
                return;
            }
            log.error("RabbitMQ rejected product event {}. Cause: {}", eventId, cause);
        });
        rabbitTemplate.setReturnsCallback(returned -> log.error(
                "RabbitMQ returned unroutable message. exchange={}, routingKey={}, replyCode={}, replyText={}",
                returned.getExchange(),
                returned.getRoutingKey(),
                returned.getReplyCode(),
                returned.getReplyText()
        ));
        return rabbitTemplate;
    }

    @Bean
    SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            MessageConverter messageConverter,
            RabbitMqProperties properties
    ) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter);
        factory.setDefaultRequeueRejected(false);
        factory.setAdviceChain(retryAdvice(properties));
        return factory;
    }

    private Advice retryAdvice(RabbitMqProperties properties) {
        return RetryInterceptorBuilder.stateless()
                .maxRetries(Math.max(0, properties.listenerRetryMaxAttempts() - 1))
                .backOffOptions(
                        properties.listenerRetryInitialInterval(),
                        properties.listenerRetryMultiplier(),
                        properties.listenerRetryMaxInterval()
                )
                .recoverer(new RejectAndDontRequeueRecoverer())
                .build();
    }
}
