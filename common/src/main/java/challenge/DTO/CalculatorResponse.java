package challenge.DTO;

import java.math.BigDecimal;

public class CalculatorResponse {
    private String requestId;
    private BigDecimal result;

    public CalculatorResponse(){}    
    
    public CalculatorResponse(String requestId, BigDecimal result){
        this.requestId = requestId;
        this.result = result;
    }

    public String getRequestId() {
        return this.requestId;
    }
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
    public BigDecimal getResult() {
        return this.result;
    }
    public void setResult(BigDecimal result) {
        this.result = result;
    }

}
