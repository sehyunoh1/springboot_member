package com.its.member;

import com.its.member.dto.MemberDTO;
import com.its.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

// Assertions에 class가 static으로 정의되었기 때문에 static 코드 사용
import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.*;
@SpringBootTest
public class MemberTest {
    @Autowired
   private MemberService memberService;
    // 회원가입 테스트
    // 신규회원 데이터 생성(DTO)
    // 회원가입 기능 실행
    // 가입 성공 후 가져온 id 값으로 DB 조회를 하고
    // 가입시 사용한 email과 조회한 email이 일치하는지를 판단
    @Test
    @Transactional
//    DB상 rollback을 의미함.
    @Rollback(value = true)
    public void memberSaveTest(){
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setMemberEmail("testEmail2");
        memberDTO.setMemberPassword("testPassword");
        memberDTO.setMemberName("testName");
        memberDTO.setMemberAge(22);
        memberDTO.setMemberPhone("010-1111-1111");

       Long saveId = memberService.save(memberDTO);
      MemberDTO savedMember =  memberService.findById(saveId);

//     assertThat: test코드 상에서 비교하는(일치하는지) 코드
      assertThat(memberDTO.getMemberEmail()).isEqualTo(savedMember.getMemberEmail());

    }


    //로그인 테스트
    @Test
    @Transactional
    @Rollback(value = true)
    @DisplayName("로그인 테스트")
    public void loginTest(){
        // 1. 회원가입
        // 2. 로그인 수행
        // 3. 로그인 결과가 null 이 아니면 테스트 통과

        // 1.
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setMemberEmail("testEmail3");
        memberDTO.setMemberPassword("1234");
        memberDTO.setMemberName("이름");
        memberDTO.setMemberAge(30);
        memberDTO.setMemberPhone("010-0000-0000");
         memberService.save(memberDTO);

        // 1-1 확인용 DTO 객체 추가
        MemberDTO DTO = new MemberDTO();
        DTO.setMemberEmail("admin");
        DTO.setMemberPassword("1111");
        //2.
        MemberDTO loginResult = memberService.login(DTO);
        //3.
        assertThat(loginResult).isNotNull();
    }
    @Test
    @Transactional
    @Rollback(value = true)
    @DisplayName("회원 수정 테스트")
    public void updateTest(){
        MemberDTO memberDTO = newMember();
       Long savedId = memberService.save(memberDTO);
        //수정용 MemberDTO
        memberDTO.setId(savedId);
        memberDTO.setMemberName("수정 이름");
        //수정처리
        memberService.update(memberDTO);

        //수정 처리 확인(DB에서 조회한 이름이 수정할 때 사용한 이름과 같은지 확인)
        MemberDTO memberDB = memberService.findById(savedId);
        assertThat(memberDB.getMemberName()).isEqualTo(memberDTO.getMemberName());


    }
    public MemberDTO newMember(){ // 새로운 member 객체 선언 메서드
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setMemberEmail("1111@aaaa.com");
        memberDTO.setMemberPassword("1111");
        memberDTO.setMemberName("이름");
        memberDTO.setMemberAge(22);
        memberDTO.setMemberPhone("010-1111-1111");
        return memberDTO;
    }
    @Test
    @Transactional
    @Rollback(value = true)
    public void deleteTest(){
        MemberDTO memberDTO = newMember();
        Long saveId = memberService.save(memberDTO);
        // 삭제처리.
         memberService.delete(saveId);
         // 삭제 후 처리가 되었는지 DB에서 비교
        assertThat(memberService.findById(saveId)).isNull();
    }
}
