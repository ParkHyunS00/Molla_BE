# 학사 졸업 프로젝트 MOLLA - BE
<div align="center">
<img src="https://github.com/user-attachments/assets/484880f3-ce24-4d90-8f25-855ad4e7f7e9" alt="initialpage" style="width: 20%;"/>
</div>

<div align="center">
Molla(Monitoring Of Life Logs and Affect)는 자연어 처리와 감정 모니터링을 활용한 AI 기반 상담 애플리케이션 입니다.<br>
이 프로젝트는 한국의 높은 우울증 문제에 대응하기 위해 개발되었으며, 심리상담에 대한 접근성을 높이는 것을 목표로 합니다.
</div>

<br />

## 📜 프로젝트 개요
- 프로젝트 명 : Molla(Monitoring Of Life Logs and Affect)
- 프로젝트 기간 : 2024.09.01 ~ 2024.11.4
- 참여 인원 : 2

| **박현수** | **황상범** |
|:----------:|:----------:|
| BE, Android  | ML, Android  |
| 상담, 게시글, 일기, 로그인, 회원가입, UI | 감정 분석 모델, 언어 생성 모델, 모델 서빙, UI |

<br />

## 📖 프로젝트 배경
한국의 연간 우울증 환자가 100만 명을 돌파하며 정신 건강 문제가 사회적 이슈로 떠오르고 있습니다. 이러한 문제에 대응하기 위해 본 프로젝트는 AI 기반 상담 보조 시스템을 개발하고자 합니다. 자연어 처리와 감정 분석 기술을 활용하여 사용자가 작성한 일기 데이터를 분석하고 개인 맞춤형 상담을 제공합니다.  
경량화된 LSTM 기반 감정 분석 모델을 통해 높은 정확도와 빠른 추론 속도를 달성하였으며 작성한 일기의 감정을 분석하며, 부정적인 감정이 빈번한 사용자에게 전문가의 도움을 권유합니다.  
또한 최근 거대 언어 모델의 발전으로 자연어 처리 기술이 지속적으로 향상되고 있으며 이를 활용하여 심리 치료를 돕고자 합니다.

<br />

## 🎯 프로젝트 기대 효과
- 상담 접근성 향상
- 1차적인 치료 보조 도구로써 전문가와의 상담 전 준비 역할
- 지속적인 감정 모니터링
- 비용 측면에서의 부담 완화

<br />

## 🚀 주요 기능
- 일기 목록
  - 일기 작성/삭제/수정
  - 작성한 일기 감정 분석
  - 감정 분석 결과와 조언 제공
- 커뮤니티 게시글 목록
  - 게시글 작성/삭제/수정
  - 댓글 작성/삭제/수정
  - 작성자의 최근 주된 감정분석 결과 표시
- 맞춤형 상담
  - AI 상담사와 채팅 기능
  - 분석된 감정에 기반하여 사용자에게 개인화된 상담 메시지 제공
- 우울증 위험도 평가
  - 일기의 감정 분석 결과 통계자료 제공
- 그 외 기능
  - 로그인/회원가입

<br />

## 🔗 시스템 흐름
<div align="center">
  <img width="716" alt="flow" src="https://github.com/user-attachments/assets/8d4251c4-b6d5-457a-9cc3-e3645d82d175" style="width: 50%;">
</div>

<br />

## 📊 ERD
<div align="center">
  <img width="526" alt="erd" src="https://github.com/user-attachments/assets/f5b141ea-537b-4208-8093-f39324672ab4" style="width: 50%;">
</div>

<br />

## 📱 구현 결과
- 일기
<div align="center">
  <img width="1596" alt="image" src="https://github.com/user-attachments/assets/4da71508-3882-4711-8e59-0999880d7373">
</div>

- 게시글
<div align="center">
<img width="1546" alt="image" src="https://github.com/user-attachments/assets/0a7d9ee8-c3f9-48c1-ab89-9fd4d366dc1c">
</div>

- 상담
<div align="center">
<img width="1364" alt="image" src="https://github.com/user-attachments/assets/0979400f-c54b-47e8-a3c4-3f0367ed6579">
</div>


<br />

## ⚙️ 개발 환경
- JDK 17
- Spring boot 3.3.2
- IntelliJ IDEA Ultimate
- Gradle 8.8

<br />

## 🧰 사용 기술
- Spring boot
- Spring Data JPA
- WebSocket
- MySQL
