package com.example.mockproject.controller;

import com.example.mockproject.dto.request.CandidateRequestDto;
import com.example.mockproject.dto.response.CandidateDropDownResponseDto;
import com.example.mockproject.dto.response.CandidateInformationResponseDto;
import com.example.mockproject.dto.response.CandidateViewResponse;
import com.example.mockproject.dto.response.MessageResponseDto;
import com.example.mockproject.service.CandidateService;
import com.example.mockproject.utils.annotation.FileAnnotation;
import com.example.mockproject.utils.annotation.OrdinalEnumConstraint;
import com.example.mockproject.utils.annotation.PositiveIntegerValidation;
import com.example.mockproject.utils.common.FileUtils;
import com.example.mockproject.utils.constant.Message;
import com.example.mockproject.utils.enums.CandidateStatusEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/candidate")
@Validated
@RequiredArgsConstructor
@Slf4j
public class CandidateController {


    private final CandidateService candidateService;

    /**
     * /**
     * Method Post
     * Create new candidate by Recruiter, Manager
     *
     * @param candidateRequestDto candidate information
     * @param file  candidate's CV
     * @return results after initialization
     */

    @PostMapping(value = "/create-candidate")
    public ResponseEntity<Object> createCandidate(@RequestPart(value = "request", required = false) @Validated CandidateRequestDto candidateRequestDto,
                                                  @RequestPart(value = "file",required = false) @FileAnnotation MultipartFile file) {
        MessageResponseDto<CandidateRequestDto> check;
        check = candidateService.createCandidate(candidateRequestDto, file);
        if (check != null) {
            return ResponseEntity.ok().body(check.getMessage());
        } else {
            return ResponseEntity.ok(Message.MESSAGE_005);
        }
    }


    /**
     * This method is used to delete a candidate according to the ID passed on the param
     * based on ID to delete field logic in DB by ID
     *
     * @param id id
     * @return ResponseEntity
     */
    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<String> deleteCandidate(@PathVariable(name = "id") @PositiveIntegerValidation(message = "Id is not true ") String id) {
        candidateService.deleteById(id);
        return new ResponseEntity<>(Message.MESSAGE_009, HttpStatus.OK);
    }

    /**
     * show candidate information details
     *
     * @param id candidate id
     * @return response entity
     */
    @GetMapping("/view-candidate-ID/{id}")
    public ResponseEntity<Object> candidateInformation(@PositiveIntegerValidation(message = Message.MESSAGE_039)
                                                           @PathVariable(name = "id") String id) {
        CandidateInformationResponseDto response = candidateService.getCandidateInformation(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Download file by id
     *
     * @param id       candidate id
     * @param fileName encrypted file name
     * @param request  request
     * @return ResponseEntity
     */
    @GetMapping("/downloadFile/{id}")
    public ResponseEntity<Object> downloadFile(@PositiveIntegerValidation(message = Message.MESSAGE_039)
                                                   @PathVariable(name = "id") String id,
                                               @RequestParam(value = "fileName", required = false, defaultValue = "") String fileName,
                                               HttpServletRequest request) {
        if ("".equals(fileName)) {
            return ResponseEntity.badRequest().body("File does not exist");
        }

        Resource resource = candidateService.loadFileAsResource(String.valueOf(fileName), id);
        String contentType = getContentTypeFile(request, resource);
        String filename = FileUtils.getOriginalFileName(resource.getFilename(), Integer.parseInt(id));
        filename = java.net.URLDecoder.decode(filename, StandardCharsets.UTF_8);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" +
                                filename + "\"")
                .body(resource);
    }


    /**
     * Edit candidate by Recruiter, Manager
     *
     * @param id                  candidate id
     * @param candidateRequestDto candidate information
     * @param file                candidate's CV
     * @return ResponseEntity
     */
    @PutMapping(value = "/edit-candidate-ID/{id}")
    public ResponseEntity<Object> editCandidateInformation(@PositiveIntegerValidation(message = Message.MESSAGE_039)
                                                           @PathVariable(name = "id") String id,
                                                           @RequestPart(value = "request",required = false) @Valid CandidateRequestDto candidateRequestDto,
                                                           @RequestPart(value = "file",required = false) @FileAnnotation MultipartFile file) {
        MessageResponseDto<CandidateRequestDto> messageResponse = candidateService.editCandidate(id, candidateRequestDto, file);
        if (messageResponse != null) {
            return ResponseEntity.ok().body(messageResponse.getMessage());
        } else {
            return ResponseEntity.badRequest().body(Message.MESSAGE_005);
        }
    }

    /**
     * Candidate View
     *
     * @param key    String search
     * @param status String
     * @param pageable   Pageable
     * @return result candidateViewResponses
     */
    @GetMapping("/candidate-list")
    public ResponseEntity<?> getCandidate(@RequestParam(required = false, defaultValue = "") String key,
                                          @OrdinalEnumConstraint(enumClass = CandidateStatusEnum.class,
                                                  message = "status is not true") @RequestParam(required = false, defaultValue = "") String status,
                                          @PageableDefault Pageable pageable) {
        Page<CandidateViewResponse> candidateViewResponses = candidateService.candidateViewResponse(key,status,String.valueOf(pageable.getPageNumber()) ,String.valueOf(pageable.getPageSize()));
        return ResponseEntity.ok(candidateViewResponses);
    }

    /**
     * get all values available in database for dropdown
     *
     * @return ResponseEntity
     */
    @GetMapping("/get-all-dropdown")
    public ResponseEntity<Object> getAllDropdown() {
        CandidateDropDownResponseDto response = candidateService.getCandidateDropdown();
        return ResponseEntity.ok(response);
    }

    /**
     * Get content type of file download
     *
     * @param request  HttpServletRequest
     * @param resource Resource
     * @return String
     */
    private String getContentTypeFile(HttpServletRequest request, Resource resource) {
        String contentType;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            contentType = "application/octet-stream";
        }

        return contentType;
    }
}
