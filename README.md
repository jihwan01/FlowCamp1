# FlowCamp1

## A. 개발 팀원

- 권예진
- 박근영
- 오지환

## B. 개발 환경

- OS: Android (minSdk: 24, targetSdk: 33)
- Language: Java
- IDE: Android Studio
- Target Device: Galaxy S10

## C. 어플리케이션 소개

### TAB 3 - 2048 게임

#### Major features

- 상하좌우로 화면을 swipe하여 숫자 cell들을 밀고 합치며 점수를 높일 수 있습니다.
- 우측 상단에 위치한 Reset 버튼을 통해 게임을 초기화할 수 있습니다.

# ReadMe

### TAB 1 - 전화번호부

---

**실행 화면**
|![KakaoTalk_20230705_160732280](https://github.com/jihwan01/FlowCamp1/assets/61741090/ad29bbab-0eea-46cd-a239-1b84f19e0dfa)|![KakaoTalk_20230705_160732280_01](https://github.com/jihwan01/FlowCamp1/assets/61741090/3f8c62a9-5129-40ba-a349-e1f07d2b819f)|![KakaoTalk_20230705_160732280_02](https://github.com/jihwan01/FlowCamp1/assets/61741090/c5114b2d-9c3a-40f0-9b5f-d1d8e8eca255)|
| ------ | ------ | ------ |

왼쪽부터 차례로 메인 페이지, 연락처 추가 페이지, 세부 정보 페이지입니다.

**Major Features**

1. **연락처 추가**
   1. 우측 상단의 **‘+’** 버튼을 누르면 연락처 추가 페이지로 이동하게 됩니다.
   2. 이름, 전화번호를 누르면 수정 가능하게 창이 변경되며 값을 입력 후 **가상 키보드**의 **완료**를 눌러 값을 저장할 수 있습니다.
   3. 이후 우측 상단의 **ADD**을 누르면 연락처 추가가 완료됩니다.
2. **연락처 검색**
   1. 상단의 검색창을 눌러 검색을 진행할 수 있습니다.
3. **연락처 세부 정보 페이지**

   1. 세부 정보 페이지에서는 다음과 같은 기능들을 사용할 수 있습니다.
      1. **연락처 수정**
      2. **연락처 삭제**
      3. **전화 걸기**
      4. **문자 보내기**
      5. **영상통화 걸기**
   2. 연락처 우측의 “i” 버튼을 누르면 세부 정보 페이지로 들어가게 됩니다.

   **연락처 수정**

   - 세부 정보 페이지에서 이름, 전화 번호를 누르면 수정 가능하게 변경되며, 값을 입력한 후에 **가상 키보드**의 **완료**를 누르면 자동으로 값이 수정됩니다.

   **연락처 삭제**

   - 세부 정보 페이지에서 삭제 버튼을 누르면 값이 수정되며 기본 페이지로 이동됩니다.

   **전화 걸기**

   - 세부 정보 페이지에서 전화기 모양 버튼을 누르면 전화가 자동으로 걸립니다.

   **문자 보내기**

   - 세부 정보 페이지에서 문자 모양 버튼을 누르면 문자 보내기 창으로 이동됩니다.

   **영상통화 걸기**

   - 세부 정보 페이지에서 영상통화 버튼을 누르면 영상통화가 걸립니다.

**기술 설명**

- Recycler View를 이용하여, 연락처 데이터들을 표현합니다.
- 연락처 탭 코드가 다른 탭에 영향을 주는 것을 막기 위해 모든 Logic이 Fragment 안에 담기도록 구조를 디자인 하였습니다.
  - Fragment Container만을 가지고 있는 Fragment를 하나 만들어서 탭 전환 시에는 Container를 가지고 있는 Fragment만을 띄웁니다.
  - 실제 탭의 구현은 추가 Fragment들을 만들어 구현한 뒤, Container에 추가 Fragment를 넣어주는 형태로서 위의 목적을 달성하였습니다.
- 이름, 전화번호를 직접 눌러서 수정 가능하도록 만들기 위해서 TextView와 EditText를 겹쳐놓고 수정할 때와 아닐 때에 따라 번갈아가며 visible을 바꿔줌으로서 구현하였습니다.

### TAB 3 - 2048 게임
![Screenshot_20230705-164121_FlowCamp1](https://github.com/jihwan01/FlowCamp1/assets/98662998/f17538b0-6d72-42e4-a63b-2de190c3b840)
![Screenshot_20230705-164248_FlowCamp1](https://github.com/jihwan01/FlowCamp1/assets/98662998/cbc14304-fa5b-4c1e-80f9-68c56089fdbf)
![Screenshot_20230705-164318_FlowCamp1](https://github.com/jihwan01/FlowCamp1/assets/98662998/131525f5-3584-45dd-835e-52626290b778)

#### Major features

- 상하좌우로 스와이프하여 숫자 cell들을 밀고 합치며 점수를 높일 수 있습니다.
- 우측 상단에 위치한 Reset 버튼을 통해 게임을 초기화할 수 있습니다.

---

#### 기술 설명

1. 숫자 cell 생성
   - GridView에 16개의 TextView를 4x4 형태로 배치하여 게임판을 구현하였습니다.
   - 각 TextView가 나타낼 숫자 값은 cellValueMatrix라는 2차원 배열에 저장되어 있습니다.
   - cell 값에 따른 TextView의 배경색은 HashMap에 저장되어 있습니다.
2. swipe
   - GestureDetector의 onFling() 메소드에 터치 좌표를 입력 받아 swipe 방향을 인식하는 기능을 추가하였습니다.
   - swipe 방향에 따라 cellValueMatrix를 업데이트 하도록 구현하였습니다.
3. 기록 저장
   - 앱을 종료한 후 다시 실행해도 게임 정보를 유지할 수 있도록 SharedPreferences를 사용하여 게임 진행 상황, 현재 점수, 최고 점수 등 데이터를 파일로 저장하고 불러오도록 구현하였습니다.
