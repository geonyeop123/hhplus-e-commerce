package kr.hhplus.be.domain.userCoupon;


import kr.hhplus.be.domain.common.GlobalEventPublisher;
import kr.hhplus.be.domain.common.PageResult;
import kr.hhplus.be.domain.coupon.Coupon;
import kr.hhplus.be.domain.user.User;
import kr.hhplus.be.domain.user.UserService;
import kr.hhplus.be.support.exception.AlreadyIssuedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserCouponService {

    private final UserService userService;
    private final UserCouponRepository userCouponRepository;
    private final GlobalEventPublisher globalEventPublisher;


    public void callIssueUserCoupon(UserCouponCommand.CallIssue command) {
        boolean isFail = !userCouponRepository.callIssue(command.user().getId(), command.couponId());
        if(isFail){
           throw new AlreadyIssuedException("이미 발급 요청한 쿠폰입니다.", command.user().getId(), command.couponId());
        }
    }

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

        Optional<UserCoupon> byUserIdAndCouponId = userCouponRepository.findByUserIdAndCouponId(command.user().getId(), command.coupon().getId());

        UserCoupon userCoupon;

        if(byUserIdAndCouponId.isPresent()){
            userCoupon = byUserIdAndCouponId.get();
            log.error("이미 발급받은 쿠폰입니다.");
            userCouponRepository.issueChecked(command.user().getId(), command.coupon().getId());
        }else{
            Coupon coupon = command.coupon();
            userCoupon = coupon.issueTo(command.user());
            userCouponRepository.save(userCoupon);
        }

        return userCoupon;
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
