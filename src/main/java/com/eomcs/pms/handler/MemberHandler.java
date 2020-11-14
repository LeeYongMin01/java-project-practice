package com.eomcs.pms.handler;

import com.eomcs.pms.domain.Member;
import com.eomcs.util.Prompt;

public class MemberHandler {

  MemberList memberList = new MemberList();
  
  public void add() {
    System.out.println("[회원 등록]");

    Member member = new Member();
    member.setNo(Prompt.inputInt("번호? "));
    member.setName(Prompt.inputString("이름? "));
    member.setEmail(Prompt.inputString("이메일? "));
    member.setPassword(Prompt.inputString("암호? "));
    member.setPhoto(Prompt.inputString("사진? "));
    member.setTel(Prompt.inputString("전화? "));
    member.setRegisteredDate(new java.sql.Date(System.currentTimeMillis()));
    
    memberList.add(member);
    
  }

  public void list() {
    System.out.println("[회원 목록]");

    Member[] members = memberList.toArray();
    
    for (Member member : members) {
      System.out.printf("%d, %s, %s, %s, %s\n",
          member.getNo(),
          member.getName(),
          member.getEmail(),
          member.getTel(),
          member.getRegisteredDate());
    }
  }

  public Member findByName(String name) {
    Member[] members = memberList.toArray();
    
    for(Member member : members) {
      if(member.name.equals(name)) {
        return member;
      }
    }
    return null;
  }
}
