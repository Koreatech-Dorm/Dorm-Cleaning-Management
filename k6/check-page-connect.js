import http from 'k6/http';
import { SharedArray } from 'k6/data';
import { check, sleep } from 'k6';
import { scenario } from 'k6/execution';

const data = new SharedArray('token', function () {
    return JSON.parse(open('./token.json'));
});

export const options = {
    scenarios: {
        dormitory_checkin: {
            // shared-iterations: 여러 VU가 총 반복 횟수를 나눠서 처리합니다.
            executor: 'shared-iterations',

            // 총 방의 개수만큼 정확히 반복 (1499번 요청 후 종료)
            iterations: 1449,

            // 동시에 입실을 시도하는 학생 수 (동시 접속자)
            vus: 50, // 가상 유저 수

            // 최대 이 시간 안에 1499건을 다 처리하지 못하면 강제 종료
            maxDuration: '10m',
        },
    },
};

export default function () {
    // 현재 반복 횟수(인덱스)를 가져와서 해당 순서의 토큰을 꺼냄
    // scenario.iterationInTest는 0부터 1499까지 증가함
    const userIndex = scenario.iterationInTest;
    const user = data[userIndex];

    // 예외 처리: 데이터보다 반복 횟수가 많을 경우
    if (!user) {
        console.error(`No user data for index ${userIndex}`);
        return;
    }

    const token = user.token;

    // 도커 내부망 호스트 사용
    const url = `http://host.docker.internal:8080/check?token=${token}`;

    const res = http.get(url);

    // 에러일 때만 로그 출력
    if (res.status !== 200) {
        console.log(`❌ FAILED [Room Index: ${userIndex}] Status: ${res.status} / Token: ${token}`);
    }

    check(res, {
        'status was 200': (r) => r.status === 200,
    });

    // 실제 사람이 QR 찍고 들어가는 텀을 고려 (0.5초 ~ 1.5초 랜덤 휴식)
    sleep(Math.random() * 1 + 0.5);
}