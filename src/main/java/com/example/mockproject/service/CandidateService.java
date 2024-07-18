package com.example.mockproject.service;

import com.example.mockproject.dto.request.CandidateRequestDto;
import com.example.mockproject.dto.response.CandidateDropDownResponseDto;
import com.example.mockproject.dto.response.CandidateInformationResponseDto;
import com.example.mockproject.dto.response.CandidateViewResponse;
import com.example.mockproject.dto.response.MessageResponseDto;
import com.example.mockproject.utils.enums.CandidateStatusEnum;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CandidateService {

    MessageResponseDto<CandidateRequestDto> createCandidate(CandidateRequestDto candidateRequestDto, MultipartFile file);
    String deleteById(String Id);

    CandidateInformationResponseDto getCandidateInformation(String id);

    Resource loadFileAsResource(String fileName, String id);

    MessageResponseDto<CandidateRequestDto> editCandidate(String id, CandidateRequestDto candidateRequestDto, MultipartFile file);

    Page<CandidateViewResponse>candidateViewResponse(String key , String status, String page , String size);

    CandidateDropDownResponseDto getCandidateDropdown();

    List<Object> getCandidateListByStatus(CandidateStatusEnum status);
}
