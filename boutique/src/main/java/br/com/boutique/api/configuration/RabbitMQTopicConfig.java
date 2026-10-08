package br.com.boutique.api.configuration;

import lombok.Getter;
import org.modelmapper.ModelMapper;
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

    @Bean
    public Queue beautyProcedureQueue() {
        return new Queue("beautyProcedureQueue", true);
    }

    @Bean
    public Binding bindingBeautyProcedures(Queue beautyProcedureQueue, TopicExchange exchange) {
        return BindingBuilder.bind(beautyProcedureQueue).to(exchange).with("beautyProcedures.#");
    }

    @Bean
    public Queue appointmentQueue() {
        return new Queue("appointmentQueue", true);
    }

    @Bean
    public Binding bindingAppointment(Queue appointmentQueue, TopicExchange exchange) {
        return BindingBuilder.bind(appointmentQueue).to(exchange).with("appointments.#");
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

}