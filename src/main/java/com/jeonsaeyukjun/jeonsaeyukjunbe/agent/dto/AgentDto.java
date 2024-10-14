package com.jeonsaeyukjun.jeonsaeyukjunbe.agent.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true) // 인식하지 못하는 필드 무시
public class AgentDto {
    private String bzmn_conm; // 공인중개사명
    private String stdg_cd; // 행정동 코드
    private String addr; // 주소
    private String telno; //전화번호
}
