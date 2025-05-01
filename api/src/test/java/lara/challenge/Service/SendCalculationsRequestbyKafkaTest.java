package lara.challenge.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ActiveProfiles;

import challenge.DTO.CalculatorRequest;


public class SendCalculationsRequestbyKafkaTest {

    @Mock
    private KafkaTemplate<String, CalculatorRequest> kafkaTemplate;
    
    @Mock
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
}
