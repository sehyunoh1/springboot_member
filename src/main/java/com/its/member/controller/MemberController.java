package com.its.member.controller;

import com.its.member.dto.MemberDTO;
import com.its.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
private final MemberService memberService;
    @GetMapping("/save")
    public String saveForm(){return "MembersPage/MemberSave";}

    @PostMapping("/save")
    public String save(@ModelAttribute MemberDTO memberDTO){
        memberService.save(memberDTO);
        return "/MembersPage/memberLogin";
    }
    @GetMapping("/login")
    public String loginForm(){ return "/MembersPage/MemberLogin";}

    @PostMapping("/login")
    public String login(@ModelAttribute MemberDTO memberDTO, HttpSession session){
       MemberDTO member= memberService.login(memberDTO);
       if(member != null){
           session.setAttribute("member",member);
           session.setAttribute("loginEmail",member.getMemberEmail()); // 다른 기능에서 email을 id처럼 사용
           return "/MembersPage/MemberMain";
       }else {
           return "/MembersPage/MemberLogin";
       }
    }
    @GetMapping("/")
    public String findAll(Model model){
        List<MemberDTO> memberDTOList = memberService.findAll();
        model.addAttribute("memberList",memberDTOList);
        return "/MembersPage/MemberList";
    }
    @GetMapping("/{Id}")
    public String findById(@PathVariable Long Id,Model model){
          MemberDTO memberDTO=  memberService.findById(Id);
          model.addAttribute("member",memberDTO);
          return "/MembersPage/MemberDetail";
    }

    @GetMapping("/update")
    public String updateform(Model model, HttpSession session){
        String loginEmail = (String)session.getAttribute("loginEmail");
        MemberDTO memberDTO = memberService.findByMemberEmail(loginEmail);
        model.addAttribute("member",memberDTO);
        return "MembersPage/MemberUpdate";
    }
    @PostMapping("/update")
    public String update(@ModelAttribute MemberDTO memberDTO){
        memberService.update(memberDTO);
        return "MembersPage/MemberMain";
    }

    @GetMapping("/delete/{Id}")
    public String delete(@PathVariable Long Id){
        memberService.delete(Id);
        return "redirect:/member/";
    }
}
