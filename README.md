# Dorm Cleaning Management System
**Dorm Cleaning Management System**은 학생들의 기숙사 입/퇴실 상태와 청소 담당자들의 청소 유무 상태를 QR 기반으로 관리하고,
관리자 페이지를 통해 실시간으로 방 상태 현황을 확인할 수 있는 **기숙사 관리 시스템**입니다.

## 팀 정보

| 팀장 | 팀원 |
|:----:|:----:|
| <img src="https://avatars.githubusercontent.com/Glory0206" width="100"> | <img src="https://avatars.githubusercontent.com/JangSeokyun" width="100"> |
| 서영광 | 장석윤 |


## ✨ 주요 기능
### QR 기반 입/퇴실 및 청소 상태 관리
- 학생이 QR을 스캔하면 해당 호실의 현재 상태가 표시됩니다.
- **입실 하기** 버튼을 누르면 상태가 "재실 중"으로 변경되며, 이후 **퇴실 하기** 버튼이 활성화됩니다.
- 청소 담당자는 별도의 **청소 인증 코드**를 입력해야 **청소 완료** 버튼이 활성화됩니다.
- 단, 퇴실이 완료되지 않은 상태에서는 버튼이 "재실 중"으로 보여지며 비활성화 됩니다.(입실 -> 퇴실 -> 청소 완료 순서로만 진행 가능)

### 기숙사/호실 관리(Admin 페이지)
- 생활관 및 호실 정보를 생성·조회·수정·삭제할 수 있습니다.
- 호실의 입·퇴실 및 청소 상태를 모니터링합니다.
- 호실별 QR 코드를 생성합니다.
- 청소 인증 코드를 등록하고 관리합니다.

## 🛠️ 기술 스택
### Backend
- **Java 17:** 메인 언어
- **Spring Boot:** Application Framework
- **Thymeleaf:** 서버 사이드 템플릿 엔진

### DB
- **MySQL:** RDBMS

### Frontend
- **HTML/CSS(BootStrap)/JavaScript**
- **Thymeleaf 기반 템플릿 렌더링**

### Infra / Tools
- **Gradle:** 빌드 도구
- **Docker:** 컨테이너화
- **Nginx:** 리버스 프록시
- **k6:** 부하 테스트 도구

## 프로젝트 구조
```aiexclude
project-root
 ├── docs/                # 프로젝트 문서
 ├── k6/                  # k6 부하 테스트 스크립트
 ├── nginx/
 │   └── default.conf     # Nginx 리버스 프록시 설정
 └── src
     └── main
         ├── java
         │   └── com.dorm.cleaning
         │       ├── config            # 설정
         │       ├── controller        # 요청 처리 및 라우팅
         │       ├── service           # 비즈니스 로직
         │       ├── repository        # JPA Repository 계층
         │       ├── entity (domain)   # 엔티티 모델
         │       ├── dto               # 요청/응답 데이터 구조
         │       └── exception         # 예외 처리
         └── resources
             ├── templates (Thymeleaf) # 화면 템플릿
             ├── static                # 정적 리소스
             ├── application.yml       # 공통 환경 설정
             ├── application-dev.yml   # 개발 환경 설정
             └── application-prod.yml  # 배포 환경 설정
```