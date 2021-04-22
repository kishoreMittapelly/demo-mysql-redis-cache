# spring-mysql-redis-cache
Demo Redis  REST API Cache example

##
### Prerequisites
- JDK 1.8
- Maven
- Mysql
- Redis

## Quick Start

```
MySQL START
```

```
Redis START
```

### Build
```
mvn clean package
```

### Run
```
java -jar target/demo-mysql-redis-cache.jar
```

##
### Get information about system health, configurations, etc.
```
http://localhost:8091/env
http://localhost:8091/health
http://localhost:8091/info
http://localhost:8091/metrics
```

##
### TEST using CURL

- data add
```
curl -d '{"title":"post title", "content":"post content"}' -H "Content-Type: application/json" -X POST http://localhost:8080/api/posts
```

- data get cache TimeUnit.SECONDS 10
```
curl http://localhost:8080/api/members/1
```

##
### Swagger-ui REST API Reference & Test
- http://localhost:8080/swagger-ui.html
- Response Content Type : application/json

##
### Redis monitor
- 


MBR_NM
MBR_GDR_CD
ADR_LN_1_TXT
ADR_LN_2_TXT
CITY_NM
ST_CD
ZIP_CD
MBR_PHONE
EMAIL

