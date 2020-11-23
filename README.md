# sprinkle-rest-api

## 목차
* [실행 방법](#실행-방법)
* [요구 사항](#요구-사항)
* [상세 구현 요건 및 제약사항](#상세-구현-요건-및-제약사항)
* [개발 환경](#개발-환경)
* [문제해결 전략](#문제해결-전략)
* [API 명세](#API-명세)

## 실행 방법
* 프로젝트 위치에서 터미널을 실행하고 ./gradlew 명령어를 통해 프로젝트를 build합니다.
* cd build/libs : build 후 생성된 jar파일로 이동합니다.
* java -jar sprinkle-rest-api-0.0.1-SNAPSHOT.jar : java 명령어를 통해 실행합니다.(java11 설치필요)

## 요구 사항

* 뿌리기, 받기, 조회 기능을 수행하는 REST API 를 구현합니다.
    * 요청한 사용자의 식별값은 숫자 형태이며 "X-USER-ID" 라는 HTTP Header로 전달됩니다.
    * 요청한 사용자가 속한 대화방의 식별값은 문자 형태이며 "X-ROOM-ID" 라는 HTTP Header로 전달됩니다.
    * 모든 사용자는 뿌리기에 충분한 잔액을 보유하고 있다고 가정하여 별도로 잔액에 관련된 체크는 하지 않습니다.
* 작성하신 어플리케이션이 다수의 서버에 다수의 인스턴스로 동작하더라도 기능에 문제가 없도록 설계되어야 합니다.
* 각 기능 및 제약사항에 대한 단위테스트를 반드시 작성합니다.

## 상세 구현 요건 및 제약사항

### 1. 뿌리기 API

* 다음 조건을 만족하는 뿌리기 API를 만들어 주세요.
    * 뿌릴 금액, 뿌릴 인원을 요청값으로 받습니다.
    * 뿌리기 요청건에 대한 고유 token을 발급하고 응답값으로 내려줍니다.
    * 뿌릴 금액을 인원수에 맞게 분배하여 저장합니다. (분배 로직은 자유롭게 구현해 주세요.)
    * token은 3자리 문자열로 구성되며 예측이 불가능해야 합니다.
        
### 2. 받기 API

* 다음 조건을 만족하는 받기 API를 만들어 주세요.
    * 뿌리기 시 발급된 token을 요청값으로 받습니다.
    * token에 해당하는 뿌리기 건 중 아직 누구에게도 할당되지 않은 분배건 하나를
    API를 호출한 사용자에게 할당하고, 그 금액을 응답값으로 내려줍니다.
    * 뿌리기 당 한 사용자는 한번만 받을 수 있습니다.
    * 자신이 뿌리기한 건은 자신이 받을 수 없습니다.
    * 뿌린기가 호출된 대화방과 동일한 대화방에 속한 사용자만이 받을 수
    있습니다.
    * 뿌린 건은 10분간만 유효합니다. 뿌린지 10분이 지난 요청에 대해서는 받기 실패 응답이 내려가야 합니다.

### 3. 조회 API

* 다음 조건을 만족하는 조회 API를 만들어 주세요.
    * 뿌리기 시 발급된 token을 요청값으로 받습니다.
    * token에 해당하는 뿌리기 건의 현재 상태를 응답값으로 내려줍니다. 현재
    상태는 다음의 정보를 포함합니다.
    * 뿌린 시각, 뿌린 금액, 받기 완료된 금액, 받기 완료된 정보 ([받은 금액, 받은
    사용자 아이디] 리스트)
    * 뿌린 사람 자신만 조회를 할 수 있습니다. 다른사람의 뿌리기건이나 유효하지
    않은 token에 대해서는 조회 실패 응답이 내려가야 합니다.
    * 뿌린 건에 대한 조회는 7일 동안 할 수 있습니다.
    
 ## 개발 환경

* Language: Java 11
* Framework: Spring Boot 2.4.0.RELEASE
* Database: H2
* 형상관리: Git

## 문제해결 전략

### 1. 도메인주도설계
* Business Logic을 도메인에 분산하여 응집도를 높히고 결합도를 낮춤
* 단어사전, 용어사전 정의하여 용어 통일성 확보
<img width="382" alt="words" src="https://user-images.githubusercontent.com/40617794/99963060-899cbf80-2dd4-11eb-8f5a-0a321b00d085.png">
<img width="486" alt="terms" src="https://user-images.githubusercontent.com/40617794/99963086-928d9100-2dd4-11eb-83fe-d527c3d72e9a.png">

### 2. DB 모델링
* 뿌리기(Sprinkling), 받기(Receiving) 테이블로 구성
<img width="376" alt="테이블 구성" src="https://user-images.githubusercontent.com/40617794/99963945-e3ea5000-2dd5-11eb-97cb-a420c21a84e3.png">

### 3. Rest API
* Rest API 요건 중에서도 Uniform Interface의 self-descrptive messages와 HATEOS를 만족하고자 함.
* Self-descriptive: profile 링크를 이용하여 메시지 상세 API문서 조회
* HATEOS: Spring HATEOS 사용

### 4. JPA 낙관적 Lock
* JPA Version 낙관적 Lock을 이용하여 격리성 보장

### 5. 테스트
* api : request header, parameter 검증 테스트, Spring Rest Docs 작성
* domain : Sprinkling Entity 기능 검증 
   * 받기, Max Random Money 값 가져오기, 받기 완료된 List 가져오기, 총 받은 금액 가져오기 기능 테스트
* infra : Random Token 발급기, 뿌리기 금액 분배기 기능 동작여부 테스트
* repository : Receiving Entity 낙관적 Lock 동작여부 테스트
* service : SprinklingValidator, SprinklingMapper 기능 테스트
   * SprinklingValidatorTest : 요청 값 논리적 검증, 뿌리기 만료 여부 등의 검증기능 동작 테스트
   * SprinklingMapperTest : SprinklingMapper 클래스가 요청 값을 통해 만들어낸 Sprinkling의 데이터 정상여부 테스트
   
### 6. 토큰 생성, 뿌리기 금액 나누기
* SecureRandom 클래스를 이용하여 random하게 값 생성

### 7. API 문서(뿌리기, 받기, 조회)
* Spring Rest Docs를 이용하여 API 문서 작성
* API 호출 응답 링크 중 profile 링크를 이용해 API기능 참조
* 조회 Url : http://localhost:8080/docs/index.html
   * 뿌리기 : http://localhost:8080/docs/index.html#sprinkling-create
   * 받기 : http://localhost:8080/docs/index.html#sprinkling-receive
   * 조회 : http://localhost:8080/docs/index.html#sprinkling-get
   
### 8. 예외 처리
* ExceptionHandler를 정의하여 각 상황에서 발생하는 예외를 구분해서 처리
* 리턴 값으로 에러 코드, 에러 메세지, 디버그 메세지, 파라미터 검증 메세지를 Dto에 담아서 반환

## API 명세

#### 뿌리기 API (`POST` /api/sprinklings)

Request headers

| Name | Description |
| --- | --- |
| X-USER-ID | 사용자 ID |
| X-ROOM-ID | 대화방 ID |

Request fields

| Path | Type | Description |
| --- | --- | --- |
| sprinkledMoney | Number | 뿌릴 금액 |
| peopleCount | Number | 뿌릴 인원 |

HTTP request

```
POST /api/sprinklings HTTP/1.1
Content-Type: application/json;charset=UTF-8
X-USER-ID: 100
X-ROOM-ID: R1
Accept: application/hal+json
Content-Length: 50

{
  "sprinkledMoney" : 1000,
  "peopleCount" : 4
}
```

Response fields

| Path | Type | Description |
| --- | --- | --- |
| id | Number | 뿌리기 ID |
| roomId | String | 뿌리기가 생성된 대화방 ID |
| creatorId | Number | 뿌리기 생성자 ID |
| peopleCount | Number | 뿌린 인원 |
| sprinkledMoney | Number | 뿌린 금액 |
| token | String | 예측 불가능한 3자리 문자열 토큰 |
| maxRandomMoney | Number | 받을 수 있는 최고 금액 |

Example response

```
HTTP/1.1 201 Created
Location: http://localhost:8080/api/sprinklings/1
Content-Type: application/hal+json
Content-Length: 504

{
  "data" : {
    "id" : 1,
    "roomId" : "R1",
    "creatorId" : 100,
    "peopleCount" : 4,
    "sprinkledMoney" : 1000,
    "token" : "k8A",
    "maxRandomMoney" : 250
  },
  "code" : "OK",
  "message" : "요청이 성공하였습니다.",
  "_links" : {
    "self" : {
      "href" : "http://localhost:8080/api/sprinklings/1"
    },
    "receiving" : {
      "href" : "http://localhost:8080/api/sprinklings/1"
    },
    "profile" : {
      "href" : "/docs/index.html#sprinkling-create"
    }
  }
}
```

#### 받기 API (`PUT` /api/sprinklings/{id})

Path Variable

| Parameter | Description |
| --- | --- |
| id | 뿌리기 아이디 |

Request headers

| Name | Description |
| --- | --- |
| X-USER-ID | 수신자 ID |
| X-ROOM-ID | 대화방 ID |
| X-TOKEN | 토큰 |

HTTP request

```
PUT /api/sprinklings/1 HTTP/1.1
Content-Type: application/json;charset=UTF-8
X-USER-ID: 1000
X-ROOM-ID: R1
X-TOKEN: aHZ
Accept: application/hal+json
```

Response fields

| Path | Type | Description |
| --- | --- | --- |
| id | Number | 뿌리기 ID |
| roomId | String | 뿌리기가 생성된 대화방 ID |
| creatorId | Number | 뿌리기 생성자 ID |
| receiverId | Number | 받기 완료한 유저 ID |
| receivedMoney | Number | 받은 금액 |
| isMaxRandomMoney | Boolean | 최고 받은 금액 여부 |

Example response

```
HTTP/1.1 200 OK
Content-Type: application/hal+json
Content-Length: 404

{
  "data" : {
    "id" : 1,
    "roomId" : "R1",
    "creatorId" : 100,
    "receiverId" : 1000,
    "receivedMoney" : 574,
    "isMaxRandomMoney" : false
  },
  "code" : "OK",
  "message" : "요청이 성공하였습니다.",
  "_links" : {
    "self" : {
      "href" : "http://localhost:8080/api/sprinklings/1"
    },
    "profile" : {
      "href" : "/docs/index.html#sprinkling-receive"
    }
  }
}
```

#### 조회 API (`GET` /api/sprinklings/{id})

Path Variable

| Parameter | Description |
| --- | --- |
| id | 뿌리기 아이디 |

Request headers

| Name | Description |
| --- | --- |
| X-USER-ID | 조회자 ID |
| X-TOKEN | 토큰 |

HTTP request

```
GET /api/sprinklings/1 HTTP/1.1
X-USER-ID: 100
X-TOKEN: aHZ
```

Response fields

| Path | Type | Description |
| --- | --- | --- |
| id | Number | 뿌리기 ID |
| roomId | String | 뿌리기가 생성된 대화방 ID |
| creatorId | Number | 뿌리기 생성자 ID |
| sprinkledTime | String | 뿌리기 생성시간 |
| sprinkledMoney | Number | 뿌린 금액 |
| receivedMoney | Number | 받은 금액 |
| maxRandomMoney | Number | 최고 받을 수 있는 금액 |
| receivingDtos[].receivedMoney | Number | 받기 완료된 정보 [받은 금액] 리스트 |
| receivingDtos[].receiverId | Number | 받기 완료된 정보 [받은 사용자 아이디] 리스트 |

Example response

```
HTTP/1.1 200 OK
Content-Type: application/hal+json
Content-Length: 690

{
  "data" : {
    "id" : 1,
    "roomId" : "R1",
    "creatorId" : 100,
    "sprinkledTime" : "2020-11-23T21:27:50.40813",
    "sprinkledMoney" : 1000,
    "receivedMoney" : 857,
    "maxRandomMoney" : 574,
    "receivingDtos" : [ {
      "receivedMoney" : 283,
      "receiverId" : 1001
    }, {
      "receivedMoney" : 574,
      "receiverId" : 1002
    } ]
  },
  "code" : "OK",
  "message" : "요청이 성공하였습니다.",
  "_links" : {
    "self" : {
      "href" : "http://localhost:8080/api/sprinklings/1"
    },
    "receiving" : {
      "href" : "http://localhost:8080/api/sprinklings/1"
    },
    "profile" : {
      "href" : "/docs/index.html#sprinkling-read"
    }
  }
}
```
