## 1. 요구사항 분석
- [마일스톤](milestone.md)
- [시퀀스 다이어그램](sequenceDiagram.md)
- [클래스 다이어그램](classDiagram.md)
- [ERD](erd.md)

---

## 시퀀스 다이어그램

* [1. 포인트 조회](#포인트-조회)
* [2. 포인트 충전](#포인트-충전)
* [3. 상품 조회](#상품-조회)
* [4. 주문 및 결제](#주문-및-결제)
* [5. 선착순 쿠폰 발급](#선착순-쿠폰-발급)
* [6. 인기 상품 스케줄링](#인기-상품-스케줄링)
* [7. 인기-상품-조회](#인기-상품-조회)

## 포인트 조회

```mermaid
sequenceDiagram
		actor A as 사용자
		participant S as 서버
		participant U as 유저
		participant P as 포인트
		
A->>S: 포인트 조회 요청 
activate S
S->>U: 유저 조회
activate U
U->>S: 유저 반환
deactivate U
opt 유저 없음
	S->>A: 조회 실패
end
S->>P: 포인트 조회
activate P
P->>S: 포인트 반환
deactivate P
S->>A: 포인트 반환
deactivate S
```

## 포인트 충전

```mermaid
sequenceDiagram
		actor A as 사용자
		participant S as 서버
		participant U as 유저
		participant P as 포인트

A->>S: 포인트 충전 요청 
activate S
S->>U: 유저 조회
activate U
U->>S: 유저 반환
deactivate U
opt 유저 없음
	S->>A: 충전 실패
end
S->>P: 포인트 충전
activate P
opt 최대 잔고 초과
	P->>S: 충전 실패
	S->>A: 충전 실패
end
P->>S: 충전 성공
deactivate P
S->>A: 충전 성공
deactivate S
```

## 상품 조회

```mermaid
sequenceDiagram
		actor A as 사용자
		participant S as 서버
		participant P as 상품

A->>S: 상품 조회
activate S
S->>P: 상품 및 재고 조회
activate P
P->>S: 상품 및 재고 반환
deactivate P
S->>A: 상품 조회 성공
deactivate S
```

## 주문 및 결제
```mermaid
sequenceDiagram
		actor A as 사용자
		participant O as 주문
		participant U as 유저
		participant P as 상품
		participant O as 주문
		participant PP as 결제
		participant C as 사용자쿠폰
		participant PO as 포인트
		participant PL as 외부플랫폼
		
A->>O: 주문 요청
activate O
O->>U: 유저 조회
activate U
U->>O: 유저 반환
opt 유저 없음
	U->>A: 주문 실패
	deactivate U
end
O->>P: 상품 및 재고 조회
activate P
P->>O: 상품 및 재고 반환
opt 상품 없음, 재고 없음
	P->>A: 주문 실패
end
O->>P: 재고 차감처리
deactivate P
activate O
O->>O: 주문 생성 (주문상품, 주문금액)
deactivate O
O->>PP: 결제 요청
activate PP
opt 쿠폰 있음
	PP->>C: 쿠폰 조회
	activate C
	C->>PP: 쿠폰 반환
	opt 쿠폰 유효성 검사
	C->>O: 결제 실패
	O->>A: 주문 실패
	end
	PP->>C: 쿠폰 사용처리
	deactivate C
end
activate PP
PP->>PP: 결제 생성 (결제 금액)
deactivate PP

PP->>PO: 포인트 조회
PO->>PP: 포인트 반환
PP->>PO: 포인트 사용
opt 포인트 부족
	PO->>PP: 포인트 사용 실패
	PP->>O: 결제 실패
	O->>A: 주문 실패
end
PO->>PP: 포인트 사용 성공
PP->>O: 결제 완료
deactivate PP
O-->>PL: 주문 정보 전송
O->>A: 주문 완료
deactivate O
		
```

## 선착순 쿠폰 발급

```mermaid
sequenceDiagram
		actor A as 사용자
		participant S as 서버
		participant U as 유저
		participant C as 쿠폰
		participant CC as 사용자쿠폰

A->>S: 선착순 쿠폰 발급 요청
activate S
S->>U: 유저 조회
activate U
U->>S: 유저 정보 반환
deactivate U
opt 유저 없음
	S->>A: 선착순 쿠폰 발급 실패
end
S->>C: 쿠폰 조회
activate C
C->>S: 쿠폰 정보 반환
deactivate C
opt 쿠폰 없음 / 발급 수량 소진
	S->>A: 선착순 쿠폰 발급 실패
end
S->>CC: 해당 쿠폰 보유 여부 확인
activate CC
CC->>S: 쿠폰 보유 여부 반환
deactivate CC
opt 쿠폰 있음
	S->>A: 선착순 쿠폰 발급 실패
end
	S->>CC: 쿠폰 발급
	activate CC
	CC->>S: 쿠폰 발급 성공
	deactivate CC
	S->>C: 쿠폰 개수 차감
	activate C
	C->>S: 차감 성공
	deactivate C
	S->>A: 선착순 쿠폰 발급 성공
deactivate S
```

## 인기 상품 스케줄링

```mermaid
sequenceDiagram
		participant S as 서버
		participant O as 주문
		participant PS as 주문상품서머리

loop 스케줄링
	opt 지정된 시간
		S->>+O: 전일 주문완료 데이터 조회
		activate S
		O->>-S: 전일 주문완료 데이터 반환
		S->>PS: 주문 상품 및 개수 데이터 집계 요청
		activate PS
		PS->>PS: 주문 상품 및 개수 데이터 집계
		PS->>S: 데이터 집계 완료
		deactivate PS
		S->>S: 집계된 데이터 저장
	deactivate S
	end
end

```

## 인기 상품 조회

```mermaid
sequenceDiagram
	actor A as 사용자
	participant S as 서버
	participant PS as 주문상품서머리
	
A->>S: 인기 상품 top5 조회 요청
activate S
S->>PS: 3일간 주문 상품 서머리 조회
activate PS
PS->>S: 3일간 주문 상품 서머리 반환
deactivate PS
S->>PS: top5 집계 요청
activate PS
PS->>S: top5 데이터 반환
deactivate PS
S->>A: 인기 상품 top5 조회 성공
deactivate S
```