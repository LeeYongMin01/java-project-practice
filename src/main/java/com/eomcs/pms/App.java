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
    TaskAddCommand taskAddCommand = new TaskAddCommand(taskList, memberListCommand);
    TaskListCommand taskListCommand = new TaskListCommand(taskList);
    TaskDetailCommand taskDetailCommand = new TaskDetailCommand(taskList);
    TaskUpdateCommand taskUpdateCommand = new TaskUpdateCommand(taskList, memberListCommand);
    TaskDeleteCommand taskDeleteCommand = new TaskDeleteCommand(taskList);
    
    HelloCommand helloCommand = new HelloCommand();
    
    Deque<String> commandStack = new ArrayDeque<>();

    Queue<String> commandQueue = new LinkedList<>();

    loop:
      while (true) {
        String command = Prompt.inputString("명령> ");

        commandStack.push(command);
        commandQueue.offer(command);

        switch (command) {
          case "/member/add": memberAddCommand.execute(); break;
          case "/member/list": memberListCommand.execute(); break;
          case "/member/detail": memberDetailCommand.execute(); break;
          case "/member/update": memberUpdateCommand.execute(); break;
          case "/member/delete": memberDeleteCommand.execute(); break;
          case "/project/add": projectAddCommand.execute(); break;
          case "/project/list": projectListCommand.execute(); break;
          case "/project/detail": projectDetailCommand.execute(); break;
          case "/project/update": projectUpdateCommand.execute(); break;
          case "/project/delete": projectDeleteCommand.execute(); break;
          case "/task/add": taskAddCommand.execute(); break;
          case "/task/list": taskListCommand.execute(); break;
          case "/task/detail": taskDetailCommand.execute(); break;
          case "/task/update": taskUpdateCommand.execute(); break;
          case "/task/delete": taskDeleteCommand.execute(); break;
          case "/board/add": boardAddCommand.execute(); break;
          case "/board/list": boardListCommand.execute(); break;
          case "/board/detail": boardDetailCommand.execute(); break;
          case "/board/update": boardUpdateCommand.execute(); break;
          case "/board/delete": boardDeleteCommand.execute(); break;
          case "/hello": helloCommand.execute(); break;
          
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
