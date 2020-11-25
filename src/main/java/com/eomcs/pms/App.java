package com.eomcs.pms;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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
import com.eomcs.pms.handler.Command;
import com.eomcs.pms.handler.HelloCommand;
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
import com.eomcs.pms.handler.TaskAddCommand;
import com.eomcs.pms.handler.TaskDeleteCommand;
import com.eomcs.pms.handler.TaskDetailCommand;
import com.eomcs.pms.handler.TaskListCommand;
import com.eomcs.pms.handler.TaskUpdateCommand;
import com.eomcs.util.Prompt;

public class App {

  static List<Board> boardList = new ArrayList<>();
  static File boardFile = new File("./board.data"); // 게시글 저장 데이터를 받을 파일 준비

  static List<Member> memberList = new ArrayList<>();
  static File memberFile = new File("./member.data");

  static List<Project> projectList = new LinkedList<>();
  static File projectFile = new File("./project.data");

  static List<Task> taskList = new ArrayList<>();
  static File taskFile = new File("./task.data");

  public static void main(String[] args) {

    loadBoards();
    loadMembers();
    loadProjects();
    loadTasks();

    Map<String,Command> commandMap = new HashMap<>();

    commandMap.put("/board/add", new BoardAddCommand(boardList));
    commandMap.put("/board/list", new BoardListCommand(boardList));
    commandMap.put("/board/detail", new BoardDetailCommand(boardList));
    commandMap.put("/board/update", new BoardUpdateCommand(boardList));
    commandMap.put("/board/delete", new BoardDeleteCommand(boardList));

    MemberListCommand memberListCommand = new MemberListCommand(memberList);
    commandMap.put("/member/add", new MemberAddCommand(memberList));
    commandMap.put("/member/list", memberListCommand);
    commandMap.put("/member/detail", new MemberDetailCommand(memberList));
    commandMap.put("/member/update", new MemberUpdateCommand(memberList));
    commandMap.put("/member/delete", new MemberDeleteCommand(memberList));

    commandMap.put("/project/add", new ProjectAddCommand(projectList, memberListCommand));
    commandMap.put("/project/list", new ProjectListCommand(projectList));
    commandMap.put("/project/detail", new ProjectDetailCommand(projectList));
    commandMap.put("/project/update", new ProjectUpdateCommand(projectList, memberListCommand));
    commandMap.put("/project/delete", new ProjectDeleteCommand(projectList));

    commandMap.put("/task/add", new TaskAddCommand(taskList, memberListCommand));
    commandMap.put("/task/list", new TaskListCommand(taskList));
    commandMap.put("/task/detail", new TaskDetailCommand(taskList));
    commandMap.put("/task/update", new TaskUpdateCommand(taskList, memberListCommand));
    commandMap.put("/task/delete", new TaskDeleteCommand(taskList));

    commandMap.put("/hello", new HelloCommand());

    Deque<String> commandStack = new ArrayDeque<>();

    Queue<String> commandQueue = new LinkedList<>();

    loop:
      while (true) {
        String inputStr = Prompt.inputString("명령> ");

        if(inputStr.length() == 0) {
          continue;
        }

        commandStack.push(inputStr);
        commandQueue.offer(inputStr);

        switch (inputStr) {
          case "history": printCommandHistory(commandStack.iterator()); break;
          case "history2": printCommandHistory(commandQueue.iterator()); break;
          case "quit":
          case "exit":
            System.out.println("안녕!");
            break loop;
          default:
           Command command = commandMap.get(inputStr);
            if(command != null) {
              try{
              command.execute();
             } catch(Exception e) {
               System.out.println("-----------------------------");
               System.out.printf("명령어 실행 중 오류 발생: %s\n", e);
               System.out.println("-----------------------------");
             }
            } else {
            System.out.println("실행할 수 없는 명령입니다.");
           }
        }
        System.out.println();
      }
    Prompt.close();

    saveBoards();
    saveMembers();
    saveProjects();
    saveTasks();

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

  private static void saveBoards() {
    ObjectOutputStream out = null;
    // => 데이터 저장을 위해 데이터를 내보내야 하니까 출력스트림인 FileOutputStream 사용

  try {
    out = new ObjectOutputStream(
        new BufferedOutputStream(
            new FileOutputStream(boardFile)));

    // 데이터의 개수 출력
    out.writeInt(boardList.size());

    for(Board board : boardList) {
      out.writeObject(board);
    }

    System.out.printf("총 %d개의 게시글 데이터를 저장했습니다.\n", boardList.size());
  } catch(Exception e) {
    System.out.println("게시글 데이터의 파일 저장 중 오류 발생! - " + e.getMessage());

  } finally {
    try {
    out.close();
  } catch(Exception e) {

      }
    }
  }

  private static void loadBoards() {

    ObjectInputStream in = null;

    try {
    in = new ObjectInputStream(
        new BufferedInputStream(
            new FileInputStream(boardFile)));

    int size = in.readInt();

    for(int i = 0; i < size; i++) {
      boardList.add((Board)in.readObject());

    }
    System.out.printf("총 %d 개의 게시글 데이터를 로딩했습니다.\n", boardList.size());
    } catch(Exception e) {
      System.out.println("게시글 파일 읽기 중 오류 발생! - " + e.getMessage());

    } finally {
      try {
        in.close();
      } catch(Exception e) {

      }
    }
  }

  private static void saveMembers() {
    ObjectOutputStream out = null;

    try {
      out = new ObjectOutputStream(
          new BufferedOutputStream(
              new FileOutputStream(memberFile)));

      out.writeInt(memberList.size());

      for (Member member : memberList) {
        out.writeObject(member);

      }
      System.out.printf("총 %d 개의 회원 데이터를 저장했습니다.\n", memberList.size());

    } catch (IOException e) {
      System.out.println("회원 데이터의 파일 쓰기 중 오류 발생! - " + e.getMessage());

    } finally {
      try {
        out.close();
      } catch (IOException e) {
      }
    }
  }

  private static void loadMembers() {
    ObjectInputStream in = null;

    try {
      in = new ObjectInputStream(
          new BufferedInputStream(
              new FileInputStream(memberFile)));

      int size = in.readInt();

      for (int i = 0; i < size; i++) {
        memberList.add((Member)in.readObject());
      }
      System.out.printf("총 %d 개의 회원 데이터를 로딩했습니다.\n", memberList.size());

    } catch (Exception e) {
      System.out.println("회원 파일 읽기 중 오류 발생! - " + e.getMessage());
    } finally {
      try {
        in.close();
      } catch (Exception e) {
      }
    }
  }

  private static void saveProjects() {
    ObjectOutputStream out = null;

    try {
      out = new ObjectOutputStream(
          new BufferedOutputStream(
          new FileOutputStream(projectFile)));

      out.writeInt(projectList.size());

      for (Project project : projectList) {
        out.writeObject(project);
    }
      System.out.printf("총 %d 개의 프로젝트 데이터를 저장했습니다.\n", projectList.size());

    } catch (IOException e) {
      System.out.println("프로젝트 데이터의 파일 쓰기 중 오류 발생! - " + e.getMessage());

    } finally {
      try {
        out.close();
      } catch (IOException e) {
      }
    }
  }

  private static void loadProjects() {
    ObjectInputStream in = null;

    try {
      in = new ObjectInputStream(
          new BufferedInputStream(
              new FileInputStream(projectFile)));

      int size = in.readInt();

      for (int i = 0; i < size; i++) {
        projectList.add((Project)in.readObject());
      }
      System.out.printf("총 %d 개의 프로젝트 데이터를 로딩했습니다.\n", projectList.size());

    } catch (Exception e) {
      System.out.println("프로젝트 파일 읽기 중 오류 발생! - " + e.getMessage());
    } finally {
      try {
        in.close();
      } catch (Exception e) {
      }
    }
  }

  private static void saveTasks() {
    ObjectOutputStream out = null;

    try {
      out = new ObjectOutputStream(
          new BufferedOutputStream(
              new FileOutputStream(taskFile)));

      out.writeInt(taskList.size());
      for (Task task : taskList) {
        out.writeObject(task);
    }
      System.out.printf("총 %d 개의 작업 데이터를 저장했습니다.\n", taskList.size());

    } catch (IOException e) {
      System.out.println("작업 데이터의 파일 쓰기 중 오류 발생! - " + e.getMessage());

    } finally {
      try {
        out.close();
      } catch (IOException e) {
      }
    }
  }

  private static void loadTasks() {
    ObjectInputStream in = null;

    try {
      in = new ObjectInputStream(
          new BufferedInputStream(
              new FileInputStream(taskFile)));

      int size = in.readInt();

      for (int i = 0; i < size; i++) {
        taskList.add((Task)in.readObject());

      }
      System.out.printf("총 %d 개의 작업 데이터를 로딩했습니다.\n", taskList.size());

    } catch (Exception e) {
      System.out.println("작업 파일 읽기 중 오류 발생! - " + e.getMessage());
    } finally {
      try {
        in.close();
      } catch (Exception e) {
      }
    }
  }
}
