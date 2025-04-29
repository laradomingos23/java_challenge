package lara.challenge.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import challenge.DTO.CalculatorResponse;
import lara.challenge.Controller.CalculatorController;
import lara.challenge.Service.SendCalculationsRequestbyKafka;

public class CalculatorControllerTest {
    
    @Mock
    private SendCalculationsRequestbyKafka mockService;

    @InjectMocks
    private CalculatorController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // se necessário, injetar manualmente: ReflectionTestUtils.setField(controller, "request", mockService);
    }

    @Test
    void testSum() throws Exception {
        when(mockService.calculate(eq("SUM"), any(), any(), anyString()))
            .thenReturn(new BigDecimal("5"));

        CalculatorResponse response = controller.sum(new BigDecimal("2"), new BigDecimal("3"));

        assertNotNull(response);
        assertEquals(new BigDecimal("5"), response.getResult());
        verify(mockService).calculate(eq("SUM"), eq(new BigDecimal("2")), eq(new BigDecimal("3")), anyString());
    }

    @Test
    void testSubtraction() throws Exception {
        when(mockService.calculate(eq("SUBTRACT"), any(), any(), anyString()))
            .thenReturn(new BigDecimal("1"));

        CalculatorResponse response = controller.subtract(new BigDecimal("3"), new BigDecimal("2"));

        assertEquals(new BigDecimal("1"), response.getResult());
    }

    @Test
    void testMultiplication() throws Exception {
        when(mockService.calculate(eq("MULTIPLY"), any(), any(), anyString()))
            .thenReturn(new BigDecimal("0"));

        CalculatorResponse response = controller.multiply(new BigDecimal("3"), new BigDecimal("0"));

        assertEquals(new BigDecimal("0"), response.getResult());
    }

    @Test
    void testDivision() throws Exception {
        when(mockService.calculate(eq("DIVIDE"), any(), any(), anyString()))
            .thenReturn(new BigDecimal("3"));

        CalculatorResponse response = controller.divide(new BigDecimal("3"), new BigDecimal("1"));

        assertEquals(new BigDecimal("3"), response.getResult());
    }

    @Test
    void testDivisionByZero_shouldThrowException() throws Exception {
        when(mockService.calculate(eq("DIVIDE"), any(), any(), anyString()))
            .thenReturn(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
            controller.divide(new BigDecimal("10"), BigDecimal.ZERO)
        );

        assertTrue(exception.getMessage().contains("The number cannot be divided by zero."));
    }
}
