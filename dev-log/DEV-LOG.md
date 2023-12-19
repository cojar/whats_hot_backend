# What's Hot Dev-Log

## 23-12-19
### feature/post-api-members
- [x] signup 엔드포인트 수정; post:/api/members
- [x] S-01-01 TC 작성
- [x] F-01-01-01 TC 작성
- [x] F-01-01-02 TC 작성
- [x] F-01-01-03 TC 작성

## 23-12-18
### feature/base_module-file
- [x] SaveFile 엔티티 수정
- [x] ReviewImage, SpotImage 추가 및 릴레이션 정리
- [x] F-00-00-01 TC 작성
- [x] F-00-00-02 TC 작성
- [x] validate 성공 케이스 작성, MemberControllerTest @ParameterizedTest 부분 수정
- [x] create 성공 TC 작성
- [x] 일부 오류 제거

## 23-12-16
### feature/post-api-members-login
- [x] S-01-02 TC 작성
- [x] F-01-02-01 TC 작성
- [x] F-01-02-02 TC 작성
- [x] F-01-02-03 TC 작성
- [x] F-01-02-02; member not exist 부분 코드 수정

## 23-12-15
### feature/get-api-members-me
- [x] S-01-04 TC 작성
- [x] README.md 개발로그 링크 dev tree로 변경
- [x] 테스트 코드 MemberService 의존성 주입 부분 BaseControllerTest로 이전

## 23-12-13
- [x] 컨트롤러 묘듈 패키징
- [x] 컨트롤러 테스트 파일 생성
- [x] 초기 데이터 입력 부분 변경 및 서비스 로직에 @Transactional 추가

## 23-12-12
- [x] patch:/api/comments/{id} 응답 및 api 문서 작업
- [x] delete:/api/comments/{id} 응답 및 api 문서 작업
- [x] patch:/api/comments/{id}/like 응답 및 api 문서 작업

## 23-12-11
- [x] post:/api/reviews 응답 및 api 문서 작업
- [x] get:/api/reviews 응답 및 api 문서 작업
- [x] get:/api/reviews/{id} 응답 및 api 문서 작업
- [x] patch:/api/reviews/{id} 응답 및 api 문서 작업
- [x] delete:/api/reviews/{id} 응답 및 api 문서 작업
- [x] patch:/api/reviews/{id}/like 응 및 api 문서 작업
- [x] post:/api/comments 응답 및 api 문서 작업
- [x] get:/api/comments/{id} 응답 및 api 문서 작업

## 23-12-10
- [x] post:/api/spots 응답 및 api 문서 작업
- [x] repository 이전 및 패키지 이름 수정
- [x] patch:/api/spots/{id} 응답 및 api 문서 작업
- [x] delete:/api/spots/{id} 응답 및 api 문서 작업
- [x] get:/api/spots/{id} 응답 및 api 문서 작업
- [x] get:/api/spots 응답 및 api 문서 작업

## 23-12-09
- [x] 회원 엔티티 수정

## 23-12-08
- [x] 엔티티 설계 완료

## 23-12-06
- [x] jwt 도입, member 로그인 적용, 응답 문서 작업
- [x] /api/member/me 응답 및 api 문서 작업
- [x] /api/member/me/password 응답 및 api 문서 작업
- [x] /api/member/signup 응답 및 api 문서 작업
- [x] get:/api/member/me/username 응답 및 api 문서 작업
- [x] 아이디/비밀번호 찾기 엔드포인트 수정, 응답, api 문서 작업
- [x] 로그아웃 응담 및 api 문서 작업, 비밀번호 변경 patch로 수정
- [x] App 설정 default 파일 추가

## 23-12-05
- [x] base - 프로젝트 세팅
- [x] base - global 파트 작업
- [x] base - global 파트 작업; test
- [x] base - global 파트 작업; resData에 success 여부 포함
- [x] Swagger 문서 작업 세팅, IndexController에 대한 문서 작업