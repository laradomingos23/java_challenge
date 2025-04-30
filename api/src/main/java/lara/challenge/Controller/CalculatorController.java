package lara.challenge.Controller;

import java.math.BigDecimal;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import challenge.DTO.CalculatorResponse;
import lara.challenge.Service.SendCalculationsRequestbyKafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class CalculatorController {
    private static final Logger logger = LoggerFactory.getLogger(CalculatorController.class);
    @Autowired
    private SendCalculationsRequestbyKafka request;

    @GetMapping("/sum")
    public CalculatorResponse sum(@RequestParam("a") BigDecimal a, @RequestParam("b") BigDecimal b) throws Exception {
        logger.info("Received sum request: a={}, b={}", a, b);
        return handleOperation(a, b, "SUM");
    }

    @GetMapping("/sub")
    public CalculatorResponse subtract(@RequestParam("a") BigDecimal a, @RequestParam("b") BigDecimal b)
            throws Exception {
        logger.info("Received subraction request: a={}, b={}", a, b);
        return handleOperation(a, b, "SUBTRACT");
    }

    @GetMapping("/mul")
    public CalculatorResponse multiply(@RequestParam("a") BigDecimal a, @RequestParam("b") BigDecimal b)
            throws Exception {
        logger.info("Received multiplication request: a={}, b={}", a, b);
        return handleOperation(a, b, "MULTIPLY");
    }

    @GetMapping("/div")
    public CalculatorResponse divide(@RequestParam("a") BigDecimal a, @RequestParam("b") BigDecimal b)
            throws Exception {
        logger.info("Received division request: a={}, b={}", a, b);
        return handleOperation(a, b, "DIVIDE");
    }

    private CalculatorResponse handleOperation(BigDecimal a, BigDecimal b, String operation) throws Exception {
        String requestId = UUID.randomUUID().toString();
        logger.info("Random Resquest Identification Created: id={}", requestId);

        BigDecimal result = request.calculate(operation, a, b, requestId);

        if (result == null) {
            logger.error("Calculation error: " + "The number cannot be divided by zero.");
            throw new IllegalArgumentException("Calculation error: " + "The number cannot be divided by zero.");
        }
        return new CalculatorResponse(requestId, result);

    }
}
