package kr.hhplus.be.server.interfaces.point.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PointChargeRequest(
        @Positive
        @NotNull
        Integer amount
) {

}
