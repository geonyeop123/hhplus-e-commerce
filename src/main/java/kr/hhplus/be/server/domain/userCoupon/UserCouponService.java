package kr.hhplus.be.server.domain.userCoupon;

import kr.hhplus.be.server.domain.common.PageResult;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserCouponService {

    private final UserService userService;
    private final UserCouponRepository userCouponRepository;

    public UserCoupon findById(Long id) {
        return userCouponRepository.findById(id)
                            .orElseThrow(() -> new IllegalArgumentException("조회된 쿠폰이 없습니다."));
    }

    public PageResult<UserCoupon> findAllByUserId(UserCouponCommand.FindAll command) {

        // Pageable은 page를 0부터 인식
        int pageNo = command.pageNo() - 1;
        userService.findById(command.userId());
        Pageable pageable = PageRequest.of(pageNo, command.pageSize()
                            , Sort.by("createdAt").descending());

        Page<UserCoupon> page = userCouponRepository.findAllByUserId(command.userId(), pageable);

        return PageResult.create(page.getContent(), command.pageNo(), command.pageSize(), page.getTotalElements());
    }

    @Transactional
    public UserCoupon issue(UserCouponCommand.Issue command){
        Coupon coupon = command.coupon();
        LocalDate now = LocalDate.now();
        UserCoupon userCoupon = coupon.issueTo(command.user(), now);
        return userCouponRepository.save(userCoupon);
    }

    public UserCoupon validate(UserCouponCommand.Validate command) {
        UserCoupon userCoupon = findById(command.userCouponId());
        userCoupon.validate(command.userId());
        return userCoupon;
    }

    public UserCouponInfo validateAndGetInfo(UserCouponCommand.Validate command) {
        UserCoupon userCoupon = findById(command.userCouponId());
        userCoupon.validate(command.userId());
        return UserCouponInfo.from(userCoupon);
    }

    @Transactional
    public UserCoupon use(UserCouponCommand.Use command) {

        UserCoupon userCoupon = findById(command.userCouponId());
        userCoupon.use(command.userId(), command.orderId());

        return userCouponRepository.save(userCoupon);
    }
}
