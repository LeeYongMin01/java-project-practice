package com.eomcs.pms.handler;

import java.util.Arrays;
import com.eomcs.pms.domain.Board;

public class BoardList {

  static final int DEFAULT_CAPACITY = 3;
  Board[] list;
  int size = 0;
  
  public BoardList() {
    list = new Board[DEFAULT_CAPACITY];
  }
  
  public BoardList(int initialCapacity) {
    if(initialCapacity <= DEFAULT_CAPACITY) {
      list = new Board[DEFAULT_CAPACITY];
    } else {
      list = new Board[initialCapacity];
    }
  }
  
  public void add(Board board) {
    if(size == list.length) {
      
      int oldCapacity = list.length;
      int newCapacity = oldCapacity + (oldCapacity >> 1);
      
      list = Arrays.copyOf(list, newCapacity);
      System.out.printf("==> 새 배열을 %d개 생성하였음!\n", newCapacity);
    }
    list[size++] = board;
  }
  
  public Board[] toArray() {
    return Arrays.copyOf(list, size);
  }
}
