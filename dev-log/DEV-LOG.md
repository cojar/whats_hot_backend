# What's Hot Dev-Log

## 24-01-07
### feature/comment_module-comment
- [x] F-04-03-01 TC 수정
- [x] F-04-03-02 TC 작성
- [x] F-04-03-03 TC 작성
- [x] F-04-03-04 TC 작성
- [x] S-04-04 TC 수정
- [x] F-04-04-01 TC 수정
- [x] F-04-04-02 TC 수정
- [x] F-04-04-03 TC 수정
- [x] S-04-05 TC 수정

## 24-01-06
### feature/comment_module-comment
- [x] comment_module 서비스 로직 수정
- [x] InitConfig 수정
- [x] S-04-01 TC 수정
- [x] F-04-01-01 TC 수정
- [x] F-04-01-02 TC 수정
- [x] F-04-01-03 TC 작성
- [x] F-04-01-04 TC 작성
- [x] S-04-02 TC 수정
- [x] F-04-02-01 TC 수정
- [x] S-04-03 TC 수정

## 24-01-05
### dev
- [x] 내장 톰캣 https로 설정 변경

### feature/spring_doc
- [x] 내장 톰캣 https 설정 변경으로 인한 롤백

### feature/errors-exception_handler
- [x] application-prod.properties.default 수정
- [x] FileServiceTest TC 점검
- [x] Auth-Exception Handling 작업

## 24-01-04
### feature/post-api-categories
- [x] InitConfig 카테고리데이터 세부지역 제거 및 뎁스별 재정립

### feature/spring_doc
- [x] swagger-ui 문제 해결 시도

### feature/util
- [x] spot, review 가 데이터 추가

### fix/base_module-file
- [x] validate 로직 수정
- [x] dev-log 작성
- [x] 버그 수정

## 24-01-03
### dev
- [x] 1차 배포 작업 진행

## feature/util
- [x] WebMvcConfig 추가
- [x] properties.default 파일 수정

## 24-01-02
### feature/delete-api-reviews-id
- [x] S-03-05 TC 추가
- [x] F-03-05-01 TC 추가
- [x] F-03-05-02 TC 추가

## 24-01-01
### feature/post-api-reviews
- [x] 생성 로직 수정 및 S-03-01 TC 점검
- [x] 생성 TC 중 Bad Request 부분에서 엔티티 미생성 확인 추가
- [x] InitConfig 리뷰 생성 관련 로직 변경 및 미사용 로직 제거

## 23-12-31
### feature/member_module-member
- [x] login 로직 수정 및 로그인이 필요한 모든 TC 점검
- [x] json 문자열 직접 사용하는 부분 ObjectMapper 사용하도록 변경
- [x] logout 로직 수정 및 관련 TC 점검
- [x] me 로직 수정 및 관련 TC 점검
- [x] updatePassword 로직 수정 및 관련 TC 점검
- [x] findUsername 로직 수정 및 관련 TC 점검
- [x] findPassword 로직 수정 및 관련 TC 점검
- [x] 테스트 코드 로그인 로직 수정
- [x] 미사용 패키지 정리

## 23-12-29
### feature/post-api-categories
- [x] 맛집, 여행지, 숙박 대/중/소분류로 나누어 카테고리 편성
- [x] S-05-01 카테고리생성 TC 작성

### feature/get-api-spots-id
- [x] get spot detail 작성
- [x] S-02-03 TC 추가
- [x] F-02-04-01 TC 추가
- [x] spotController get/spot/detail 수정
- [x] S-02-03 TC 수정
- [x] F-02-04-01 TC 수정
- [x] S-02-03 TC 추가 수정
- [x] F-02-04-01 TC 추가 수정
- [x] S-02-03 TC 추가 재수정
- [x] F-02-04-01 TC 추가 재수정
- [x] 마무리 수정

### feature/patch-api-spots-id
- [x] 수정 TC 중 Bad Request 부분에서 엔티티 미수정 확인 추가
- [x] 생성 부분 체크 로직 분리
- [x] InitConfig 장소 생성 관련 로직 변경

### feature/delete-api-spots-id
- [x] S-02-05 TC 추가
- [x] F-02-05-01 TC 추가

### feature/post-api-members
- [x] 생성 로직 수정 및 S-01-01 TC 점검
- [x] 생성 TC 중 Bad Request 부분에서 엔티티 미생성 확인 추가
- [x] InitConfig 가동용 static resource 추가
- [x] 머지 후 conflict 수정

## 23-12-28
### feature/patch-api-spots-id
- [x] 수정 로직 수정
- [x] 일부 로직 수정 및 깨진 TC 잘 작동하도록 수정
- [x] 미사용 로직 제거

## 23-12-27
### feature/comment_module-comment
- [x] getValidate 오류 메시지 출력 내용 수정
- [x] getMyValidate 오류 메시지 출력 내용 수정
- [x] updateValidate 오류 메시지 출력 내용 수정
- [x] updateValidate 오류 메시지 출력 내용 중 rejectValue 수정
- [x] updateValidate missMatchedUser 오류 메시지 출력 내용 수정
- [x] deleteValidate 오류 메시지 출력 내용 수정
- [x] likeValidate 오류 메시지 출력 내용 수정

### feature/post-api-spots
- [x] 생성 로직 수정
- [x] 자식 엔티티 저장 후 refresh 되도록 수정
- [x] 생성 TC 중 Bad Request 부분에서 엔티티 미생성 확인 추가

## 23-12-26
### feature/post-api-reviews
- [x] conflict issue 해결

### feature/response
- [x] ResCode 생성
- [x] ResData 매개변수 수정
- [x] ResCode 성공 케이스 정리
- [x] 연관 컨트롤러, 서비스 로직 수정

### feature/errors-exception
- [x] ApiResponseException 생성

### feature/errors-exception_handler
- [x] ApiGlobalExceptionHandler 생성
- [x] fail case 모두 ApiResponseException 던지도록 수정
- [x] mailService fail case에 수정사항 반영

## 23-12-25
### feature/patch-api-spots-id
- [x] SpotService.create 롤백
- [x] SpotHashtagService.createAll 로직 변경

### feature/post-api-reviews
- [x] ReviewRequest.CreateReview 수정
- [x] 연관 엔티티 수정
- [x] S-03-01 TC 작성, ResData.reduceError 수정
- [x] S-03-01 TC - status private 작성
- [x] S-03-01 TC - without hashtags 작성
- [x] S-03-01 TC - without images 작성
- [x] F-03-01-01 TC 작성
- [x] F-03-01-01 -> F-03-01-02 수정
- [x] F-03-01-01 TC 작성
- [x] ReviewHashTag.createAll 로직 변경
- [x] F-00-00-01 TC 작성
- [x] F-00-00-02 TC 작성
- [x] 여러 파일 에러 동시 발생 TC 작성; F-00-00-01, F-00-00-02

## 23-12-24
### feature/post-api-spots
- [x] F-02-01-01 TC 작성
- [x] F-02-01-02 TC 작성
- [x] F-02-01-03 TC 작성
- [x] F-02-01-04 TC 작성
- [x] F-00-00-01 TC 작성
- [x] F-00-00-02 TC 작성
- [x] 여러 파일 에러 동시 발생 TC 작성; F-00-00-01, F-00-00-02

### feature/patch-api-spots-id
- [x] S-02-04 TC 작성
- [x] S-02-04 partial input request TC 작성
- [x] S-02-04 partial input images TC 작성
- [x] F-02-04-01 TC 작성
- [x] S-02-04 partial input only images TC 작성
- [x] F-02-04-02 TC 작성
- [x] F-02-04-03 TC 작성
- [x] F-02-04-04 TC 작성
- [x] F-02-04-05 TC 작성
- [x] F-00-00-01 TC 작성
- [x] F-00-00-02 TC 작성
- [x] 여러 파일 에러 동시 발생 TC 작성; F-00-00-01, F-00-00-02

## 23-12-23
### feature/patch-api-comments-like-id
- [x] toggleLike 추가 수정
- [x] S-04-06 TC like TC와 unlike TC로 분할
- [x] S-04-06 unlike TC 수정
- [x] S-04-06 unlike TC 추가 수정

### feature/post-api-spots
- [x] _File 엔티티도 마지막에 저장되도록 수정

## 23-12-22
### feature/patch-api-comments-like-id
- [x] patch likeComment/id 작성
- [x] S-04-06 TC 작성
- [x] F-04-06-01 TC 작성
- [x] F-04-06-02 TC 작성
- [x] toggleLike 수정

### feature/delete-api-comments-id
- [x] delete comments/id 작성
- [x] S-04-05 TC 작성
- [x] F-04-05-01 TC 작성
- [x] F-04-05-02 TC 작성
- [x] TC 일부 수정

### feature/patch-api-comments-id
- [x] patch comments/id 작성
- [x] S-04-04 TC 작성
- [x] F-04-04-01 TC 작성
- [x] F-04-04-02 TC 작성
- [x] F-04-04-03 TC 작성
- [x] update 수정
- [x] TC 일부 수정
- [x] F-04-04-01 TC 수정
- [x] TC 추가 수정 및 오류 메시지 수정
- [x] validate 수정

### feature/post-api-spots
- [x] fail, exception 발생 시 db 저장되지 않도록 변경, update 로직 service 코드로 이전

## 23-12-21
### feature/get-api-comments-id
- [x] get comments/id 작성
- [x] S-04-02 TC 이전
- [x] F-04-02-01 TC 이전

### feature/get-api-comments
- [x] getAllByAuthor 수정
- [x] validate 수정
- [x] get comments/id 분리

### feature/post-api-members
- [x] F-00-00-01 TC 작성
- [x] F-00-00-02 TC 작성

### feature/post-api-spots
- [x] S-02-01 TC 작성 및 관련 엔티티, DTO 수정

## 23-12-20
### feature/get-api-comments
- [x] S-04-02 TC 작성
- [x] F-04-02-01; validate 작성
- [x] F-04-02-01 TC 작성
- [x] get comments/me 작성
- [x] S-04-03 TC 작성
- [x] F-04-03-01 TC 작성

### feature/post-api-comments
- [x] create 작성
- [x] S-04-01 TC 작성
- [x] F-04-01-01 TC 작성
- [x] F-04-01-02 TC 작성

### feature/post-api-members-logout
- [x] S-01-03 TC 작성

### feature/patch-api-members-password
- [x] S-01-05 TC 작성
- [x] F-01-05-01 TC 작성
- [x] F-01-05-02 TC 작성
- [x] F-01-05-03 TC 작성

### feature/post-api-members-username
- [x] S-01-06 TC 작성
- [x] F-01-06-01 TC 작성
- [x] F-01-06-02 TC 작성

### feature/post-api-members-password
- [x] S-01-07 TC 작성
- [x] 임시 비밀번호 이메일 발송
- [x] F-01-07-01 TC 작성
- [x] F-01-07-02 TC 작성
- [x] 이메일 발송 예외 시 임시 비밀번호 저장되지 않도록 변경

## 23-12-19
### feature/post-api-members
- [x] signup 엔드포인트 수정; post:/api/members
- [x] S-01-01 TC 작성
- [x] F-01-01-01 TC 작성
- [x] F-01-01-02 TC 작성
- [x] F-01-01-03 TC 작성
- [x] F-01-01-04 TC 작성
- [x] post:/api/members api 문서 수정

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
