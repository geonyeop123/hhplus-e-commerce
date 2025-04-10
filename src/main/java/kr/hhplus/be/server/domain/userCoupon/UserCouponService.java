package kr.hhplus.be.server.domain.userCoupon;

import kr.hhplus.be.server.domain.common.PageResult;
import kr.hhplus.be.server.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserCouponService {

    private final UserService userService;
    private final UserCouponRepository userCouponRepository;

    public PageResult<UserCoupon> findAllByUserId(UserCouponCommand.FindAll command) {

        userService.findByUserId(command.userId());

        long totalCount = userCouponRepository.findCountByUserId(command.userId());

        List<UserCoupon> userCoupons = userCouponRepository.findAllByUserId(command);

        if (null == userCoupons) {
            userCoupons = new ArrayList<>();
        }

        return PageResult.create(userCoupons, command.pageNo(), command.pageSize(), totalCount);
    }

    public UserCoupon findById(Long id) {
        return userCouponRepository.findById(id)
                            .orElseThrow(() -> new IllegalArgumentException("조회된 쿠폰이 없습니다."));
    }

    public UserCoupon validate(Long userId, Long userCouponId) {
        UserCoupon userCoupon = findById(userCouponId);
        userCoupon.validate(userId);
        return userCoupon;
    }

    public UserCoupon use(UserCouponCommand.Use command) {

        UserCoupon userCoupon = findById(command.userCouponId());
        userCoupon.use(command.userId(), command.orderId());

        return userCouponRepository.save(userCoupon);
    }
}
