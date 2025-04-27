package challenge.DTO;

import java.io.Serializable;
import java.math.BigDecimal;

public class CalculatorRequest implements Serializable  {
    private String requestId;
    private String operation;
    private BigDecimal a;
    private BigDecimal b;

    public CalculatorRequest() {}

    public CalculatorRequest(String requestId, String operation, BigDecimal a, BigDecimal b){
        this.requestId = requestId;
        this.operation = operation;
        this.a = a;
        this.b = b;
    } 

    public String getRequestId() {
        return this.requestId;
    }
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
    public String getOperation() {
        return this.operation;
    }
    public void setOperation(String operation) {
        this.operation = operation;
    }
    public BigDecimal getA() {
        return this.a;
    }
    public void setA(BigDecimal a) {
        this.a = a;
    }
    public BigDecimal getB() {
        return this.b;
    }
    public void setB(BigDecimal b) {
        this.b = b;
    }
    
}
