package lara.challenge.Service;

import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import challenge.DTO.CalculatorResponse;

@Service
public class CalculationResponseListener {
     private static final Logger logger = LoggerFactory.getLogger(CalculationResponseListener.class);
    private final SendCalculationsRequestbyKafka sendCalculationsRequestbyKafka;

    public CalculationResponseListener(SendCalculationsRequestbyKafka sendCalculationsRequestbyKafka) {
        this.sendCalculationsRequestbyKafka = sendCalculationsRequestbyKafka;
    }

    @KafkaListener(
        topics = "operations-response",
        groupId = "rest-group",
        containerFactory = "kafkaListenerContainerFactory"
    )
    public void listenResponse(CalculatorResponse response) {
        logger.info("Received response for requestId: {}", response.getRequestId());
        sendCalculationsRequestbyKafka.completeOperation(response.getRequestId(), response.getResult());
    }
}
