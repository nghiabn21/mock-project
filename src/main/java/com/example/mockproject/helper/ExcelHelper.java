package com.example.mockproject.helper;

import com.example.mockproject.model.*;
import com.example.mockproject.repository.BenefitRepository;
import com.example.mockproject.repository.JobRepository;
import com.example.mockproject.repository.LevelRepository;
import com.example.mockproject.repository.SkillRepository;
import com.example.mockproject.utils.common.DateUtils;
import com.example.mockproject.utils.constant.DateFormat;
import com.example.mockproject.utils.constant.Message;
import com.example.mockproject.utils.exception.ApiRequestException;
import org.apache.commons.compress.utils.Sets;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class ExcelHelper {
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;


    @Autowired
    private SkillRepository skillRepository;
    @Autowired
    BenefitRepository benefitRepository;
    @Autowired
    private LevelRepository levelRepository;

    @Autowired
    private JobRepository jobRepository;
    private final List<? extends Object> listData;
    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    static String SHEET = "Job";

    public static final String[] JOB_HEADERS = {"jobId", "jobTitle", "skills", "startDate", "endDate", "salaryFrom", "salaryTo", "benefits", "workingAddress", "level", "description"};
    public static final String[] OFFER_HEADERS = {"candidateName", "position", "manager", "status", "interviews", "contractStart", "contractEnd", "interviewNotes",
            "contractType", "level", "department", "recruiterOwner", "dueDate", "basicSalary", "note", "offerStatus"};

    /**
     * This method to check the existence of Excel file, when user want to import job data into Database
     *
     * @param file
     * @return true/false
     */
    public static boolean hasExcelFormat(MultipartFile file) {
        if (!TYPE.equals(file.getContentType())) {
            return false;
        }
        return true;
    }

    /**
     * This method read InputStream of a file to convert data from excel file into job list
     *
     * @param is
     * @return List<Job>
     */
    public List<Job> excelToJobs(InputStream is) {
        try {
            Workbook workbook = new XSSFWorkbook(is);
            Sheet sheet = workbook.getSheet(SHEET);
            Iterator<Row> rows = sheet.iterator();
            List<Job> jobs = new ArrayList<>();
            int rowNumber = 0;

            while (rows.hasNext()) {
                Row currentRow = rows.next();
                // skip header
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }
                Iterator<Cell> cellsInRow = currentRow.iterator();
                Job job = new Job();
                int cellIdx = 1;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();
                    switch (cellIdx) {
                        case 1:
                            String jobTitle = currentCell.getStringCellValue();
                            if (jobTitle.trim().equals("")) {
                                throw new ApiRequestException(Message.MESSAGE_088);
                            }
                            if (jobTitle.length() > 255) {
                                throw new ApiRequestException(Message.MESSAGE_034);
                            }
                            if (!jobTitle.matches("[a-zA-Z0-9][a-zA-Z0-9 ]+")) {
                                throw new ApiRequestException(Message.MESSAGE_088);
                            }
                            if (jobRepository.findByJobTitleIgnoreCase(jobTitle) != null) {
                                throw new ApiRequestException("JobTitle existed");
                            }
                            job.setJobTitle(StringUtils.capitalize(jobTitle.toLowerCase()));
                            break;
                        case 2:
                            String skillCurrentCell = currentCell.getStringCellValue();

                            if (skillCurrentCell.trim().equals("")) {
                                throw new ApiRequestException(Message.MESSAGE_088);
                            }
                            // Set Skill
                            String currenCell = skillCurrentCell.replaceAll("\\s+", "");
                            String[] skillArray = currenCell.split(",");
                            Set<String> skillSet = Sets.newHashSet(skillArray);
                            checkSizeSet(skillSet);
                            Set<Skill> skills = new HashSet<>();
                            Skill skill;
                            for (String i : skillSet) {
                                skill = skillRepository.findByNameIgnoreCase(i);
                                if (skill != null) {
                                    skills.add(skill);
                                } else {
                                    skillRepository.save(new Skill(StringUtils.capitalize(i.trim().toLowerCase())));
                                    skills.add(skillRepository.findByNameIgnoreCase(i));
                                }
                            }
                            job.setSkills(skills);
                            break;
                        case 3:
                            String startDate = currentCell.getStringCellValue();
                            if (startDate.trim().equals("")) {
                                throw new ApiRequestException(Message.MESSAGE_088);
                            }
                            isValidDate(startDate);
                            job.setStartDate(DateUtils.convertStringToDate(startDate));
                            break;
                        case 4:
                            String endDate = currentCell.getStringCellValue();
                            if (endDate.trim().equals("")) {
                                throw new ApiRequestException(Message.MESSAGE_088);
                            }
                            isValidDate(endDate);
                            job.setEndDate(DateUtils.convertStringToDate(endDate));
                            break;
                        case 5:
                            /** Vaanx caafn Xử lý thêm dữ liệu đầu vào là kiểu double**/
                            String salaryFrom = currentCell.getStringCellValue();
                            if (salaryFrom.trim().equals("")) {
                                throw new ApiRequestException(Message.MESSAGE_088);
                            }
                            isValidSalary(salaryFrom);
                            job.setSalaryFrom(Double.valueOf(salaryFrom));
                            break;
                        case 6:
                            /** Xử lý thêm dữ liệu đầu vào là kiểu double**/
                            String salaryTo = currentCell.getStringCellValue();
                            if (salaryTo.trim().equals("")) {
                                throw new ApiRequestException(Message.MESSAGE_088);
                            }
                            isValidSalary(salaryTo);
                            job.setSalaryTo(Double.valueOf(salaryTo));
                            break;
                        case 7:
                            String benefitCurrentCell = currentCell.getStringCellValue();
                            if (benefitCurrentCell.trim().equals("")) {
                                throw new ApiRequestException(Message.MESSAGE_088);
                            }

                            // Set Benefit
                            String benefit1 = benefitCurrentCell.replaceAll("\\s+", "");
                            String[] benefitArray = benefit1.split(",");
                            Set<String> benefitSet = Sets.newHashSet(benefitArray);
                            checkSizeSet(benefitSet);
                            Benefit benefit;
                            Set<Benefit> benefits = new HashSet<>();
                            for (String i : benefitSet) {
                                benefit = benefitRepository.findByNameIgnoreCase(i);
                                if (benefit != null) {
                                    benefits.add(benefit);
                                } else {
                                    benefitRepository.save(new Benefit(StringUtils.capitalize(i.trim().toLowerCase())));
                                    benefits.add(benefitRepository.findByNameIgnoreCase(i));
                                }
                            }

                            job.setBenefits(benefits);
                            break;
                        case 8:
                            String workingAddress = currentCell.getStringCellValue();
                            if (workingAddress.length() > 255) {
                                throw new ApiRequestException(Message.MESSAGE_034);
                            }
                            job.setWorkingAddress(StringUtils.capitalize(workingAddress.toLowerCase()));
                            break;
                        case 9:
                            String levelCurrentCell = currentCell.getStringCellValue();
                            if (levelCurrentCell.trim().equals("")) {
                                throw new ApiRequestException(Message.MESSAGE_088);
                            }
                            // Set Level
                            String level1 = levelCurrentCell.replaceAll("\\s+", "");
                            String[] levelArray = level1.split(",");
                            Set<String> levelSetString = Sets.newHashSet(levelArray);
                            checkSizeSet(levelSetString);
                            Level level;
                            Set<Level> levels = new HashSet<>();
                            for (String i : levelSetString) {
                                level = levelRepository.findByNameIgnoreCase(i);
                                if (level != null) {
                                    levels.add(level);
                                } else {
                                    levelRepository.save(new Level(StringUtils.capitalize(i.trim().toLowerCase())));
                                    levels.add(levelRepository.findByNameIgnoreCase(i));
                                }
                            }
                            job.setLevels(levels);
                            break;
                        case 10:
                            if (currentCell.getStringCellValue().length() > 255) {
                                throw new ApiRequestException(Message.MESSAGE_034);
                            }
                            job.setDescription(StringUtils.capitalize(currentCell.getStringCellValue().toLowerCase()));
                            break;
                        default:
                            break;
                    }
                    cellIdx++;
                }

                checkSalaryToGreater(job.getSalaryTo(), job.getSalaryFrom());
                checkDate(job.getStartDate(), job.getEndDate());
                jobs.add(job);
            }
            workbook.close();
            return jobs;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
    }

    public static SimpleDateFormat dtoFormat = new SimpleDateFormat(DateFormat.DATETIME);

    public void isValidDate(String value) {
        dtoFormat.setLenient(false);
        try {
            dtoFormat.parse(value);
        } catch (ParseException e) {
            throw new ApiRequestException("Date is not true");
        }
    }

    public void checkDate(Date startDate, Date endDate) {
        LocalDate localStartDate = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate localEndDate = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        if (localStartDate.isAfter(LocalDate.now())) {
            if (!localEndDate.isAfter(localStartDate)) {
                throw new ApiRequestException("The end date must be greater than the start date");
            }
        } else {
            throw new ApiRequestException("The start date must be greater than the today");
        }
    }

    public void isValidSalary(String value) {
        if (!Pattern.matches("[0-9]+(\\.){0,1}[0-9]*", value)) {
            throw new ApiRequestException("The value of Salary must be Double and >= 0");
        }
    }

    public void checkSizeSet(Set value) {
        if (value.size() > 6) {
            throw new ApiRequestException("Enter more than 6 skills or No skills entered yet");
        }
    }

    public void checkSalaryToGreater(Double salaryTo, Double salaryFrom) {
        if (salaryFrom != null) {
            if (salaryTo <= salaryFrom) {
                throw new ApiRequestException("The value of <Salary to> must be greater than <Salary from>");
            }
        }
    }

    public ExcelHelper(List<? extends Object> listData) {
        this.listData = listData;
        workbook = new XSSFWorkbook();
    }


    private void writeHeaderLine(String[] headers) {


        sheet = workbook.createSheet("excel");

        Row row = sheet.createRow(0);

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
        for (int i = 0; i < headers.length; i++) {
            createCell(row, i, headers[i], style);
        }

    }

    /**
     * This method is used to create data fields and get information according to the input data
     *
     * @param row         row
     * @param columnCount columnCount
     * @param value       value
     * @param style       style
     */

    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else if (value instanceof Double) {
            cell.setCellValue((Double) value);
        } else if (value instanceof Set) {
            cell.setCellValue(String.valueOf(value));
        } else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }

    /**
     * This method is used to read data in the DB to return the Excel file
     *
     * @param row   Row
     * @param job   job
     * @param style style
     */
    private void writeDataLineForJob(Row row, Job job, CellStyle style) {
        int columnCount = 0;
        createCell(row, columnCount++, job.getJobId().toString(), style);
        createCell(row, columnCount++, job.getJobTitle(), style);
        createCell(row, columnCount++, String.join(", ", job.getSkills().stream().map(Skill::getName).collect(Collectors.toList())), style);
        createCell(row, columnCount++, DateUtils.convertDateToString(job.getStartDate()), style);
        createCell(row, columnCount++, DateUtils.convertDateToString(job.getEndDate()), style);
        createCell(row, columnCount++, job.getSalaryFrom().toString(), style);
        createCell(row, columnCount++, job.getSalaryTo().toString(), style);
        createCell(row, columnCount++, String.join(", ", job.getBenefits().stream().map(Benefit::getName).collect(Collectors.toList())), style);
        createCell(row, columnCount++, job.getWorkingAddress(), style);
        createCell(row, columnCount++, String.join(", ", job.getLevels().stream().map(Level::getName).collect(Collectors.toList())), style);
        createCell(row, columnCount, job.getDescription(), style);
    }

    private void writeDataLineForOffer(Row row, Offer offer, CellStyle style) {
        int columnCount = 0;
        createCell(row, columnCount++, offer.getSchedule().getCandidate().getPerson().getFullName(), style);
        createCell(row, columnCount++, offer.getPosition().getValue(), style);
        createCell(row, columnCount++, offer.getManager().getAccount(), style);
        createCell(row, columnCount++, offer.getSchedule().getCandidate().getCandidateStatus().getValue(), style);
        createCell(row, columnCount++,
                offer
                        .getSchedule()
                        .getInterviewers()
                        .stream()
                        .map(User::getAccount)
                        .collect(Collectors.toSet()),
                style);
        createCell(row, columnCount++, offer.getContractStart().toString(), style);
        createCell(row, columnCount++, offer.getContractEnd().toString(), style);
        createCell(row, columnCount++, offer.getSchedule().getNotes(), style);
        createCell(row, columnCount++, offer.getContractType().getValue(), style);
        createCell(row, columnCount++, offer.getLevel().getValue(), style);
        createCell(row, columnCount++, offer.getDepartment().getValue(), style);
        createCell(row, columnCount++, offer.getRecruiterOwner().getAccount(), style);
        createCell(row, columnCount++, offer.getDueDate().toString(), style);
        createCell(row, columnCount++, offer.getBasicSalary(), style);
        createCell(row, columnCount++, offer.getNote(), style);
        createCell(row, columnCount, offer.getOfferStatusEnum().getValue(), style);
    }

    /**
     * This method is used to write data streams
     */
    private void writeDataLines(String[] headers) {
        int rowCount = 1;
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);
        if (headers == JOB_HEADERS) {
            for (Object job : listData) {
                Row row = sheet.createRow(rowCount++);
                writeDataLineForJob(row, (Job) job, style);
            }
        }
        if (headers == OFFER_HEADERS) {
            for (Object offer : listData) {
                Row row = sheet.createRow(rowCount++);
                writeDataLineForOffer(row, (Offer) offer, style);
            }
        }

    }

    /**
     * This method creates an excel file with rows, columns and corresponding data in the DB
     *
     * @param response
     * @throws IOException
     */
    public void export(HttpServletResponse response, String[] headers) throws IOException {
        writeHeaderLine(headers);
        writeDataLines(headers);
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }
}
