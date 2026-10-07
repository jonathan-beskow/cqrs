package br.com.boutique.api.configuration;

import lombok.Getter;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class RabbitMQTopicConfig {

    private final String exchangeName = "beautiqueExchange";

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(exchangeName);
    }

    @Bean
    public Queue customerQueue() {
        return new Queue("customerQueue", true);
    }

    @Bean
    public Binding bindingCustomer(Queue customerQueue, TopicExchange exchange) {
        return BindingBuilder
                .bind(customerQueue)
                .to(exchange)
                .with("customer.#");
    }
}