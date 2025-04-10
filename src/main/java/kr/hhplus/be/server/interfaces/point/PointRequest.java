package kr.hhplus.be.server.interfaces.point;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import kr.hhplus.be.server.domain.point.ChargeCommand;

public record PointRequest(
) {
        public record Charge(
                @Positive
                @NotNull
                Integer amount
        ){
                ChargeCommand toCommand(Long userId){
                        return new ChargeCommand(userId, amount);
                }
        }
}
