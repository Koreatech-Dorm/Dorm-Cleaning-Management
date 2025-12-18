# GitHub Rules

---

## Commit Message 규칙 (7가지)

1. 제목과 본문은 한 줄을 띄워 구분한다.
2. 제목은 50자 이내로 작성한다.
3. 제목의 첫 글자는 대문자로 작성한다.
4. 제목 끝에는 마침표를 사용하지 않는다.
5. 제목은 **명령문**으로 작성하며 과거형을 사용하지 않는다.
6. 본문의 각 행은 72자 이내로 줄바꿈한다.
7. 본문은 **어떻게(How)** 보다 **무엇을, 왜(What, Why)** 중심으로 작성한다.

---

## Commit Message 구조

커밋 메시지는 기본적으로 **제목(필수)**, **본문(선택)**, **꼬리말(Footer, 선택)** 로 구성한다.

```text
<type>: <subject>

<body>

<footer>
```

### Type

* **feat** : 새로운 기능 추가 또는 기존 기능 수정
* **fix** : 버그 수정
* **build** : 빌드 관련 설정 수정
* **chore** : 기타 변경 사항 (예: 설정 파일, 의존성 관리)
* **ci** : CI/CD 관련 설정 수정
* **docs** : 문서 또는 주석 수정
* **style** : 코드 스타일, 포맷팅 변경 (동작 변경 없음)
* **refactor** : 코드 리팩터링 (기능 변화 없음)
* **test** : 테스트 코드 추가 또는 수정
* **release** : 버전 릴리즈 작업

### 예시

```text
feat: Add login API
```

### Footer (이슈 자동 종료)

```text
Closes #1
```

* 이슈 번호를 포함하면 PR이 **default branch**로 머지될 때 이슈가 자동 종료된다.

---

## Issue 규칙

* **목적**: 해야 할 작업을 명확히 정의하고, 단일 목적 단위로 관리한다.
* **제목 형식**: `[타입] 핵심 목표를 한 줄로`

    * 예시

        * `[feat] 로그인 API 추가`
        * `[fix] 회원가입 중복 이메일 예외 처리`

### 라벨 예시

* `type:feature`
* `type:bug`
* `priority:high`
* `area:auth`
* `status:in-progress`

### 브랜치 연결 규칙

* 형식: `feature/<이슈번호>-<짧은-설명>`
* 예시: `feature/101-login-api`

### 이슈 종료 방법

* PR 본문에 아래 문구를 포함한다.

```text
Closes #<이슈번호>
```

* PR이 머지되면 이슈가 자동으로 종료된다.

---

## Issue 템플릿 (통합형)

### Ⅰ. 이슈 설명 (Issue Description)

* 발생한 문제 또는 제안하는 기능에 대한 전반적인 설명을 작성한다.

### Ⅱ. 발생한 문제 (Describe what happened)

* 현재 상황, 문제점, 개선이 필요한 이유를 구체적으로 작성한다.

### Ⅲ. 기대한 동작 (Describe what you expected to happen)

* 문제가 해결되었을 때 혹은 기능이 추가되었을 때의 기대 동작을 작성한다.

### Ⅳ. 재현 방법 (How to reproduce it)

* 동일한 문제가 발생할 수 있도록 단계별 재현 과정을 작성한다.
* 기능 제안의 경우 생략 가능하다.

### Ⅴ. 추가 사항 (Anything else we need to know?)

* 참고 링크, 스크린샷, 로그 등 공유가 필요한 내용을 작성한다.

---

## Pull Request(PR) 규칙

* **승인 기준**: 최소 1~2명 승인 후 머지한다.
* **PR 크기**: 이슈 단위로 작게 나누며, 하나의 목적만 포함한다.
* **머지 후 작업**

    * 작업 브랜치를 로컬/원격 모두 삭제한다.
    * 이슈 자동 종료 여부를 확인한다.

---

## PR 제목 및 본문 형식

### PR 제목

```text
type(scope): 명령형 제목 (#이슈번호)
```

* 예시

```text
feat(auth): Add login API (#101)
```

### PR 본문 구성

* **Why**: 해결하려는 문제 또는 변경 배경
* **What**: 핵심 변경 사항 요약
* **How to test**: 테스트 방법 또는 검증 절차
* **이슈 연결**: `Closes #101`
* **스크린샷 / 로그**: UI, 오류, 성능 관련 자료 (선택)
* **주의사항 / 마이그레이션**: 필요 시 명시

---

## PR 템플릿 (통합형)

### Ⅰ. PR 내용 설명 (Describe what this PR did)

* 이 PR에서 어떤 변경이 이루어졌는지 요약한다.

### Ⅱ. 관련 이슈 (Does this pull request fix one issue?)

* 관련된 이슈 번호 또는 설명을 작성한다.(예: #101)

### Ⅲ. 검증 방법 (Describe how to verify it)

* 테스트 및 확인 절차를 구체적으로 작성한다.

### Ⅳ. 리뷰 시 참고 사항 (Special notes for reviewers)

* 리뷰 시 유의할 점이나 추가로 참고해야 할 내용을 작성한다.

---

## 브랜치 전략

### 브랜치 명명 규칙

* 기본 작업 브랜치

```text
feature/<이슈번호>-<짧은-설명>
```

* 긴급 수정 브랜치

```text
hotfix/<이슈번호>-<설명>
```

* 예시

    * `feature/101-login-api`
    * `hotfix/212-nullpointer-login`

### 작업 흐름

1. 이슈 생성
2. 브랜치 생성 및 작업
3. PR 생성 및 리뷰어 지정
4. 승인 1~2명 후 **Squash & Merge**
5. 브랜치 삭제 및 이슈 자동 종료 확인 (`Closes #`)

---

## 빠른 예시

* 이슈: `[feat] 로그인 API 추가` → `#101`
* 브랜치: `feature/101-login-api`
* PR 제목: `feat(auth): Add login API (#101)`
* PR 본문 마지막 줄:

```text
Closes #101
```

* 머지 후 브랜치 삭제 (원격 포함)
