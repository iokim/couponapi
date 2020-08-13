# 개발 프레임워크
* JDK8
* SpringBoot
* JPA
* H2 DataBase
* JWT
* Gradle

# 문제해결전략
* 쿠폰 생성(16자리 숫자)
* 사용자ID를 입력받아서 쿠폰 지급
* 사용자에게 지급된 쿠폰 조회(지급여부만 확인, 사용여부는 확인 안함)
* 쿠폰 사용시 used true 처리 -> used true인 쿠폰 재사용 불가
* 쿠폰 취소시 used false 처리 -> 재사용 가능
* 당일 만료되는 쿠폰 조회
* 회원 비밀번호 spring security의 PasswordEncoder로 암호화

# 빌드 및 실행 방법
* 빌드
cd api
gradle bootJar

* 실행
cd api/build/libs
java -jar api-0.0.1-SNAPSHOT.jar
