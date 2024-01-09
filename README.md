![Brochure_수정1](https://github.com/engelhyunji/final/assets/147478174/f94f141f-bf5a-4bc4-b06d-3718dc36e494)


# 🐶 와르와르 BE Github
# 🐶 와르와르 [서비스 링크](https://final-pi-five.vercel.app/)
반려견과 견주들을 위한 서비스와 정보를 제공하는 라이프스타일 기반 커뮤니티

## 프로젝트 노션 페이지
- https://hh99-final-team3.notion.site/S-A-8a4790eaa6c942faae34d58b5424c4b2?pvs=4
## 프로젝트 Github
- Back-end  https://github.com/engelhyunji/final
- Front-end  https://github.com/team3pet/Back3

## 팀 와르와르
| 역할 | 이름 | 개인 Git 주소 |
|    :---:     | :---           | :---          |
|![image](https://github.com/engelhyunji/final/assets/147478174/71bf1b7c-e1c0-4208-b6d2-548f0083dd37)| 류시영     | https://github.com/seeyoungryu |
| ![image](https://github.com/engelhyunji/final/assets/147478174/71bf1b7c-e1c0-4208-b6d2-548f0083dd37)     | 문민희       | https://github.com/mooooonmin    |
| ![image](https://github.com/engelhyunji/final/assets/147478174/71bf1b7c-e1c0-4208-b6d2-548f0083dd37)     | 전아영       |https://github.com/ayoung-jeon   |
| ![image](https://github.com/engelhyunji/final/assets/147478174/71bf1b7c-e1c0-4208-b6d2-548f0083dd37)     | 박나원( L ) | https://github.com/Hewllpark      |
| ![image](https://github.com/engelhyunji/final/assets/147478174/0cf31df6-9185-4ec1-bc65-78e2129b29e4)| 강현지       | https://github.com/engelhyunji |
| ![image](https://github.com/engelhyunji/final/assets/147478174/0cf31df6-9185-4ec1-bc65-78e2129b29e4)| 하다연( LV )| https://github.com/hdayeon |
| ![image](https://github.com/engelhyunji/final/assets/147478174/96b6cc7c-0b61-4b41-a424-9a37d837c4ce)| 권하영       | hykwon121@naver.com (E-mail)|

# 🐾 팀 와르와르 담당
<details>
<summary>담당</summary> 

| 역할 | 이름 | 분담                                                                                                                                                                                                                                                                                                                                                                                      |
| --- | --- |-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Spring | 류시영 | ‣ 반려동물<br>   - 반려동물 등록, 조회, 수정, 삭제<br>  ‣ 반려동물 이미지<br> - 등록, 수정 삭제<br>‣ 테스트서버 (http)<br>   - AWS EC2, RDS(MySQL), S3<br>‣ 스케쥴링 서비스 로직                                                                                                                                                                                                                                                   |
| Spring | 문민희 | ‣ 유저<br>   - 회원가입, 로그인(Access/Refresh), 로그아웃<br>‣ 가게<br>   - 가게 등록, 조회, 수정, 삭제<br>‣ 마이페이지<br>   - 내가 등록한 가게, 반려동물, 채팅방 조회<br>   - 내 채팅방의 해시태그 추가, 삭제<br>‣ 채팅<br>   - 채팅방 목록 및 해시태그 목록 조회<br>   - 인기 해시태그 조회<br>   - WebSocket, Stomp 기반 실시간 채팅<br>‣ 테스트서버 (http, https)<br>   - AWS EC2, RDS(MySQL), S3, CodeDeploy<br>‣ Redis<br>   - Refresh Token + JTI 저장<br>   - 채팅방 관련 데이터 저장<br>   - Pub/Sub<br>   - 중복 접속 방지를 위한 JTI 저장<br>‣ Swagger |
| Spring | 전아영 | ‣ 회원가입<br>   - 이메일 인증<br>‣ 이미지<br> - 이미지 관련 등록, 수정, 삭제<br>‣ 가게<br>   - 가게 등록, 조회, 수정, 삭제<br>   - 키워드 검색 (Query DSL)<br>‣ 리뷰<br>    - 리뷰 등록, 조회, 삭제<br>‣ 좋아요<br>   - 등록, 조회, 삭제                                                                                                                                                                                                          |
| Spring✨ | 박나원(L) | ‣ 배포<br>  - Github Actions + Codedeploy, Nginx 서버관리<br>‣ 서버<br>  - 가게, 펫 이미지 관리 (AWS S3)<br>  - 데이터베이스 관리 (AWS RDS - MySQL)<br>  - FE/BE 서버 인프라 조성 (HTTPS - AWS Route53, ACM, EC2)<br>‣ 유저<br>   - 회원탈퇴 (hard delete)<br>                                                                                                                                                               |
| React | 강현지 | ‣ 반려동물 페이지<br>   - 반려동물 등록, 수정, 삭제<br>‣ 지도 페이지<br>   - 카카오 api<br>   - 가게 주소 등록 및 검색<br>   - 가게 목록 조회<br>   - 위치 마커 표시<br>‣ 메인 페이지<br>   - 가게 조회 상세페이지 이동<br>   - 반려동물 조회 상세페이지 이동<br>   - 케러셀                                                                                                                                                                                          |
| React ✨ | 하다연(VL) | ‣ 유저관련<br>   - 회원가입, 로그인(Access/Refresh), 로그아웃, 회원탈퇴<br>   - 전역 상태로 저장해서 로그인 상태 확인<br>‣ 가게 페이지<br>   - 등록, 수정<br>   - 카테고리 별 목록 조회<br>   - 검색<br>‣ 마이페이지<br>   - 내가 등록한 가게, 반려동물, 채팅방 조회, 수정, 삭제<br>   - 내 채팅방의 해시태그 추가, 삭제<br>‣ 채팅 페이지<br>   - 채팅방 목록 및 해시태그 목록 조회<br>   - 웹소켓 기반 실시간 채팅                                                                                               |
| Design | 권하영 | ‣ 디자인 총괄 + 메인페이지 반응형 작업                                                                                                                                                                                                                                                                                                                                                                 |

</details>


# 🐾 아키텍처

![image](https://github.com/engelhyunji/final/assets/147478174/834e3c8d-bc6b-4327-9e90-d8eec125c9b0)

# 🐾 User Flow
<details>
  <summary>User Flow</summary> 
  
![image](https://github.com/team3pet/Back3/assets/147483798/56994965-6126-4a52-ab88-d52fdbe55848)
</details>


# 🐾 ERD
<details>
  <summary>ERD</summary> 
  
![image](https://github.com/team3pet/Back3/assets/147483798/980ba0cb-69bc-4c79-bb79-34218d160dc7)
</details>

# 🐾 주요기능
## 가게 및 반려동물 등록
<details>
  <summary>가게 및 반려동물 등록</summary> 
- 로그인한 사용자에 한하여 나의 가게 또는 반려동물을 이미지와 함께 **등록**할 수 있습니다.<br>
- 등록한 가게 또는 반려동물은 각 목록페이지에 게시되어 공유됩니다.<br>
  
![image](https://github.com/engelhyunji/final/assets/147478174/d89ad0b9-f305-4471-94a3-55679c0ed34f)
</details>

## 가게 리뷰 작성 및 리뷰 추천
<details>
  <summary>가게 리뷰 작성 및 리뷰 추천</summary>
- 가게에 리뷰를 등록하여 사용자들끼리 정보를 공유할 수 있습니다.<br>
- 가게에 달린 리뷰 중 도움이 된 리뷰에 추천을 할 수 있습니다.<br>
  
![image](https://github.com/engelhyunji/final/assets/147478174/c12938c8-262a-41f6-b4b3-7291edf40ae0)
</details>

## 가게 검색
<details>
  <summary>가게 검색</summary>
- 가게 이름, 위치, 소개 정보를 대상으로 키워드를 검색하여 해당하는 결과를 찾아볼 수 있습니다.<br>
  
![image](https://github.com/engelhyunji/final/assets/147478174/60f949ac-beb2-46f7-8555-82f683df2232)
</details>

## 지도
<details>
  <summary>지도</summary>
- 가게의 위치정보를 바탕으로 카카오api 지도 상에서 가게 검색, 목록 조회가 가능합니다.<br>
  
![image](https://github.com/engelhyunji/final/assets/147478174/e264b37f-708f-4e68-a09a-b0e7def24b64)
</details>

## 실시간 채팅
<details>
  <summary>실시간 채팅</summary>
- 채팅방 전체목록을 볼 수 있고 각 방의 최근 메세지와 등록된 해시태그가 보여집니다.<br>
- 인기 해시태그 7개가 나열되어있고 각 태그를 클릭하면 해당하는 채팅방 확인이 가능합니다.<br>
- 계정 당 두개의 새로운 채팅방을 생성할 수 있고 참여하고픈 채팅방에 입장할 수 있습니다.<br>
- 같은 채팅방에 입장한 유저와 실시간 채팅을 할 수 있습니다.<br>
- 채팅참여자와 각 참여자가 등록한 반려동물을 확인할 수 있습니다.<br>
  
![채팅 예시 이미지](https://github.com/engelhyunji/final/assets/147478174/9196ee1a-178c-482c-ae2b-3d55358197b8)
</details>



# 🐾  *기술스택 정리*

<details>
<summary>공통기술</summary>

| 기술 | 설명 |
| --- | --- |
| WebSocket | - 서버가 클라이언트에게 비동기 메세지를 보낼 때 가장 널리 사용하는 기술<br>- 양방향 메시지 전송 가능 |
| Stomp (StompJs) | - 메세지는 STOMP의 "destination" 헤더를 기반으로 @Controller 객체의 @MethodMapping 메서드로 라우팅 된다.<br>- STOMP의 "destination" 및 Message Type을 기반으로 메세지를 보호하기 위해 Spring Security를 함께 사용할 수 있다 → WebSocket으로 들어오는 요청을 먼저 검증하기 위해 사용 |

</details>

<details>
<summary>[BE]</summary>

  | 기술 | 이유 |
  | --- | --- |
  | Redis | - 소켓 서버 역할은 데이터 검증과 목적지로의 전달이었기에 데이터를 영구적으로 저장할 필요가 없었기에 입력, 삭제 속도가 빠른 인메모리 데이터베이스인 Redis를 사용함.<br>- Redis의 Pub/Sub 기능을 사용해 소켓간의 메시지를 클러스터링 하기 위해 선택함. |
  | Swagger | - API 문서 자동 생성 및 테스트 용이성을 향상시켜 프론트와 백엔드 팀 간의 협업을 강화하기 위함.<br>- API 엔드포인트의 명확한 문서화를 통해 개발 생산성과 서비스 품질을 향상시키기 위해 선택. |
  | Spring Security | - 사용자의 인가, 인증 보안을 위한 대표적인 라이브러리인 Security 사용 |
  | JWT (Access/Refresh) | - 세션정책에서 Stateless 방식을 선택하기 위한 사용<br>- 엑세스토큰의 짧은 유효시간과 탈취의 위험성을 낮추기 위해 리프레시토큰 사용<br>- 중복접속 방지를 위해 토큰 JTI 같이 발급하는 방식으로 사용 |
  | Query DSL | - 검색 가능한 각각의 엔드포인트를 생성하기보단 하나의 엔드포인트에서 조건값을 받아 핸들링이 가능해 유지보수에 좋다고 생각하여 사용<br>- 지금은 가게 이름, 위치, 소개에서 통합으로 검색하고 있으나 향후 더 세부적인 검색기능을 생각하기 때문에 사용 |
  | Nginx | 리버스 프록시를 사용하게 되어 내부 네트워크와 외부 네트워크 간의 중간자 역할을 하여 서버의 실제 IP 주소를 숨김으로서 외부 공격으로부터 서버를 보호하는 데 도움을 줌. 또, 컨텐츠를 압축하고 최적화하여 전송할 수 있어 데이터 전송량을 줄이고 페이지 로딩 속도를 개선하는 데 도움이 됨. |
  | GitHub Actions | 팀원들이 항상 쓰게 되는 깃허브에서 main 브랜치로 트리거를 함으로써 배포가 자동화되며 codedeploy, workflow 두 군데에서 모두 롤백도 용이하기 때문에 선택함. |
  | Amazon RDS | 데이터베이스를 설정하고 유지하는데에 있어서 다른 기능들과 같이 amazon 상에서 관리할 수 있다는 장점과 자동백업이 이루어진다는 장점으로 인해 선택함 |
  | Amazon S3 | codedeploy와도 긴밀히 연결이 가능하고, ec2 instance 등 amazon 내에서 관리가 용이하며 이미지와 같은 파일 업로드 시 용량 부담을 최소화 할 수 있어 선택함 |

</details>

<details>
<summary>[FE]</summary>

  | 기술 | 이유 |
  | --- | --- |
  | React-Query | API 데이터의 캐싱, 동기화 및 상태 관리를 효율적으로 처리하기 위해 선택함. |
  | Axios | HTTP 요청을 쉽게 하고, 더 다양한 설정 옵션을 제공하기 때문에 선택함. |
  | styled-components | 컴포넌트 단위로 스타일을 적용하고, 동적 스타일링이 가능하기 때문에 선택함. |
  | TypeScript | 코드의 안정성을 높이고, 개발 생산성을 향상시키기 위해 선택함. |
  | react-router-dom | A태그만 써도 페이지 이동이 가능하고 다른 라이브러리도 있지만, A태그는 화면을 새로고침한 다음에 페이지를 이동한다는 단점이 있음. 하지만 REACT ROUTER에서는 새로운 페이지를 로드하지 않고 하나의 페이지 안에서 필요한 데이터만 가져오는 형태를 가지기 때문에 불필요한 렌더링을 없애고 웹 어플리케이션 내에서 라우팅을 쉽게 관리하기 위해 선택함. |
  | Vercel | Github를 통해 자동 빌드 및 배포가 가능한 것에 더불어, 배포 최적화로 개발 프로세스가 간단해 선택함. |

</details>



# 🐾  [데모영상](https://youtu.be/nUkjb7pTXLo)


<details>
<summary> ✅유저피드백 및 개선사항</summary>

| 유저피드백 부분 | 개선사항 |
| --- | --- |
| 공통 | • 로고 깨짐 현상<br>• 전체적 디자인 변경 및 수정 |
| 회원가입, 로그인, 로그아웃, 회원탈퇴 | • 회원가입시 입력값의 유효성 검사, 인증방식 추가<br>• 이미 가입되어 있는 이메일로 재가입 가능<br>• 인증번호 재발송 alert 요청<br>• 인증번호 유효시간이 흘러 마이너스로 넘어가는 문제<br>• 닉네임 글자수 제한<br>• 중복로그인 방지 요청<br>• 회원탈퇴 오류 |
| 메인 | • 메인페이지 케러셀 및 펫 이미지 클릭 상세페이지 이동.<br>• 메인페이지 케러셀 에러 수정 |
| 가게 | • 가게 등록시 가게명, 설명 글자수 제한 + 직관적인 글자 수 표시 기능<br>• 필수 입력값 필드 유효성 검사 추가 및 사용자에게 알림<br>• 가게를 등록한 유저는 본인 가게에 리뷰 등록 제한<br>• 전화번호, 시간 필드값 나눠서 조합하는 방식으로 변경<br>• 가게 등록 시 종류 통일 위해 영어에서 한글로 변경<br>• 가게 전체 조회 시 리뷰수 추가<br>• 등록시 이미지 드래그 앤 드롭 업로드 |
| 반려동물 | • 에러 응답값 메세지도 출력되게 수정<br>• 등록시 글자수 제한, 공백처리 제한<br>• 등록시 이미지 드래그 앤 드롭 업로드<br>• 펫 등록 수정 입력필드 글자제한 및 여백 입력 안되게 설정 |
| 리뷰, 좋아요 | • 필수 입력값 필드 유효성 검사 추가 및 사용자에게 알림<br>• 리뷰 글자수 제한<br>• 본인이 작성한 리뷰에만 삭제버튼 보이도록 수정<br>• 한명의 회원이 한 가게에 리뷰 중복 등록 불가하게 수정<br>• 본인의 좋아요 상태 조회 (get 추가) - 좋아요 상태 유지 목적 |
| 채팅, 해시태그 | • 중복 접속 방지<br>• 채팅 공백 메세지 수정 요청<br>• 잦은 입장 알림 메세지 수정 요청<br>• '채팅방 나가기' 버튼을 누르지 않고 나가는 사용자에 대한 처리 요청<br>• 채팅방 삭제 오류<br>• 입장한 유저가 없으면 채팅방 자동 삭제 요청<br>• 채팅 메세지 글자수 제한이 없어서 채팅방 레이아웃을 넘어가는 오류<br>• 채팅방 입장시 입장한 유저 실시간 반영 오류<br>• 채팅방 해시태그 글자수 제한 요청<br>• 채팅방 해시태그 필수입력값 유효성 검사 |
| 마이페이지 | • 마이페이지에서 가게, 펫 등록 가능하도록 수정 요청 |


</details>


<details>
<summary> ☑️ 향후 개선이 필요한 사항</summary>

| 파트 | 향후 개선이 필요한 부분 |
| --- | --- |
| 회원가입, 로그인, 로그아웃, 회원탈퇴 | • 비밀번호 더블 체크<br>• 소셜로그인 추가 요청<br>• 아이디/비밀번호 찾기 기능 요청<br>• 회원 정보 수정 요청<br>• 인증번호 발송 딜레이 타임 |
| 메인 | • 미반영 된 디자인 요소 적용 |
| 가게 | • 해시태그 기능 추가<br>• 이름, 위치, 소개 등 세부조건 추가 검색 |
| 반려동물 | • 좋아요, 팔로우 기능<br>• 세부조건으로 정렬기능 추가 |
| 리뷰, 좋아요 | • 대댓글 기능 추가 요청 |
| 채팅, 해시태그 | • 채팅 메세지 시간 요청 |

</details>
