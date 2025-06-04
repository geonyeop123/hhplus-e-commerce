package kr.hhplus.be.domain.userCoupon;


import kr.hhplus.be.domain.common.PageResult;
import kr.hhplus.be.domain.coupon.Coupon;
import kr.hhplus.be.domain.user.User;
import kr.hhplus.be.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserCouponService {

    private final UserService userService;
    private final UserCouponRepository userCouponRepository;

    public List<Long> findIssueTargetUserIds(UserCouponCommand.FindIssueTarget command) {
        return userCouponRepository.findIssueTarget(command.couponId(), command.quantity());
    }

    public UserCoupon findById(Long id) {
        return userCouponRepository.findById(id)
                            .orElseThrow(() -> new IllegalArgumentException("조회된 쿠폰이 없습니다."));
    }

    @Transactional(readOnly = true)
    public PageResult<UserCoupon> findAllByUserId(UserCouponCommand.FindAll command) {
        // Pageable은 page를 0부터 인식
        int pageNo = command.pageNo() - 1;
        User user = command.user();
        Pageable pageable = PageRequest.of(pageNo, command.pageSize()
                            , Sort.by("createdAt").descending());

        Page<UserCoupon> page = userCouponRepository.findAllByUserId(user.getId(), pageable);

        return PageResult.create(page.getContent(), command.pageNo(), command.pageSize(), page.getTotalElements());
    }

    public UserCoupon issue(UserCouponCommand.Issue command){
        Long userId = command.user().getId();
        Long couponId = command.coupon().getId();
        Coupon coupon = command.coupon();

        return userCouponRepository
            .findByUserIdAndCouponId(userId, couponId)
            .map(existing -> {
                log.warn("이미 보유한 쿠폰입니다. couponId : {}, userCouponId: {}", couponId, existing.getId());
                return existing;
            })
            .orElseGet(() -> userCouponRepository.save(coupon.issueTo(command.user())));
    }

    public UserCouponInfo validateAndGetInfo(UserCouponCommand.Validate command) {
        if(command.isEmptyCoupon()){
            return UserCouponInfo.empty();
        }

        UserCoupon userCoupon = findById(command.userCouponId());
        userCoupon.validate(command.userId());
        return UserCouponInfo.from(userCoupon);
    }

    public UserCouponInfo use(UserCouponCommand.Use command) {
        if(command.isEmptyCoupon()){
            return UserCouponInfo.empty();
        }

        UserCoupon userCoupon = findById(command.userCouponId());
        userCoupon.use(command.userId());
        return UserCouponInfo.from(userCoupon);
    }
}
