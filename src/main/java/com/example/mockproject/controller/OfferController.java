package com.example.mockproject.controller;

import com.example.mockproject.dto.request.OfferRequestDto;
import com.example.mockproject.dto.request.OfferStatusRequestDto;
import com.example.mockproject.dto.response.OfferViewDto;
import com.example.mockproject.service.OfferService;
import com.example.mockproject.utils.annotation.PositiveIntegerValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/offer")
@Validated
@RequiredArgsConstructor
public class OfferController {
    private final OfferService offerService;

    /**
     *
     * @param id - offer id received from path request
     * @return ResponseEntity Offer ResponseDto
     */
    @GetMapping("{id}")
    public ResponseEntity<?> doDetail(@PathVariable(name = "id") Integer id) {
        return ResponseEntity.ok(this.offerService.findByID(id));
    }

    /**
     *
     * @param id         - offer id received from path requests
     * @param requestDto - offer information from request body
     * @return ResponseEntity Offer ResponseDto
     */
    @PutMapping("{id}")
    public ResponseEntity<?> doEdit(@PathVariable(name = "id") Integer id, @Valid @RequestBody OfferRequestDto requestDto) {
        return ResponseEntity.ok(this.offerService.update(id, requestDto));
    }

    /**
     * This api use to get data to create new offer
     * @return ResponseEntity
     */
    @GetMapping("/get-data-create-offer")
    public ResponseEntity<?> getDataCreateOffer(){
        return ResponseEntity.ok(offerService.getDataCreateOffer());
    }

    /**
     * This api use to get current waiting for approval offer
      * @return
     */
    @GetMapping("/get-current-offer")
    public ResponseEntity<?> getCurrentOffer(){
        return ResponseEntity.ok(offerService.getCurrentOffer());
    }
    /**
     * This api is used to create a new offer
     * @param offerRequestDto is request body to convert to entity and save to database
     * @return ResponseEntity
     */
    @PostMapping("/create-offer")
    public ResponseEntity<?> create(@Valid @RequestBody OfferRequestDto offerRequestDto){
        return ResponseEntity.ok(offerService.addNewOffer(offerRequestDto));
    }

    /**
     * This api is used to get offer list by candidate name, department and offer status
     * @param name - name of candidate
     * @param department - department of offer
     * @param status - status of offer
     * @param pageable - use to get page number and page size
     * @return ResponseEntity
     */
    @GetMapping("/view-offer")
    public ResponseEntity<?> getOffer(@RequestParam(defaultValue = "") String name, @RequestParam(required = false) Integer department, @RequestParam(required = false) Integer status,@PageableDefault Pageable pageable) {
        Page<OfferViewDto> viewDtos = offerService.getOfferPage(name, department, status, pageable);
        return ResponseEntity.ok(viewDtos);
    }

    @PostMapping("/change-offer-status/{id}")
    public ResponseEntity<?> changeStatusOffer(@PathVariable("id") @PositiveIntegerValidation(message = "Invalid") String id,
                                               @Valid @RequestBody 
                                               OfferStatusRequestDto offerStatusRequestDto) {
        return ResponseEntity.ok(offerService.changeStatusOffer(Integer.parseInt(id),offerStatusRequestDto ));
    }
}
