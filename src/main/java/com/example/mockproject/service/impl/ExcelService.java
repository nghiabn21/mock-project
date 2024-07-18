package com.example.mockproject.service.impl;

import com.example.mockproject.dto.request.ExportRequestDto;
import com.example.mockproject.helper.ExcelHelper;
import com.example.mockproject.model.Job;
import com.example.mockproject.model.Offer;
import com.example.mockproject.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.mockproject.repository.OfferRepository;
import com.example.mockproject.utils.common.DateUtils;
import com.example.mockproject.utils.exception.ApiRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExcelService {

    private final JobRepository repository;
    private final ExcelHelper excelHelper;
    private final OfferRepository offerRepository;

    /**
     * This method to save the job list get from file excel to database
     * @param file file
     */
    public void save(MultipartFile file) {
        try {
            List<Job> jobs = excelHelper.excelToJobs(file.getInputStream());
            repository.saveAll(jobs);
        } catch (IOException e) {
            throw new RuntimeException("fail to store excel data: " + e.getMessage());
        }
    }

    /**
     * this method get all information of table JOB
     * @return repository
     */
    public List<Job> listAll() {
        return repository.findAll();
    }

    /**
     * method is used to get list offer by param contractStart
     * @param exportRequestDto - is request body use to get information of dateFrom and dateTo to compare with contractStart
     * @return List<Offer>
     */
    public List<Offer> getOfferByContractStart(ExportRequestDto exportRequestDto){

        Date dateFrom = DateUtils.convertStringToDate(exportRequestDto.getDateFrom());
        Date dateTo = DateUtils.convertStringToDate(exportRequestDto.getDateTo());
        if (dateFrom.after(dateTo)) {
            throw new ApiRequestException("dateFrom must be before dateTo");
        }

        return offerRepository.findByContractStart(dateFrom, dateTo);
    }
}
