# 데일리 스크랩 주식 정보
종목 코드 입력을 통해 최근 5영업일 간의 최고, 최저, 개장, 종가, 거래량, 거래일자별 데이터를 수집하여 리턴 한다.

### 실행
#### 도커 컴포즈로 실행

    docker-compose up

### 프로젝트 빌드
#### jib 플러그인을 이용한 원격 resistry 갱신

    ./gradlew jib
or

    ./gradle jib



### 사용도구
- Spring Boot 2.0
- Gradle
- MyBatis framewwork
- Swagger
- JUnit 5.0
- postgreSQL
- JiB Plugin

### 스크린샷
![swagger](https://user-images.githubusercontent.com/11978687/202740337-db2479bf-6758-4c67-a167-8e304baebd89.png)
- http://localhost:8080/swagger-ui/

![docker](https://user-images.githubusercontent.com/11978687/202834338-531ded68-fb98-43d0-ae4e-ede76b157c77.jpg)
- docker 이미지

![data](https://user-images.githubusercontent.com/11978687/202740329-fe12bed3-6035-421a-976b-cb1b70b062ac.png)
- DB 저장내역