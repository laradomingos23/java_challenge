package lara.challenge.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.TopicPartition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import org.springframework.kafka.core.KafkaTemplate;

import challenge.DTO.CalculatorRequest;

public class SendCalculationsRequestbyKafkaTest {

    private KafkaTemplate<String, CalculatorRequest> kafkaTemplate;
    private SendCalculationsRequestbyKafka service;

    @BeforeEach
    public void setUp() {
        kafkaTemplate = mock(KafkaTemplate.class);
        service = new SendCalculationsRequestbyKafka(kafkaTemplate);
    }

    @Test
    public void testCalculate_Success() throws Exception {
        String requestId = "req-123";
        BigDecimal a = BigDecimal.TEN;
        BigDecimal b = new BigDecimal("2");
        String operation = "add";

        // simular o "listener"
        new Thread(() -> {
            try {
                Thread.sleep(100); // simula algum tempo
                service.completeOperation(requestId, new BigDecimal("12"));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();

        BigDecimal result = service.calculate(operation, a, b, requestId);

        assertEquals(new BigDecimal("12"), result);

        // Verifica se o KafkaTemplate enviou a mensagem
        ArgumentCaptor<CalculatorRequest> captor = ArgumentCaptor.forClass(CalculatorRequest.class);
        verify(kafkaTemplate).send(eq("operations-request"), eq(requestId), captor.capture());
        CalculatorRequest sent = captor.getValue();
        assertEquals(operation, sent.getOperation());
        assertEquals(a, sent.getA());
        assertEquals(b, sent.getB());
    }
/* 
    @Test
    public void testCalculate_Timeout() {
        String requestId = "req-timeout";
        BigDecimal a = BigDecimal.ONE;
        BigDecimal b = BigDecimal.ONE;

        Exception exception = assertThrows(TimeoutException.class, () -> {
            service.calculate("sum", a, b, requestId);
        });

        assertEquals("Timeout waiting for calculation response", exception.getMessage());
        }*/
}
