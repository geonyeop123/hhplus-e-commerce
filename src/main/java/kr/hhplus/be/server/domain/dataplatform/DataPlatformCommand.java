package kr.hhplus.be.server.domain.dataplatform;

import kr.hhplus.be.server.domain.order.OrderInfo;

public record DataPlatformCommand(
    OrderInfo orderInfo
) {

}
