package kr.hhplus.be.server.interfaces.point;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import kr.hhplus.be.domain.point.PointCommand;
import kr.hhplus.be.domain.user.User;

public record PointRequest(
) {
        public record Charge(
                @Positive
                @NotNull
                Integer amount
        ){
                PointCommand.Charge toCommand(User user){
                        return new PointCommand.Charge(user, amount);
                }
        }
}
