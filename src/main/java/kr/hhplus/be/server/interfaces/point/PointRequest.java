package kr.hhplus.be.server.interfaces.point;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import kr.hhplus.be.server.domain.point.PointCommand;

public record PointRequest(
) {
        public record Charge(
                @Positive
                @NotNull
                Integer amount
        ){
                PointCommand.Charge toCommand(Long userId){
                        return new PointCommand.Charge(userId, amount);
                }
        }
}
