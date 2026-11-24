# OAuth2 Token Request Helpers / OAuth2 토큰 요청 유틸리티

[ENG](#eng) | [KR](#kr)

## ENG

### Project structure

```text
pom.xml
src/main/java/io/github/parkwoong/oauth/Application.java
src/main/java/io/github/parkwoong/oauth/TokenRequestController.java
src/main/java/io/github/parkwoong/oauth/HttpUtils.java
src/main/java/io/github/parkwoong/oauth/AuthorizationCodeForm.java
src/main/resources/application.properties
src/test/java/io/github/parkwoong/oauth/TokenRequestTest.java
```

### Code explanation

`HttpUtils.getClientInfo` reads a client ID and secret from an HTTP Basic authorization header. It returns `null` when the header is missing or malformed and does not log credentials.

`AuthorizationCodeForm.newBodyFromCode` builds a form body with `client_id`, `client_secret`, `grant_type=authorization_code`, `code`, and `redirect_uri`. It URL-encodes field values so special characters stay inside their fields. The caller supplies the authorization code, credentials, and redirect URI.

TokenRequestController checks a Basic header and returns a form preview with the secret hidden.

### Run and test

Use JDK 8 and Maven 3.6.3+ from the repository root:

```sh
java -version
mvn clean verify
mvn spring-boot:run
```

`TokenRequestTest` checks Basic header decoding, missing or malformed headers, form encoding, and missing fields. The app listens on localhost:8080. Tests use sample values and do not call an identity provider.

#### Postman API

In Postman's Basic Auth tab, set username sample and password secret, then send POST http://localhost:8080/api/oauth/basic with no body. Expected: HTTP 200, {"clientId":"sample","secretPresent":true}; the secret is not returned. To preview form encoding, send POST http://localhost:8080/api/oauth/form with Content-Type: application/json and body {"code":"abc","clientId":"sample","clientSecret":"secret","redirectUri":"http://localhost/callback"}. Expected: HTTP 200 with form starting client_id=sample&client_secret=REDACTED&grant_type=authorization_code.

## KR

### 프로젝트 구조

```text
pom.xml
src/main/java/io/github/parkwoong/oauth/Application.java
src/main/java/io/github/parkwoong/oauth/TokenRequestController.java
src/main/java/io/github/parkwoong/oauth/HttpUtils.java
src/main/java/io/github/parkwoong/oauth/AuthorizationCodeForm.java
src/main/resources/application.properties
src/test/java/io/github/parkwoong/oauth/TokenRequestTest.java
```

### 코드 설명

`HttpUtils.getClientInfo`는 HTTP Basic 인증 헤더에서 클라이언트 ID와 secret을 읽습니다. 헤더가 없거나 형식이 잘못되면 `null`을 반환하며 인증 정보를 로그에 남기지 않습니다.

`AuthorizationCodeForm.newBodyFromCode`는 `client_id`, `client_secret`, `grant_type=authorization_code`, `code`, `redirect_uri`를 담은 form 본문을 만듭니다. 특수문자가 필드 값을 벗어나지 않도록 URL 인코딩합니다. 인증 코드, 인증 정보, redirect URI는 호출자가 전달합니다.

TokenRequestController는 Basic 헤더를 확인하고 secret을 가린 form 미리보기를 반환합니다.

### 실행과 테스트

저장소 루트에서 JDK 8과 Maven 3.6.3 이상을 사용하세요.

```sh
java -version
mvn clean verify
mvn spring-boot:run
```

`TokenRequestTest`는 Basic 헤더 해석, 누락·오류 헤더, form 인코딩, 필수값 누락을 확인합니다. 앱은 localhost:8080에서 실행됩니다. 테스트에는 가상 값을 사용하며 실제 인증 서버를 호출하지 않습니다.

#### Postman API

Postman의 Basic Auth에서 사용자 이름 sample, 비밀번호 secret을 설정하고 본문 없이 POST http://localhost:8080/api/oauth/basic을 보내세요. 예상 결과: HTTP 200, {"clientId":"sample","secretPresent":true}이며 secret은 반환하지 않습니다. form 인코딩은 Content-Type: application/json으로 POST http://localhost:8080/api/oauth/form에 {"code":"abc","clientId":"sample","clientSecret":"secret","redirectUri":"http://localhost/callback"}을 보내 확인하세요. 예상 결과: HTTP 200이며 form은 client_id=sample&client_secret=REDACTED&grant_type=authorization_code로 시작합니다.
