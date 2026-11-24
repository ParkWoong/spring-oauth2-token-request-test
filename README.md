# OAuth2 Token Request Parsing / OAuth2 토큰 요청 파싱

[ENG](#eng) | [KR](#kr)

## ENG

### Project structure

```text
pom.xml
src/main/java/io/github/parkwoong/oauth/Application.java
src/main/java/io/github/parkwoong/oauth/TokenRequest.java
src/main/java/io/github/parkwoong/oauth/TokenRequestParser.java
src/main/java/io/github/parkwoong/oauth/AuthorizationCodeForm.java
src/main/resources/application.properties
src/test/java/io/github/parkwoong/oauth/TokenRequestTest.java
```

### Code explanation

`TokenRequestParser` reads client credentials from a Basic header or a form/JSON body. It accepts only the `authorization_code` grant in this example. If credentials appear in both places, they must match. Duplicate fields, malformed input, missing values, and unsupported content types are rejected. Request bodies are limited to 16 KiB.

`AuthorizationCodeForm` uses URL encoding to build a form body with `client_id`, `client_secret`, `grant_type`, `code`, and `redirect_uri`. The redirect URI must be HTTPS or localhost HTTP. The application does not contact an identity provider and does not log or return credentials. Use synthetic sample values in tests.

### Run and test

Use JDK 8 and Maven 3.6.3+ from the repository root:

```sh
java -version
mvn clean verify
mvn spring-boot:run
```

`TokenRequestTest` covers Basic authentication, JSON and form input, form encoding, duplicate fields, conflicting credentials, and invalid requests. The application starts without an HTTP server and exits; there is no real token endpoint. This example does not implement refresh tokens, automatic login, or OAuth provider integration. Basic credentials use UTF-8 and split at the first colon; it does not demonstrate OAuth's optional percent-encoding rules for client credentials.

## KR

### 프로젝트 구조

```text
pom.xml
src/main/java/io/github/parkwoong/oauth/Application.java
src/main/java/io/github/parkwoong/oauth/TokenRequest.java
src/main/java/io/github/parkwoong/oauth/TokenRequestParser.java
src/main/java/io/github/parkwoong/oauth/AuthorizationCodeForm.java
src/main/resources/application.properties
src/test/java/io/github/parkwoong/oauth/TokenRequestTest.java
```

### 코드 설명

`TokenRequestParser`는 Basic 헤더 또는 form/JSON 본문에서 클라이언트 정보를 읽습니다. 예제에서는 `authorization_code` grant만 처리합니다. 인증 정보가 두 곳에 있으면 값이 일치해야 합니다. 중복 필드, 잘못된 형식, 필수값 누락 및 지원하지 않는 콘텐츠 타입은 거부합니다. 요청 본문은 16 KiB로 제한합니다.

`AuthorizationCodeForm`은 URL 인코딩으로 `client_id`, `client_secret`, `grant_type`, `code`, `redirect_uri`가 담긴 form 본문을 만듭니다. redirect URI는 HTTPS 또는 localhost HTTP만 허용합니다. 앱은 인증 서버에 연결하지 않으며 인증 정보를 로그나 응답에 출력하지 않습니다. 테스트에는 가상 값을 사용합니다.

### 실행과 테스트

JDK 8과 Maven 3.6.3 이상을 사용하고 저장소 루트에서 실행하세요.

```sh
java -version
mvn clean verify
mvn spring-boot:run
```

`TokenRequestTest`는 Basic 인증, JSON·form 입력, form 인코딩, 중복 필드, 충돌하는 인증 정보 및 잘못된 요청을 확인합니다. 앱은 HTTP 서버 없이 시작 후 종료하며 실제 토큰 엔드포인트는 없습니다. refresh token, 자동 로그인 및 OAuth 제공자 연동은 구현하지 않았습니다. Basic 인증 정보는 UTF-8로 디코딩하고 첫 번째 콜론에서 나누며, OAuth의 선택적 클라이언트 정보 퍼센트 인코딩 규칙은 다루지 않습니다.
