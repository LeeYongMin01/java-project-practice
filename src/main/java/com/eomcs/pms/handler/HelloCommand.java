package com.eomcs.pms.handler;

public class HelloCommand implements Command {
  
  @Override
  public void execute() {
    System.out.println("안녕하세요!");
  }
}
