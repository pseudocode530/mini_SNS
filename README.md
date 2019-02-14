# mini_sns
## Introduction
mini sns 어플리케이션 제작  


## Environment / Requirement
AndroidStudio  
Windows10  
Firebase


## Goal
파이어베이스를 이용하여 실시간 DB를 구축하고 게시판 기능 구현  


## Project composition
#### 주요 클래스
CommentDTO.java : 댓글을 firebase에 저장하기 위한 Data Transfer Object  
UserDTO.java : 유저정보를 기존 파이어베이스인증과 분리하여 따로 데이터베이스에 저장하기 위한 Data Transfer Object  
WriteDTO.java : 게시글을 파이어베이스에 저장하기 위한 Data Transfer Object  
LoginActivity.java : firebase의 auth를 이용하여 구글로그인 / 일반이메일 로그인 기능 구현  
Main2Activity.java : 게시글들이 보이는 액티비티. JSON형태의 데이터 변화를 감지하는 리스너 포함. 리사이클러 뷰의 각 아이템을 클릭시 ReadActivity로 데이터 전달  
ReadActivity.java : 게시글의 내용과 댓글을 읽어오는 액티비티. 추가로 댓글을 등록할 경우 파이어베이스에 데이터를 전송  
WritePostACtivity.java : 게시글을 작성하는 액티비티  


