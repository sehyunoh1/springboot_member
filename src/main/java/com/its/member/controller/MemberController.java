package com.its.member.controller;

import com.its.member.dto.MemberDTO;
import com.its.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public String saveForm() {
        return "MembersPage/MemberSave";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute MemberDTO memberDTO) {
        memberService.save(memberDTO);
        return "/MembersPage/memberLogin";
    }

    @GetMapping("/login")
    public String loginForm(@RequestParam(value = "redirectURL", defaultValue = "/member/main") String redirectURL,
                            Model model) {
        model.addAttribute("redirectURL",redirectURL);
        return "/MembersPage/MemberLogin";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute MemberDTO memberDTO, HttpSession session,
                        @RequestParam(value = "redirectURL", defaultValue = "/member/main") String redirectURL) {
        MemberDTO member = memberService.login(memberDTO);
        if (member != null) {
            session.setAttribute("member", member);
            session.setAttribute("loginEmail", member.getMemberEmail()); // 다른 기능에서 email을 id처럼 사용
            //인터셉터에 걸려서 로그인한 사용자가 직전에 요청한 페이지로 보내주기 위해서 redirect:/직전요청주소
            // 인터셉터 걸리지 않고 로그인을 하는 사용자는 defaultValue에 의해서 main으로
            return "redirect:"+redirectURL;
//            return "/MembersPage/MemberMain";
        } else {
            return "/MembersPage/MemberLogin";
        }
    }
    @GetMapping("/main")
    public String main(){ return "/MembersPage/MemberMain";}
    @GetMapping("/")
    public String findAll(Model model) {
        List<MemberDTO> memberDTOList = memberService.findAll();
        model.addAttribute("memberList", memberDTOList);
        return "/MembersPage/MemberList";
    }

    @GetMapping("/{Id}")
    public String findById(@PathVariable Long Id, Model model) {
        MemberDTO memberDTO = memberService.findById(Id);
        model.addAttribute("member", memberDTO);
        return "/MembersPage/MemberDetail";
    }

    @GetMapping("/update")
    public String updateform(Model model, HttpSession session) {
        String loginEmail = (String) session.getAttribute("loginEmail");
        MemberDTO memberDTO = memberService.findByMemberEmail(loginEmail);
        model.addAttribute("member", memberDTO);
        return "MembersPage/MemberUpdate";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute MemberDTO memberDTO) {
        memberService.update(memberDTO);
        return "MembersPage/MemberMain";
    }

    @GetMapping("/delete/{Id}")
    public String delete(@PathVariable Long Id) {
        memberService.delete(Id);
        return "redirect:/member/";
    }

    @PostMapping("/dup-check")
//    public @ResponseBody String duplicate(@RequestParam String memberEmail){
    public ResponseEntity duplicate(@RequestParam String memberEmail) {
        MemberDTO Result = memberService.findByMemberEmail(memberEmail);
        if(Result == null){
            return new ResponseEntity<>("사용할수 있습니다.", HttpStatus.OK); // HttpStatus.Ok = 200 error code
        }
        else{
            return new ResponseEntity<>("사용할수 없습니다.",HttpStatus.CONFLICT); // HttpStatus.CONFLICT =409 error code
        }
    }
    @GetMapping("/ajax/{id}")
    public ResponseEntity findByIdAxios(@PathVariable Long id) {
        System.out.println("id = " + id);
        MemberDTO memberDTO = memberService.findById(id);
        if (memberDTO != null) {
            return new ResponseEntity<>(memberDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

       /*
        get: /member/{id}
        post: /member/{id}
        delete: /member/{id}
        put: /member/{id}
     */

    @DeleteMapping("/{id}")
    public ResponseEntity deleteByAxios(@PathVariable Long id) {
        System.out.println("id = " + id);
        memberService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateByAxios(@PathVariable Long id,
                                        @RequestBody MemberDTO memberDTO) {
        System.out.println("id = " + id + ", memberDTO = " + memberDTO);
        memberService.update(memberDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
