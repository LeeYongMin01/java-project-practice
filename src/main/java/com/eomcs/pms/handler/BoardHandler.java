package com.eomcs.pms.handler;

import java.sql.Date;
import com.eomcs.util.Prompt;

public class BoardHandler {

  static class Board {
    int no;
    String title;
    String content;
    String writer;
    Date registeredDate;
    int viewCount;
  }
  
  static final int LENGTH = 100;
  Board[] list = new Board[LENGTH];
  int size = 0;
  
  public void add() {
    System.out.println("[게시물 등록]");
    
    Board board = new Board();
    board.no = Prompt.inputInt("번호? ");
    board.title = Prompt.inputString("제목? ");
    board.content = Prompt.inputString("내용? ");
    board.writer = Prompt.inputString("작성자? ");
    board.registeredDate = new Date(System.currentTimeMillis());
    board.viewCount = 0;
    this.list[this.size++] = board;
    
    System.out.println("게시글을 등록하였습니다.");
  }
  
  public void list() {
    System.out.println("[게시글 목록]");

    for(int i = 0; i < this.size; i++) {
      Board board = this.list[i];
      System.out.printf("%d, %s, %s, %s, %d\n",
          board.no,
          board.title,
          board.writer,
          board.registeredDate,
          board.viewCount);
    }
  }
}
