package lara.challenge.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.common.errors.TimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import challenge.DTO.CalculatorRequest;

@Service
public class SendCalculationsRequestbyKafka {
    private static final Logger logger = LoggerFactory.getLogger(SendCalculationsRequestbyKafka.class);
    private final KafkaTemplate<String, CalculatorRequest> kafkaTemplate;
    private final Map<String, CompletableFuture<BigDecimal>> futures = new ConcurrentHashMap<>();

    public SendCalculationsRequestbyKafka(KafkaTemplate<String, CalculatorRequest> kafkaTemplate) {
        System.out.println("Kafka Bootstrap Servers: " + System.getProperty("spring.kafka.bootstrap-servers"));
        this.kafkaTemplate = kafkaTemplate;
    }

    public BigDecimal calculate(String operation, BigDecimal a, BigDecimal b, String requestID) throws Exception {

        CalculatorRequest request = new CalculatorRequest(requestID, operation, a, b);
        logger.info("Calculator Resquest Created : {}", request);

        CompletableFuture<BigDecimal> future = new CompletableFuture<>();
        futures.put(requestID, future);
        kafkaTemplate.send("operations-request", requestID, request);
        logger.info("Sent CalculatorRequest [operation={}, a={}, b={}] to Kafka topic '{}' with correlationId='{}'",
                operation, a, b, "operations-request", requestID);

        try {
            BigDecimal result = future.get(20, TimeUnit.SECONDS);
            logger.info("Received calculation result for requestId='{}': {}", requestID, result);
            futures.remove(requestID);
            return result;
        } catch (TimeoutException e) {
            futures.remove(requestID);
            logger.error("Timeout waiting for response for requestId: {}", requestID);
            throw new RuntimeException("Timeout waiting for calculation response");
        }
    }

    // Método chamado pelo listener para resolver o future
    public void completeOperation(String requestId, BigDecimal result) {
        CompletableFuture<BigDecimal> future = futures.get(requestId);
        if (future != null) {
            future.complete(result);
            logger.info("Completed future for requestId='{}' with result={}", requestId, result);
        } else {
            logger.warn("No pending future found for requestId='{}'. Ignoring response.", requestId);
        }
    }
}
