package com.eomcs.pms.handler;

import java.sql.Date;
import com.eomcs.util.Prompt;

public class TaskHandler {

  // 작업 데이터
  static class Task {

   int no;
   String content;
   Date deadline;
   String owner;
   int status;
  }

  static final int TLENGTH = 100;
  Task[] list = new Task[TLENGTH];
  int size = 0;

  public MemberHandler memberHandler;
  
  public void add() {
    System.out.println("[작업 등록]");

    Task task = new Task();
    task.no = Prompt.inputInt("번호? ");
    task.content = Prompt.inputString("내용? ");
    task.deadline = Prompt.inputDate("마감일? ");
    task.status = Prompt.inputInt("상태?\n0: 신규\n1: 진행중\n2: 완료\n> ");
    
    while(true) {
      String name = Prompt.inputString("담당자?(취소: 빈 문자열) ");
      
      if(name.length() == 0) {
        System.out.println("작업 등록을 취소합니다");
        return;
      } else if (memberHandler.findByName(name) != null) {
        task.owner = name;
        break;
      }
      System.out.println("등록된 회원이 아닙니다.");
    }

    this.list[this.size++] = task;
  }

  public void list() {
    System.out.println("[작업 목록]");

    for (int i = 0; i < this.size; i++) {
      Task task = this.list[i];
      String stateLabel = null;
      switch (task.status) {
        case 1:
          stateLabel = "진행중";
          break;
        case 2:
          stateLabel = "완료";
          break;
        default:
          stateLabel = "신규";
      }
      System.out.printf("%d, %s, %s, %s, %s\n",
          task.no,
          task.content,
          task.deadline,
          stateLabel,
          task.owner);
    }
  }
}
