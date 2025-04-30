package lara;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import challenge.DTO.CalculatorRequest;
import challenge.DTO.CalculatorResponse;

@Service
public class CalculatorService {
    private final KafkaTemplate<String, CalculatorResponse> kafkaTemplate;
    private static final Logger logger = LoggerFactory.getLogger(CalculatorService.class);

    public CalculatorService(KafkaTemplate<String, CalculatorResponse> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "operations-request", groupId = "calculator-group", containerFactory = "calculatorKafkaListenerContainerFactory")
    public void performOperation(CalculatorRequest calculatorRequest) {
        try {
            BigDecimal numA = calculatorRequest.getA();
            BigDecimal numB = calculatorRequest.getB();
            BigDecimal result = BigDecimal.ZERO;

            switch (calculatorRequest.getOperation()) {
                case "SUM":
                    logger.info("Started sum operation: a={}+b={}", numA, numB);
                    result = numA.add(numB);
                    break;
                case "SUBTRACT":
                    logger.info("Started subtraction operation: a={}-b={}", numA, numB);
                    result = numA.subtract(numB);
                    break;
                case "MULTIPLY":
                    logger.info("Started multiplication operation: a={}*b={}", numA, numB);
                    result = numA.multiply(numB);
                    break;
                case "DIVIDE":
                    if (numB.compareTo(BigDecimal.ZERO) == 0) {
                        logger.error("The number {} cannot be divided by zero.", numA);
                        throw new ArithmeticException("The number cannot be divided by zero.");
                    }
                    logger.info("Started division operation: a={}/b={}", numA, numB);
                    result = numA.divide(numB, 20, RoundingMode.HALF_UP);
                    break;
                default:
                    logger.error("Unknown operation: {}", calculatorRequest.getOperation());
                    throw new IllegalArgumentException("Unknown operation: " + calculatorRequest.getOperation());
            }
            // Enviar o resultado de volta para o tópico operations-response
            CalculatorResponse response = new CalculatorResponse(calculatorRequest.getRequestId(), result);
            logger.info("The result {} for requestId {} was sent via kafka through the topic operations-response.",
                    result, calculatorRequest.getRequestId());
            kafkaTemplate.send("operations-response", calculatorRequest.getRequestId(), response);
        } catch (Exception e) {
            logger.error("Error calculating operation: {}", e.getMessage());
            // Em caso de erro, manda uma resposta especial indicando erro
            CalculatorResponse errorResponse = new CalculatorResponse(calculatorRequest.getRequestId(), null);
            logger.error("An error response was sent to topic operations-response for requestId: {}",
                    calculatorRequest.getRequestId());
            kafkaTemplate.send("operations-response", calculatorRequest.getRequestId(), errorResponse);
        }
    }
}
