package lara.challenge.Service;

import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import challenge.DTO.CalculatorResponse;

@Service
public class ReceiveCalculationResponseByKafka {
     private static final Logger logger = LoggerFactory.getLogger(ReceiveCalculationResponseByKafka.class);
    private final SendCalculationsRequestbyKafka requestService;

    public ReceiveCalculationResponseByKafka(SendCalculationsRequestbyKafka requestService) {
        this.requestService = requestService;
    }

    @KafkaListener(topics = "operations-response", groupId = "rest-group",     properties = {
        "spring.json.value.default.type=challenge.DTO.CalculatorResponse"
    })
    public void listenResponse(CalculatorResponse response) {
        logger.info("Recebido response: " + response.getRequestId() + " com resultado " + response.getResult());
        requestService.completeOperation(response.getRequestId(), response.getResult());
    }
}
