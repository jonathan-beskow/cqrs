package br.com.boutique.api.services.impl;

import br.com.boutique.api.configuration.RabbitMQTopicConfig;
import br.com.boutique.api.services.BrokerService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
public class BrokerServiceImpl implements BrokerService {

    private final ObjectMapper objectMapper;
    private final RabbitTemplate rabbitTemplate;
    private final RabbitMQTopicConfig rabbitMQTopicConfig;

    @Override
    public void send(String type, Object data) {
        try {
            String jsonData = objectMapper.writeValueAsString(data);

            rabbitTemplate.convertAndSend(
                    rabbitMQTopicConfig.getExchangeName(),
                    type,
                    jsonData,
                    message -> {
                        message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                        return message;
                    }
            );

        } catch (Exception ex) {
            throw new RuntimeException("Erro ao serializar mensagem", ex);
        }
    }
}