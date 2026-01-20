# Docker 배포 가이드

## 📋 사전 요구사항

- Docker: 20.10 이상
- Docker Compose: 2.0 이상

## 🚀 빠른 시작

### 1. 환경 변수 설정

```bash
# .env 파일 생성
cp .env.example .env

# .env 파일 편집하여 Jasypt 암호화 키 설정
nano .env
```

### 2. Docker Compose로 실행

```bash
# 백그라운드에서 실행
docker-compose up -d

# 로그 확인
docker-compose logs -f app
```

### 3. 접속 확인

- 애플리케이션: http://localhost:8080
- PostgreSQL: localhost:5432

## 🛠️ 주요 명령어

### 컨테이너 관리

```bash
# 서비스 시작
docker-compose up -d

# 서비스 중지
docker-compose down

# 서비스 재시작
docker-compose restart

# 볼륨까지 삭제 (데이터 초기화)
docker-compose down -v
```

### 로그 확인

```bash
# 전체 로그 확인
docker-compose logs

# 특정 서비스 로그 확인
docker-compose logs app
docker-compose logs postgres

# 실시간 로그 확인
docker-compose logs -f app
```

### 애플리케이션 재빌드

```bash
# 이미지 재빌드 후 실행
docker-compose up -d --build

# 캐시 없이 완전히 재빌드
docker-compose build --no-cache
docker-compose up -d
```

## 🔧 설정 변경

### 포트 변경

`docker-compose.yml` 파일에서 포트 매핑 수정:

```yaml
services:
  app:
    ports:
      - "원하는포트:8080"
```

### 데이터베이스 비밀번호 변경

1. `docker-compose.yml` 파일에서 PostgreSQL 환경변수 수정
2. `.env` 파일에서 POSTGRES_PASSWORD 변경
3. 컨테이너 재생성: `docker-compose down && docker-compose up -d`

### Jasypt 암호화 키 변경

1. `.env` 파일에서 `JASYPT_ENCRYPTOR_PASSWORD` 수정
2. 애플리케이션 재시작: `docker-compose restart app`

## 📝 데이터 백업 및 복구

### 데이터베이스 백업

```bash
# PostgreSQL 데이터 백업
docker-compose exec postgres pg_dump -U postgres smartplus > backup.sql

# 또는 Docker 명령어 사용
docker exec smartplus-postgres pg_dump -U postgres smartplus > backup.sql
```

### 데이터베이스 복구

```bash
# PostgreSQL 데이터 복구
docker-compose exec -T postgres psql -U postgres smartplus < backup.sql

# 또는 Docker 명령어 사용
docker exec -i smartplus-postgres psql -U postgres smartplus < backup.sql
```

## 🐛 문제 해결

### 애플리케이션이 시작되지 않는 경우

1. 로그 확인: `docker-compose logs app`
2. 데이터베이스 연결 확인: `docker-compose exec postgres pg_isready`
3. 환경 변수 확인: `docker-compose config`

### 데이터베이스 연결 실패

1. PostgreSQL 컨테이너 상태 확인: `docker-compose ps`
2. 네트워크 확인: `docker network ls`
3. 컨테이너 재시작: `docker-compose restart postgres app`

### 포트 충돌

1. 사용 중인 포트 확인: `netstat -ano | findstr :8080` (Windows) 또는 `lsof -i :8080` (Linux/Mac)
2. `docker-compose.yml`에서 다른 포트로 변경

## 🔒 보안 권장사항

1. **환경 변수 관리**
   - `.env` 파일은 절대 Git에 커밋하지 마세요
   - 실제 운영 환경에서는 강력한 비밀번호 사용

2. **네트워크 보안**
   - 필요한 포트만 노출
   - 방화벽 설정으로 외부 접근 제한

3. **정기적인 업데이트**
   - Docker 이미지 정기적 업데이트
   - 보안 패치 적용

## 📦 운영 환경 배포

### Linux 서버에서 실행

```bash
# 프로젝트 클론
git clone <repository-url>
cd smartPlus

# 환경 변수 설정
cp .env.example .env
nano .env

# Docker Compose 실행
docker-compose up -d

# 상태 확인
docker-compose ps
```

### 자동 재시작 설정

모든 서비스는 `restart: unless-stopped` 정책으로 설정되어 있어, 서버 재부팅 시 자동으로 시작됩니다.

## 📊 모니터링

### 리소스 사용량 확인

```bash
# 컨테이너 리소스 사용량
docker stats

# 특정 컨테이너만 확인
docker stats smartplus-app smartplus-postgres
```

### 헬스체크 확인

```bash
# 컨테이너 상태 확인
docker-compose ps

# PostgreSQL 헬스체크
docker-compose exec postgres pg_isready -U postgres
```

## 🔄 업데이트 절차

1. 코드 업데이트 (Git pull 등)
2. 이미지 재빌드: `docker-compose build`
3. 서비스 재시작: `docker-compose up -d`
4. 로그 확인: `docker-compose logs -f app`
