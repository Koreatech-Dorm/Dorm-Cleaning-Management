import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
    // 가상 유저(vus) vus명이 duration초 동안 요청
    vus: 10,
    duration: '15s',
};

export default function () {
    const token = ``; // token은 사용에 따라 변경
    const url = `http://nginx-proxy/check?token=${token}`;

    // 도커 내부망이어서 'http://컨테이너이름' 사용
    const res = http.get(url);

    if (res.status !== 200) {
        console.log(`❌ FAILED Status: ${res.status} / URL: ${res.url}`);
    }

    check(res, { 'status was 200': (r) => r.status == 200 });

    // 1초 쉬고 다시 요청 (너무 무리 가지 않게)
    sleep(1);
}