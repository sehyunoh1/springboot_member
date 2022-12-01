package com.its.member.service;

import com.its.member.dto.MemberDTO;
import com.its.member.entity.MemberEntity;
import com.its.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public Long save(MemberDTO memberDTO) {
        MemberEntity memberEntity = MemberEntity.toSaveEntity(memberDTO);
       Long saveId = memberRepository.save(MemberEntity.toSaveEntity(memberDTO)).getId();
       return saveId;
    }

    public MemberDTO login(MemberDTO memberDTO) {
            /*
                email로 DB에서 조회를 하고
                사용자가 입력한 비밀번호와 DB에서 조회한 비밀번호가 일치하는지를 판단해서
                로그인 성공, 실패 여부를 리턴.
                단, email 조회결과가 없을 때도 실패
             */
          Optional<MemberEntity> memberEntity = memberRepository.findByMemberEmail(memberDTO.getMemberEmail());
          if(memberEntity.isPresent()){
              MemberEntity member = memberEntity.get();
            MemberDTO DTO = MemberDTO.toDTO(member);
            if(memberDTO.getMemberPassword().equals(DTO.getMemberPassword())){
                return DTO;
            }else{
                return null;
            }
          }else{
              return null;
          }
    }

    public List<MemberDTO> findAll() {
    List<MemberEntity> memberEntityList = memberRepository.findAll();
    List<MemberDTO> memberDTOList = new ArrayList<>();
    for(MemberEntity memberEntity: memberEntityList){
        MemberDTO memberDTO =MemberDTO.toDTO(memberEntity);
        memberDTOList.add(memberDTO);
    }
    return memberDTOList;
    }

    public MemberDTO findById(Long Id) {
     Optional<MemberEntity> member=  memberRepository.findById(Id);
        if(member.isPresent()){
            MemberEntity memberEntity = member.get();
            MemberDTO memberDTO = MemberDTO.toDTO(memberEntity);
            return memberDTO;
        }else{
            return null;
        }
    }

    public MemberDTO findByMemberEmail(String loginEmail) {
       Optional<MemberEntity>optionalMemberEntity= memberRepository.findByMemberEmail(loginEmail);
       if(optionalMemberEntity.isPresent()){
           return MemberDTO.toDTO(optionalMemberEntity.get());
       }else {
           return null;
       }
    }

    public void update(MemberDTO memberDTO) {
       MemberEntity updateEntity= MemberEntity.toUpdateEntity(memberDTO);
        memberRepository.save(updateEntity);
    }

    public void delete(Long Id) {
        memberRepository.deleteById(Id);
    }
}
