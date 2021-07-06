
![ezgif com-gif-maker (1)](https://user-images.githubusercontent.com/45132207/102478967-9d6cd600-40a1-11eb-8e72-37fd236d0c79.gif)
![foret](https://user-images.githubusercontent.com/45132207/102469885-a2785800-4096-11eb-8da7-56e48ef2f2be.png)


# Foret APP VER 1.0
온라인 스터디 모임 어플
- spring3
- oracle

## 개요 
- 외부 환경에 의한 오프라인 스터디를 온라인 스터디로 계획
- 태그 및 지역을 기반으로 온라인 그룹 참여 및 활동
- 내게 맞는 태그를 검색하고, 없으면 추가하여 유저 친화적 기능
- 활발한 활동을 위해 실시간 채팅 및 푸쉬알림으로 내게 맞는 정보 백드라운드에서 수신
- 다양한 사람과 소통을 위한 익명게시판

## 구현 기능
- 로그인 및 회원가입, 비밀번호 찾기
- 가입한 그룹 출력, 페이징 처리, 회원 역할에 따라 데이터 노출 및 상태 변경
- 실시간 채팅, 푸쉬 알림
- 유저의 로그인 상태 확인, 메세지 읽음 상태, 메세지 삭제 기능
- 내가 쓴글, 가입한 그룹, 답글, 채팅 등 나의 정보 업데이트 시 푸쉬 알림
- 게시판, 댓글, 대댓글, 선택 정렬, 좋아요 기능
- 사진 업로드 및 수정 삭제
- 검색시 자동완성 기능

## 설계 중점
- Github을 통한 원활한 코드 관리
- 실시간 데이터 처리
- 직관적이고 사용하기 쉬운 UI/UX
- 데이터 흐름에 따라 화면 구상
- 기기 토큰을 활용하여 자동 로그인
- 멤버, 그룹의 기본키 활용으로 데이터 무결성 유지

## 차후 업데이트 및 보완점
- 데이터 이동의 명확성
- Oracle Database와 Firebase의 Database의 일치화 작업 필요
- 서버와의 데이터 연동시 request, response 시간 개선 필요
- 스터디에 필요한 일정 기능 필요
- 추천 기능 강화 필요 (현재 많이 사용한 태그 기준으로 정렬)
- API를 활용한 SNS 로그인 기능 구현

## Development environment
### VER 1.0
| 종류 | 이름  |
|--|--|
| 개발 툴 | STS3, Android Studio, sql developer, firebase, postman |
| 버전 관리 | GitHub |
| 데이터베이스| Oracle |
| 사용 언어 | java, sql |

## 역할
> **[jake-jeon9](https://github.com/jake-jeon9)**
- 외부 서버환경 구축
- 실시간 채팅, 푸쉬 알림
> **[SANDY-9 (SANDY)](https://github.com/SANDY-9)**
- 기획 및 디자인, 게시판 기능
- 자유게시판 및 검색 기능
> **[mansang2 (10000)](https://github.com/mansang2)**
- 데이터 처리 및 어플 프로토타입 제작
- 로그인, 유저정보, 메인 페이지 등 데이터 타겟팅
> **[azqazq195 (moseoh)](https://github.com/azqazq195)**
- DataBase 테이블 정의
- Spring 데이터 처리 구축

## Project PPT, NOT
[Foret APP PPT.pdf](https://github.com/azqazq195/Project_Foret/files/6768818/Foret.APP.PPT.pdf)

[Foret_Notion](https://www.notion.so/Foret-App-9adac55de18a45969da109b107e096eb)
