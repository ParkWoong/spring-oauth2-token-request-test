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

`HttpUtils.getClientInfo` reads a client ID and secret from an HTTP Basic authorization header.
It returns `null` when the header is missing or malformed and does not log credentials.

`AuthorizationCodeForm.newBodyFromCode` builds a form body with `client_id`, `client_secret`,
`grant_type=authorization_code`, `code`, and `redirect_uri`. It URL-encodes field values so
special characters stay inside their fields. The caller supplies the authorization code,
credentials, and redirect URI.

TokenRequestController checks a Basic header and returns a form preview with the secret hidden.

### Run and test

Use JDK 8 and Maven 3.6.3+ from the repository root:

```sh
java -version
mvn clean verify
mvn spring-boot:run
```

`TokenRequestTest` checks Basic header decoding, missing or malformed headers, form encoding,
and missing fields. The app listens on localhost:8080. Tests use sample values and do not call
an identity provider.

#### Postman API

##### 1. Read Basic credentials

**Request**

- Method: POST
- URL: http://localhost:8080/api/oauth/basic
- Authorization: Postman Basic Auth: username sample, password secret
- Body: none

**Response**

- Status: 200 OK

~~~json
{
  "clientId": "sample",
  "secretPresent": true
}
~~~

**Note:** The response does not include the secret.

##### 2. Preview the encoded form

**Request**

- Method: POST
- URL: http://localhost:8080/api/oauth/form
- Content-Type: application/json

~~~json
{
  "code": "abc",
  "clientId": "sample",
  "clientSecret": "secret",
  "redirectUri": "http://localhost/callback"
}
~~~

**Response**

- Status: 200 OK

~~~json
{
  "form": "client_id=sample&client_secret=REDACTED&grant_type=authorization_code&code=abc&redirect_uri=http%3A%2F%2Flocalhost%2Fcallback"
}
~~~

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

`HttpUtils.getClientInfo`는 HTTP Basic 인증 헤더에서 클라이언트 ID와 secret을 읽는다.
헤더가 없거나 형식이 잘못되면 `null`을 반환하며 인증 정보를 로그에 남기지 않는다.

`AuthorizationCodeForm.newBodyFromCode`는 `client_id`,
`client_secret`, `grant_type=authorization_code`, `code`,
`redirect_uri`를 담은 form 본문을 만든다. 특수문자가 필드 값을 벗어나지 않도록 URL 인코딩한다. 인증
코드, 인증 정보, redirect URI는 호출자가 전달한다.

TokenRequestController는 Basic 헤더를 확인하고 secret을 가린 form 미리보기를 반환한다.

### 실행과 테스트

JDK 8, Maven 3.6.3 이상 사용. 저장소 루트에서 실행.

```sh
java -version
mvn clean verify
mvn spring-boot:run
```

`TokenRequestTest`는 Basic 헤더 해석, 누락·오류 헤더, form 인코딩, 필수값 누락을 확인한다.
앱은 localhost:8080에서 실행된다. 테스트에는 가상 값을 사용하며 실제 인증 서버를 호출하지 않는다.

#### Postman API

##### 1. Basic 인증 정보 읽기

**요청**

- 메서드: POST
- URL: http://localhost:8080/api/oauth/basic
- 인증: Postman Basic Auth: 사용자 이름 sample, 비밀번호 secret
- 본문: 없음

**응답**

- 상태: 200 OK

~~~json
{
  "clientId": "sample",
  "secretPresent": true
}
~~~

**참고:** 응답에 secret 값은 포함되지 않음.

##### 2. 인코딩된 form 미리보기

**요청**

- 메서드: POST
- URL: http://localhost:8080/api/oauth/form
- Content-Type: application/json

~~~json
{
  "code": "abc",
  "clientId": "sample",
  "clientSecret": "secret",
  "redirectUri": "http://localhost/callback"
}
~~~

**응답**

- 상태: 200 OK

~~~json
{
  "form": "client_id=sample&client_secret=REDACTED&grant_type=authorization_code&code=abc&redirect_uri=http%3A%2F%2Flocalhost%2Fcallback"
}
~~~
