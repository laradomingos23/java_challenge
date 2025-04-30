package challenge.DTO;

import java.math.BigDecimal;
import java.util.Objects;

public class CalculatorResponse {
    private String requestId;
    private BigDecimal result;

    public CalculatorResponse() {
    }

    public CalculatorResponse(String requestId, BigDecimal result) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        CalculatorResponse that = (CalculatorResponse) o;
        return Objects.equals(requestId, that.requestId) &&
                Objects.equals(result, that.result);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestId, result);
    }
}
