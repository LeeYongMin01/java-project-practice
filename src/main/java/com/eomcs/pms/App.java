package com.eomcs.pms;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import com.eomcs.pms.domain.Board;
import com.eomcs.pms.domain.Member;
import com.eomcs.pms.domain.Project;
import com.eomcs.pms.domain.Task;
import com.eomcs.pms.handler.BoardAddCommand;
import com.eomcs.pms.handler.BoardDeleteCommand;
import com.eomcs.pms.handler.BoardDetailCommand;
import com.eomcs.pms.handler.BoardListCommand;
import com.eomcs.pms.handler.BoardUpdateCommand;
import com.eomcs.pms.handler.MemberAddCommand;
import com.eomcs.pms.handler.MemberDeleteCommand;
import com.eomcs.pms.handler.MemberDetailCommand;
import com.eomcs.pms.handler.MemberListCommand;
import com.eomcs.pms.handler.MemberUpdateCommand;
import com.eomcs.pms.handler.ProjectAddCommand;
import com.eomcs.pms.handler.ProjectDeleteCommand;
import com.eomcs.pms.handler.ProjectDetailCommand;
import com.eomcs.pms.handler.ProjectListCommand;
import com.eomcs.pms.handler.ProjectUpdateCommand;
import com.eomcs.pms.handler.TaskHandler;
import com.eomcs.util.Prompt;

public class App {

  public static void main(String[] args) {

    List<Board> boardList = new ArrayList<>();
    BoardAddCommand boardAddCommand = new BoardAddCommand(boardList);
    BoardListCommand boardListCommand = new BoardListCommand(boardList);
    BoardDetailCommand boardDetailCommand = new BoardDetailCommand(boardList);
    BoardUpdateCommand boardUpdateCommand = new BoardUpdateCommand(boardList);
    BoardDeleteCommand boardDeleteCommand = new BoardDeleteCommand(boardList);

    List<Member> memberList = new ArrayList<>();
    MemberAddCommand memberAddCommand = new MemberAddCommand(memberList);
    MemberListCommand memberListCommand = new MemberListCommand(memberList);
    MemberDetailCommand memberDetailCommand = new MemberDetailCommand(memberList);
    MemberUpdateCommand memberUpdateCommand = new MemberUpdateCommand(memberList);
    MemberDeleteCommand memberDeleteCommand = new MemberDeleteCommand(memberList);

    List<Project> projectList = new ArrayList<>();
    ProjectAddCommand projectAddCommand = new ProjectAddCommand(projectList, memberListCommand);
    ProjectListCommand projectListCommand = new ProjectListCommand(projectList);
    ProjectDetailCommand projectDetailCommand = new ProjectDetailCommand(projectList);
    ProjectUpdateCommand projectUpdateCommand = new ProjectUpdateCommand(projectList, memberListCommand);
    ProjectDeleteCommand projectDeleteCommand = new ProjectDeleteCommand(projectList);

    List<Task> taskList = new ArrayList<>();
    TaskHandler taskHandler = new TaskHandler(taskList, memberHandler);

    Deque<String> commandStack = new ArrayDeque<>();

    Queue<String> commandQueue = new LinkedList<>();

    loop:
      while (true) {
        String command = Prompt.inputString("명령> ");

        commandStack.push(command);
        commandQueue.offer(command);

        switch (command) {
          case "/member/add": memberHandler.add(); break;
          case "/member/list": memberHandler.list(); break;
          case "/member/detail": memberHandler.detail(); break;
          case "/member/update": memberHandler.update(); break;
          case "/member/delete": memberHandler.delete(); break;
          case "/project/add": projectHandler.add(); break;
          case "/project/list": projectHandler.list(); break;
          case "/project/detail": projectHandler.detail(); break;
          case "/project/update": projectHandler.update(); break;
          case "/project/delete": projectHandler.delete(); break;
          case "/task/add": taskHandler.add(); break;
          case "/task/list": taskHandler.list(); break;
          case "/task/detail": taskHandler.detail(); break;
          case "/task/update": taskHandler.update(); break;
          case "/task/delete": taskHandler.delete(); break;
          case "/board/add": boardHandler.add(); break;
          case "/board/list": boardHandler.list(); break;
          case "/board/detail": boardHandler.detail(); break;
          case "/board/update": boardHandler.update(); break;
          case "/board/delete": boardHandler.delete(); break;

          case "history": printCommandHistory(commandStack.iterator()); break;
          case "history2": printCommandHistory(commandQueue.iterator()); break;

          case "quit":
          case "exit":
            System.out.println("안녕!");
            break loop;
          default:
            System.out.println("실행할 수 없는 명령입니다.");
        }
        System.out.println();
      }
    Prompt.close();
  }

  static void printCommandHistory(Iterator<String> iterator) {
    try {
      int count = 0;
      while(iterator.hasNext()) {
        System.out.println(iterator.next());
        count++;

        if((count % 5) == 0 && Prompt.inputString(":").equalsIgnoreCase("q")) {
          break;
        }
      }
    } catch(Exception e) {
      System.out.println("history 명령 처리 중 오류 발생!");
    }
  }
}
