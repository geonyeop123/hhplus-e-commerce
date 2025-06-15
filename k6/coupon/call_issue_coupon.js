import http from 'k6/http';
import { check, sleep } from 'k6';

const COUPON_ID = '1';

export let options = {
    scenarios: {
        peak_10000: {
            executor: 'per-vu-iterations',
            vus: 10000,       // 10,000명의 가상 사용자
            iterations: 1,    // 각 VU당 1회 요청
            startTime: '0s',  // 시작 지연 없이 바로 실행
        },
    },
};

// 사용자 ID 범위 (가상 사용자의 ID를 생성하는 함수)
function generateUserId() {
    return Math.floor(Math.random() * 10000) + 1;
}

export default function () {

    const userId = __VU;

    const headers = {
        'Content-Type': 'application/json',
        'X-USER-ID': userId,
    };

    const url = `http://localhost:8080/api/v1/coupons/call/${COUPON_ID}`;

    const response = http.post(url, null, { headers });

    check(response, {
        'status is 200': (r) => r.status === 200,
        'status is 409 (Already Issued)': (r) => r.status === 409,
        'status is 500': (r) => r.status === 500,
    });

    sleep(0.1);
}