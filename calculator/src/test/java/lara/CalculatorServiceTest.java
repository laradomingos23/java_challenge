package lara;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;

import challenge.DTO.CalculatorRequest;
import challenge.DTO.CalculatorResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.core.KafkaTemplate;

public class CalculatorServiceTest {
    
    KafkaTemplate<String, CalculatorResponse> kafkaTemplate;
    private CalculatorService service;

    @BeforeEach
    public void setUp() {
        kafkaTemplate = mock(KafkaTemplate.class);
        service = new CalculatorService(kafkaTemplate);
    }

    @Test
    public void testSumOperation() {
        CalculatorRequest request = new CalculatorRequest("1", "SUM", new BigDecimal("10"), new BigDecimal("5"));
        service.performOperation(request);

        CalculatorResponse expectedResponse = new CalculatorResponse("1", new BigDecimal("15"));
        verify(kafkaTemplate).send(eq("operations-response"), eq("1"), eq(expectedResponse));
    }

    @Test
    public void testDivideByZero() {
        CalculatorRequest request = new CalculatorRequest("2", "DIVIDE", new BigDecimal("10"), BigDecimal.ZERO);
        service.performOperation(request);

        // Espera que o valor enviado seja nulo
        CalculatorResponse expectedResponse = new CalculatorResponse("2", null);
        verify(kafkaTemplate).send(eq("operations-response"), eq("2"), eq(expectedResponse));
    }

    @Test
    public void testUnknownOperation() {
        CalculatorRequest request = new CalculatorRequest("3", "INVALID", new BigDecimal("1"), new BigDecimal("2"));
        service.performOperation(request);

        CalculatorResponse expectedResponse = new CalculatorResponse("3", null);
        verify(kafkaTemplate).send(eq("operations-response"), eq("3"), eq(expectedResponse));
    }

    @Test
    public void testMultiplyOperation() {
        CalculatorRequest request = new CalculatorRequest("4", "MULTIPLY", new BigDecimal("4"), new BigDecimal("3"));
        service.performOperation(request);

        CalculatorResponse expectedResponse = new CalculatorResponse("4", new BigDecimal("12"));
        verify(kafkaTemplate).send(eq("operations-response"), eq("4"), eq(expectedResponse));
    }

    @Test
    public void testDivisionOperation() {
        CalculatorRequest request = new CalculatorRequest("6", "DIVIDE", new BigDecimal("10"), new BigDecimal("5"));
        service.performOperation(request);

        CalculatorResponse expectedResponse = new CalculatorResponse("6", new BigDecimal("2.00000000000000000000"));
        verify(kafkaTemplate).send(eq("operations-response"), eq("6"), eq(expectedResponse));
    }

    @Test
    public void testSubtractOperation() {
        CalculatorRequest request = new CalculatorRequest("5", "SUBTRACT", new BigDecimal("8"), new BigDecimal("3"));
        service.performOperation(request);

        CalculatorResponse expectedResponse = new CalculatorResponse("5", new BigDecimal("5"));
        verify(kafkaTemplate).send(eq("operations-response"), eq("5"), eq(expectedResponse));
    }
}
