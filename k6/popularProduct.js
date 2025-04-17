import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
    stages: [
        { duration: '10s', target: 50 },   // 0 → 50명까지 증가
        { duration: '20s', target: 100 },  // 유지
        { duration: '10s', target: 200 },  // 더 증가
        { duration: '10s', target: 0 },    // 감소 및 종료
    ],
};

export default function () {
    const res = http.get('http://localhost:8080/api/v1/stats/products/popular');
    check(res, {
        'is status 200': (r) => r.status === 200,
    });
    sleep(0.1); // 사용자당 0.1초 대기
}

