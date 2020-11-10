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
  static Task[] list = new Task[TLENGTH];
  static int size = 0;

  public static void add() {
    System.out.println("[작업 등록]");

    Task task = new Task();
    task.no = Prompt.inputInt("번호? ");
    task.content = Prompt.inputString("내용? ");
    task.deadline = Prompt.inputDate("마감일? ");
    task.status = Prompt.inputInt("상태?\n0: 신규\n1: 진행중\n2: 완료\n> ");
    task.owner = Prompt.inputString("담당자? ");

    list[size++] = task;
  }

  public static void list() {
    System.out.println("[작업 목록]");

    for (int i = 0; i < size; i++) {
      Task task = list[i];
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
