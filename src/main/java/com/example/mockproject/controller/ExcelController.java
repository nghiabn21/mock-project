package com.example.mockproject.controller;

import com.example.mockproject.dto.request.ExportRequestDto;
import com.example.mockproject.helper.ExcelHelper;
import com.example.mockproject.model.Job;
import com.example.mockproject.model.Offer;
import com.example.mockproject.repository.JobRepository;
import com.example.mockproject.service.impl.ExcelService;
import com.example.mockproject.utils.exception.ApiRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RestController
@RequestMapping("/api/excel")
@Transactional
public class ExcelController {
    @Autowired
    private ExcelService excelService;
    @Autowired
    private JobRepository jobRepository;

    /**
     * This method is used to export list job from database to file Excel
     *
     * @param response - contain information of header and contentType
     * @throws IOException
     */
    @PostMapping("/users/export/excel")
    public void exportJobListToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=job_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);

        List<Job> listUsers = excelService.listAll();

        ExcelHelper excelExporter = new ExcelHelper(listUsers);

        excelExporter.export(response, ExcelHelper.JOB_HEADERS);
    }

    /**
     * This method is used to export list offer from database to file Excel
     *
     * @param response         - contain information of header and contentType
     * @param exportRequestDto - contain dateFrom and dateTo
     * @throws IOException
     */
    @PostMapping("/export/offer-excel")
    public void exportOfferListToExcel(HttpServletResponse response, @Valid @RequestBody ExportRequestDto exportRequestDto) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=offers_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);

        List<Offer> offerList = excelService.getOfferByContractStart(exportRequestDto);

        ExcelHelper excelExporter = new ExcelHelper(offerList);

        excelExporter.export(response, ExcelHelper.OFFER_HEADERS);
    }

    /**
     * This method is used to import Job data in excel file into database
     *
     * @param file
     * @return ResponseEntity
     */
    @PostMapping("/import")
    public ResponseEntity<?> importFile(@RequestParam("file") MultipartFile file) {
        if (ExcelHelper.hasExcelFormat(file)) {
            try {
                excelService.save(file);
                return ResponseEntity.status(HttpStatus.OK).body("Imported the file successfully: " + file.getOriginalFilename());
            } catch (ApiRequestException e) {
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(e.getMessage());
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Could not import the file: " + file.getOriginalFilename() + "!");
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please import an excel file!");
    }

}