package com.eomcs.pms;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.Date;
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
  
  public static void main(String[] args) {
    
    loadBoards(); // 파일에서 게시글 읽어오기
    
    Map<String,Command> commandMap = new HashMap<>();

    commandMap.put("/board/add", new BoardAddCommand(boardList));
    commandMap.put("/board/list", new BoardListCommand(boardList));
    commandMap.put("/board/detail", new BoardDetailCommand(boardList));
    commandMap.put("/board/update", new BoardUpdateCommand(boardList));
    commandMap.put("/board/delete", new BoardDeleteCommand(boardList));

    List<Member> memberList = new LinkedList<>();
    MemberListCommand memberListCommand = new MemberListCommand(memberList);
    commandMap.put("/member/add", new MemberAddCommand(memberList));
    commandMap.put("/member/list", memberListCommand);
    commandMap.put("/member/detail", new MemberDetailCommand(memberList));
    commandMap.put("/member/update", new MemberUpdateCommand(memberList));
    commandMap.put("/member/delete", new MemberDeleteCommand(memberList));

    List<Project> projectList = new LinkedList<>();
    commandMap.put("/project/add", new ProjectAddCommand(projectList, memberListCommand));
    commandMap.put("/project/list", new ProjectListCommand(projectList));
    commandMap.put("/project/detail", new ProjectDetailCommand(projectList));
    commandMap.put("/project/update", new ProjectUpdateCommand(projectList, memberListCommand));
    commandMap.put("/project/delete", new ProjectDeleteCommand(projectList));

    List<Task> taskList = new ArrayList<>();
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
    
    saveBoards(); // 데이터를 파일에 저장
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
    FileOutputStream out = null;
    // => 데이터 저장을 위해 데이터를 내보내야 하니까 출력스트림인 FileOutputStream 사용
    
  try {
    out = new FileOutputStream(boardFile);
    
    // 데이터의 개수 출력
    out.write(boardList.size() >> 24);
    out.write(boardList.size() >> 16);
    out.write(boardList.size() >> 8);
    out.write(boardList.size());
    
    for(Board board : boardList) { // 게시글 번호 출력
      out.write(board.getNo() >> 24);
      out.write(board.getNo() >> 16);
      out.write(board.getNo() >> 8);
      out.write(board.getNo());
      
      // 바이트 파일로 만들어서 변수에 담기
      
      byte[] bytes = board.getTitle().getBytes("UTF-8"); 
      out.write(bytes.length >> 8); // 2바이트
      out.write(bytes.length);
      out.write(bytes);
      
      bytes = board.getContent().getBytes("UTF-8");
      out.write(bytes.length >> 8);
      out.write(bytes.length);
      out.write(bytes);
      
      bytes = board.getWriter().getBytes("UTF-8");
      out.write(bytes.length >> 8);
      out.write(bytes.length);
      out.write(bytes);
      
      bytes = board.getRegisteredDate().toString().getBytes("UTF-8");
      out.write(bytes);
      
      out.write(board.getViewCount() >> 24);
      out.write(board.getViewCount() >> 16);
      out.write(board.getViewCount() >> 8);
      out.write(board.getViewCount());
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

    FileInputStream in = null;
    
    try { 
    in = new FileInputStream(boardFile);
    
    int size = in.read() << 24;
    size += in.read() << 16;
    size += in.read() << 8;
    size += in.read();
    
    for(int i = 0; i < size; i++) {
      Board board = new Board();
      
      int value = in.read() << 24;
      value += in.read() << 16;
      value += in.read() << 8;
      value += in.read();
      board.setNo(value);
      
      byte[] bytes = new byte[3000];
      
      int len = in.read() << 8 | in.read();
      in.read(bytes, 0, len);
      board.setContent(new String(bytes, 0, len, "UTF-8"));
      
      len = in.read() << 8 | in.read();
      in.read(bytes, 0, len);
      board.setContent(new String(bytes, 0, len, "UTF-8"));
      
      len = in.read() << 8 | in.read();
      in.read(bytes, 0, len);
      board.setWriter(new String(bytes, 0, len, "UTF-8"));
      
      in.read(bytes, 0, 10);
      board.setRegisteredDate(Date.valueOf(new String(bytes, 0, 10, "UTF-8")));
      
      value = in.read() << 24;
      value += in.read() << 16;
      value += in.read() << 8;
      value += in.read();
      board.setViewCount(value);
      
      boardList.add(board);
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
  
}
