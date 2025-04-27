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

    public BigDecimal calculate(String operation, BigDecimal a, BigDecimal b, String correlationID) throws Exception {
        logger.info("Calculation Started");
        CalculatorRequest request = new CalculatorRequest(correlationID, operation, a, b);
        logger.info(String.format("Calculator request => %s", request.toString()));
        
        CompletableFuture<BigDecimal> future = new CompletableFuture<>();
        futures.put(correlationID, future);
        kafkaTemplate.send("operations-request", correlationID, request);

        try {
            BigDecimal result = future.get(20, TimeUnit.SECONDS);
            futures.remove(correlationID);
            return result;
        } catch (TimeoutException e) {
            futures.remove(correlationID);
            logger.error("Timeout waiting for response for correlation ID: {}", correlationID);
            throw new RuntimeException("Timeout waiting for calculation response");
        }
    }

    // Método chamado pelo listener para resolver o future
    public void completeOperation(String correlationId, BigDecimal result) {
        CompletableFuture<BigDecimal> future = futures.get(correlationId);
        if (future != null) {
            future.complete(result);
        }
    }
}
