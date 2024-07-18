package com.example.mockproject.service;

import com.example.mockproject.dto.request.OfferRequestDto;
import com.example.mockproject.dto.request.OfferStatusRequestDto;
import com.example.mockproject.dto.response.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.mockproject.model.Offer;


import java.time.LocalDateTime;
import java.util.List;

public interface OfferService {
    OfferResponseDto findByID(Integer id);
    OfferResponseDto update(Integer id, OfferRequestDto requestDto);
    Page<OfferViewDto> getOfferPage(String name, Integer department, Integer status, Pageable pageable);
    Integer getCurrentOffer();
    DataCreateOfferResponseDto getDataCreateOffer();
    MessageResponseDto addNewOffer(OfferRequestDto offerRequestDto);
    OfferResponseDto changeStatusOffer(int id, OfferStatusRequestDto offerStatusRequestDto);
    OfferRemindResponseDto convertToDto(Offer offer);
    List<Offer> getAllOfferDueDate();
    void sendEmailReminder(List<Offer> offerList);
}
