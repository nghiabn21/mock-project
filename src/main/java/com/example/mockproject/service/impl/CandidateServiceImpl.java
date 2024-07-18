package com.example.mockproject.service.impl;

import com.example.mockproject.configuration.FileProperties;
import com.example.mockproject.dto.request.CandidateRequestDto;
import com.example.mockproject.dto.response.CandidateDropDownResponseDto;
import com.example.mockproject.dto.response.CandidateInformationResponseDto;
import com.example.mockproject.dto.response.CandidateViewResponse;
import com.example.mockproject.dto.response.MessageResponseDto;
import com.example.mockproject.model.*;
import com.example.mockproject.repository.CandidateRepository;
import com.example.mockproject.repository.PersonRepository;
import com.example.mockproject.repository.SkillRepository;
import com.example.mockproject.repository.UserRepository;
import com.example.mockproject.service.CandidateService;
import com.example.mockproject.utils.common.DateUtils;
import com.example.mockproject.utils.common.FileUtils;
import com.example.mockproject.utils.constant.Encrypt;
import com.example.mockproject.utils.constant.Message;
import com.example.mockproject.utils.enums.*;
import com.example.mockproject.utils.exception.ApiNotFoundException;
import com.example.mockproject.utils.exception.ApiRequestException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional

public class CandidateServiceImpl implements CandidateService {

    private final UserRepository userRepository;


    private final CandidateRepository candidateRepository;


    private final SkillRepository skillRepository;


    private final Path fileBasePath;

    private final PersonRepository personRepository;

    /**
     * @param userRepository      UserRepository
     * @param candidateRepository CandidateRepository
     * @param skillRepository     SkillRepository
     * @param fileProperties      fileProperties
     * @param personRepository    PersonRepository
     */
    public CandidateServiceImpl(UserRepository userRepository,
                                CandidateRepository candidateRepository,
                                SkillRepository skillRepository,
                                FileProperties fileProperties,
                                PersonRepository personRepository) {
        this.userRepository = userRepository;
        this.candidateRepository = candidateRepository;
        this.skillRepository = skillRepository;
        this.fileBasePath = Paths.get(fileProperties.getFileBasePath()).toAbsolutePath().normalize();
        this.personRepository = personRepository;
        try {
            Files.createDirectories(this.fileBasePath);
        } catch (Exception ex) {
            throw new ApiRequestException(Message.MESSAGE_036);
        }
    }


    /**
     * Create new candidate in Transactional
     * First : create new candidate include the original file name
     * Check email is exist or not (not : save candidate)
     * Check phone is exist or not (not : save candidate)
     * Check phone is valid or not (valid : save candidate)
     * Check recruiter's account is exist or not (exist : save candidate)
     * check Set<String> skills
     * Second : use the method uploadCV to save the file to the computer
     * third : use the method linkEncryption to encode file name after change file name in computer
     *
     * @param candidateRequestDto Candidate request
     * @param file                file CV request
     * @return Result
     */
    @Override
    public MessageResponseDto<CandidateRequestDto> createCandidate(CandidateRequestDto candidateRequestDto, MultipartFile file) {
        Candidate candidate = new Candidate();
        Person person = new Person();
        MessageResponseDto<CandidateRequestDto> messageResponseDto = new MessageResponseDto<>();

        checkInput(candidateRequestDto, file);

        //check email exist
        if (personRepository.existsByEmail(candidateRequestDto.getEmail())) {
            throw new ApiRequestException(Message.MESSAGE_029);
        }
        //check phone exist
        if ((candidateRequestDto.getPhoneNumber() != null) && !candidateRequestDto.getPhoneNumber().trim().isEmpty()) {
            if (personRepository.existsByPhoneNumber(candidateRequestDto.getPhoneNumber())) {
                throw new ApiRequestException(Message.MESSAGE_030);
            }
        }

        //set data to candidate
        String fileNameOriginal = file.getOriginalFilename();

        toCandidate(candidate, candidateRequestDto, fileNameOriginal);

        //set data to person
        toPerson(person, candidateRequestDto);

        //set person to candidate

        candidate.setPerson(person);
        // set Set<String> skills

        try {
            candidateRepository.save(candidate);
            //save file (change file name)
            String fileName = uploadCV(candidate.getPerson().getEmail(), file);
            messageResponseDto.setMessage(Message.MESSAGE_006);
            messageResponseDto.setData(candidateRequestDto);
            int id = candidateRepository.findCandidateByPersonEmail(candidateRequestDto.getEmail()).orElseThrow(
                    () -> new ApiNotFoundException(Message.MESSAGE_033)
            ).getCandidateId();
            if ("".equals(fileName)) {
                messageResponseDto.setLink("");
            } else {
                messageResponseDto.setLink(linkEncryption(fileName, id));
            }
            return messageResponseDto;
        } catch (ApiRequestException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException |
                 IllegalBlockSizeException | BadPaddingException e) {
            throw new ApiRequestException(e.getMessage());
        }
    }

    /**
     * When showing file name to download,
     * encode the file name in the computer
     *
     * @param name File name
     * @return encrypted file name
     * @throws NoSuchAlgorithmException  Exception
     * @throws NoSuchPaddingException    Exception
     * @throws InvalidKeyException       Exception
     * @throws IllegalBlockSizeException Exception
     * @throws BadPaddingException       Exception
     */
    public String linkEncryption(String name, int id) throws NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        SecretKeySpec keySpec = new SecretKeySpec(Encrypt.SECRET_KEY.getBytes(), Encrypt.ALGORITHM);
        //encode file name
        Cipher cipher = Cipher.getInstance(Encrypt.TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        byte[] byteEncrypted = cipher.doFinal(name.getBytes());
        String encrypted = Base64.getUrlEncoder().encodeToString(byteEncrypted);
        String link = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/candidate/downloadFile/" + id + "?fileName=")
                .path(encrypted)
                .toUriString();
        return java.net.URLDecoder.decode(link, StandardCharsets.UTF_8);
    }

    /**
     * This method is used to delete candidate information via ID , check if data already exists in DB
     * If yes, then proceed to delete the logic, otherwise return the message that the ID is not available
     *
     * @param id candidate id
     * @return String
     */
    @Override
    public String deleteById(String id) {
        Candidate candidate1 = candidateRepository.findById(Integer.valueOf(id)).orElseThrow(() -> new ApiRequestException(Message.MESSAGE_041));
       if(candidate1.getCandidateStatus()==CandidateStatusEnum.BANNED){
           throw new ApiRequestException(" id has been deleted");
       }
        try {
            candidate1.setCandidateStatus(CandidateStatusEnum.BANNED);
            candidateRepository.save(candidate1);
            return (Message.MESSAGE_009);
        } catch (Exception e) {
            return (Message.MESSAGE_010);
        }
    }

    /**
     * Get information candidate details if candidate exist
     *
     * @param id candidate id
     * @return CandidateInformationResponseDto
     */
    @Override
    public CandidateInformationResponseDto getCandidateInformation(String id) {
        Candidate candidate = candidateRepository.findById(Integer.valueOf(id)).orElseThrow(
                () -> new ApiRequestException(Message.MESSAGE_031)
        );

        int candidateId = Integer.parseInt(id);
        String fileName = candidate.getCvAttachment();
        String now = DateUtils.convertDateToString(new Date());
        String createDate = DateUtils.convertDateToString(candidate.getCreatedDate());
        String lastModifiedDate = DateUtils.convertDateToString(candidate.getLastModifiedDate());
        CandidateInformationResponseDto response = new CandidateInformationResponseDto();

        response.setFullName(candidate.getPerson().getFullName());
        response.setDob(DateUtils.convertDateToString(candidate.getPerson().getDob()));
        response.setPhoneNumber(candidate.getPerson().getPhoneNumber());
        response.setEmail(candidate.getPerson().getEmail());
        response.setAddress(candidate.getPerson().getAddress());
        response.setGender(candidate.getPerson().getGender().getValue());
        response.setCvAttachment(candidate.getCvAttachment());
        response.setPosition(candidate.getPositionEnum().getValue());
        response.setSkills(candidate.getSkills().stream().map(Skill::getName).collect(Collectors.toSet()));
        response.setRecruiter(candidate.getRecruiter().getAccount());
        response.setStatus(candidate.getCandidateStatus().getValue());
        response.setYearOfExperience(candidate.getYearOfExperience());
        response.setHighestLevel(candidate.getHighestLevel().getValue());
        response.setNote(candidate.getNote());
        response.setCreatedDate(createDate.equals(now) ? "today" : createDate);
        response.setLastModifiedDate(lastModifiedDate.equals(now) ? "today" : lastModifiedDate);
        response.setLastModifiedBy(candidate.getLastModifiedBy());
        try {
            if ("".equals(fileName) || fileName == null) {
                response.setLink("");
            } else {
                response.setLink(linkEncryption(FileUtils.getNewFileName(fileName, candidateId), candidateId));
            }
        } catch (ApiRequestException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException |
                 IllegalBlockSizeException | BadPaddingException e) {
            throw new ApiRequestException(e.getMessage());
        }
        return response;
    }


    /**
     * Encrypt file name and download file
     *
     * @param fileName encrypted file name
     * @return Resource
     */
    @Override
    public Resource loadFileAsResource(String fileName, String id) {
        String fileNameDecrypted;
        try {
            SecretKeySpec keySpec = new SecretKeySpec(Encrypt.SECRET_KEY.getBytes(), Encrypt.ALGORITHM);
            Cipher cipher = Cipher.getInstance(Encrypt.TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            byte[] byteDecrypted = cipher.doFinal(Base64.getUrlDecoder().decode(fileName));
            fileNameDecrypted = new String(byteDecrypted);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException |
                 IllegalBlockSizeException | BadPaddingException e) {
            throw new ApiRequestException(e.getMessage());
        }

        try {
            if (FileUtils.hasIdInFileName(fileNameDecrypted, id)) {
                Path filePath = this.fileBasePath.resolve(fileNameDecrypted).normalize();
                Resource resource = new UrlResource(filePath.toUri());
                System.out.println(filePath.toUri());
                if (resource.exists()) {
                    return resource;
                }

                throw new ApiRequestException("File not found " + fileName);
            }

            throw new ApiRequestException("Id is not the id of the file");
        } catch (MalformedURLException ex) {
            throw new ApiRequestException("File not found " + fileName);
        }
    }

    /**
     * Update candidate include the original file name, update file to the computer
     *
     * @param id                  candidate id
     * @param candidateRequestDto candidate information
     * @param file                cv
     * @return result
     */
    @Override
    public MessageResponseDto<CandidateRequestDto> editCandidate(String id, CandidateRequestDto candidateRequestDto, MultipartFile file) {
        checkInput(candidateRequestDto, file);
        Candidate candidateEdit = candidateRepository.findById(Integer.valueOf(id)).orElseThrow(
                () -> new ApiNotFoundException(Message.MESSAGE_037)
        );

        if (!candidateEdit.getPerson().getEmail().equals(candidateRequestDto.getEmail()) &&
                personRepository.existsByEmail(candidateRequestDto.getEmail())) {
            throw new ApiRequestException(Message.MESSAGE_029);
        }

        if (candidateRequestDto.getPhoneNumber() != null && !candidateRequestDto.getPhoneNumber().trim().equals("")) {
            if (!candidateEdit.getPerson().getPhoneNumber().equals(candidateRequestDto.getPhoneNumber()) &&
                    personRepository.existsByPhoneNumber(candidateRequestDto.getPhoneNumber())) {
                throw new ApiRequestException(Message.MESSAGE_030);
            }
        }

        String fileNameOriginal = file.getOriginalFilename();
        Person person = candidateEdit.getPerson();

        toPerson(person, candidateRequestDto);
        toCandidate(candidateEdit, candidateRequestDto, fileNameOriginal);
        candidateEdit.setPerson(person);
        try {
            candidateRepository.save(candidateEdit);
            //save file (change file name)
            String fileName = uploadCV(Integer.parseInt(id), file);
            MessageResponseDto<CandidateRequestDto> messageResponseDto = new MessageResponseDto<>();
            messageResponseDto.setMessage(Message.MESSAGE_008);
            messageResponseDto.setData(candidateRequestDto);
            if ("".equals(fileName)) {
                messageResponseDto.setLink("");
            } else {
                messageResponseDto.setLink(linkEncryption(fileName, Integer.parseInt(id)));
            }

            return messageResponseDto;
        } catch (ApiRequestException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException |
                 IllegalBlockSizeException | BadPaddingException e) {
            throw new ApiRequestException(e.getMessage());
        }
    }


    /**
     * Check input data  status :
     * status is empty or not
     * Find Candidate by key and status
     *
     * @param key    String
     * @param status String
     * @param page   String
     * @param size   String
     * @return
     */
    @Override
    public Page<CandidateViewResponse> candidateViewResponse(String key, String status, String page, String size) {
        if (Integer.parseInt(page)*Integer.parseInt(size)<0){
            throw new ApiRequestException("Number of candidates is over range");
        }

        Pageable pageable = PageRequest.of(Integer.parseInt(page), Integer.parseInt(size), Sort.by("candidateId").ascending());

        Page<CandidateViewResponse> candidateViewResponses;
        if (status.isEmpty()) {
            candidateViewResponses = candidateRepository.findCandidate(key, null, pageable);
            if (!candidateViewResponses.hasContent()) {
                throw new ApiNotFoundException(Message.MESSAGE_038);
            } else {
                return candidateViewResponses;
            }
        } else {
            candidateViewResponses = candidateRepository.findCandidate(key, Integer.parseInt(status), pageable);
            if (!candidateViewResponses.hasContent()) {
                throw new ApiNotFoundException(Message.MESSAGE_038);
            }
            return candidateViewResponses;
        }

    }

    /**
     * Setting data to response
     *
     * @return CandidateDropDownResponseDto
     */
    @Override
    public CandidateDropDownResponseDto getCandidateDropdown() {
        CandidateDropDownResponseDto response = new CandidateDropDownResponseDto();
        response.setGender(Stream.of(GenderEnum.values()).map(GenderEnum::getKey).collect(Collectors.toList()));
        response.setCandidateStatus(Stream.of(CandidateStatusEnum.values()).map(CandidateStatusEnum::getKey).collect(Collectors.toList()));
        response.setHighestLevel(Stream.of(HighestLevelEnum.values()).map(HighestLevelEnum::getKey).collect(Collectors.toList()));
        response.setPosition(Stream.of(PositionEnum.values()).map(PositionEnum::getKey).collect(Collectors.toList()));
        response.setSkills(skillRepository.findAllName());
        response.setRecruiter(userRepository.findAllAccount());
        return response;
    }

    /**
     * Check if the skill is in the table or not, if not, add more
     * Change Set<String> to Set<Skill>
     *
     * @param skills Input Set<String> skills
     * @return Set<Skill> skillsSet
     */
    private Set<Skill> skillSet(Set<String> skills) {
        // check Skill is exists in database
        try {
            Set<Skill> skillSet = new HashSet<>();
            skills.forEach(n -> {
                if (!skillRepository.existsByName(n)) {
                    //If the skill does not exist, save it to the database
                    skillRepository.save(new Skill(n));
                }
                skillSet.add(skillRepository.findByName(n));
            });
            return skillSet;
        } catch (Exception e) {
            throw new ApiRequestException(Message.MESSAGE_005 + e.getMessage());
        }
    }

    /**
     * Change file name to id + Candidate + file name
     * Find candidate id by email because email is unique
     * Add file to D://
     *
     * @param email Candidate's email
     * @param file  Candidate's Cv
     */
    private String uploadCV(String email, MultipartFile file) {
        Candidate candidate = candidateRepository.findCandidateByPersonEmail(email).orElseThrow(
                () -> new ApiNotFoundException(Message.MESSAGE_033)
        );
        //change file name
        String name = file.getOriginalFilename();
        if ("".equals(name) || name == null) {
            return "";
        }

        String fileName = FileUtils.getNewFileName(StringUtils.cleanPath(name), candidate.getCandidateId());
        return toSaveFile(fileName, file);
    }

    /**
     * Change file name to id + Candidate + file name
     * Find candidate id by email because email is unique
     * Add file to D://
     *
     * @param id   Candidate id
     * @param file Candidate Cv
     */
    private String uploadCV(int id, MultipartFile file) {
        String name = file.getOriginalFilename();
        if ("".equals(name) || name == null) {
            return "";
        }

        String fileName = FileUtils.getNewFileName(StringUtils.cleanPath(file.getOriginalFilename()), id);
        return toSaveFile(fileName, file);
    }

    /**
     * save file to server
     *
     * @param fileName String
     * @param file     MultipartFile
     * @return String file name new
     */
    private String toSaveFile(String fileName, MultipartFile file) {
        try {
            // Check if the file's name contains invalid characters
            if (fileName.contains("..")) {
                throw new ApiRequestException("Sorry! Filename contains invalid path sequence " + fileName);
            }
            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.fileBasePath.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (IOException ex) {
            throw new ApiRequestException("Could not store file " + fileName + ". Please try again!");
        }
    }

    /**
     * This method use to get candidate list by status
     *
     * @param status is keyword to filter candidate list
     * @return List<Candidate>
     */
    @Override
    public List<Object> getCandidateListByStatus(CandidateStatusEnum status) {
        return candidateRepository.getCandidateListByStatus(status.getKey());
    }

    /**
     * Check input from request
     * If error will throw exception
     *
     * @param candidateRequestDto CandidateRequestDto
     */
    private void checkInput(CandidateRequestDto candidateRequestDto, MultipartFile file) {
        if (candidateRequestDto == null) {
            throw new ApiRequestException("No data!");
        }

        String dob = candidateRequestDto.getDob();

        if (!"".equals(dob) && dob != null) {
            if (!DateUtils.checkAge(candidateRequestDto.getDob())) {
                throw new ApiRequestException(Message.MESSAGE_026);
            }
        }
        // check recruiter's account
        if (!userRepository.existsByAccountIgnoreCaseAndStatus(candidateRequestDto.getAccount(), UserStatusEnum.ACTIVATED)) {
            throw new ApiNotFoundException(Message.MESSAGE_028);
        } else {
            User recruiter = userRepository.findUserByAccountIgnoreCase(candidateRequestDto.getAccount());
            Set<String> role = recruiter.getRoles().stream().map(roles -> roles.getRole().getValue()).collect(Collectors.toSet());
            if (!role.contains("ROLE_RECRUITER")) {
                throw new ApiRequestException("User does not have Recruiter permission");
            }
        }

        if (!"".equals(Objects.requireNonNull(file.getOriginalFilename()).trim())) {
            if (file.getSize() > 10485760) {
                throw new ApiRequestException("file must be less than 10Mb");
            } else if (file.getOriginalFilename().length() > 255) {
                throw new ApiRequestException("Maximum file name length is 255");
            }
        }
    }

    /**
     * Covert CandidateRequestDto to Candidate
     *
     * @param candidate           Candidate
     * @param candidateRequestDto CandidateRequestDto
     * @param fileName            String
     */
    private void toCandidate(Candidate candidate, CandidateRequestDto candidateRequestDto, String fileName) {
        candidate.setCvAttachment(fileName);
        candidate.setNote(candidateRequestDto.getNote());
        candidate.setPositionEnum(PositionEnum.getByKey(Integer.parseInt(candidateRequestDto.getPositionEnum())));
        candidate.setCandidateStatus(CandidateStatusEnum.getByKey(Integer.parseInt(candidateRequestDto.getCandidateStatus())));
        candidate.setHighestLevel(HighestLevelEnum.getByKey(Integer.parseInt(candidateRequestDto.getHighestLevel())));
        if (!(candidateRequestDto.getYearOfExperience() == null) && !candidateRequestDto.getYearOfExperience().isEmpty()) {
            candidate.setYearOfExperience(Integer.parseInt(candidateRequestDto.getYearOfExperience()));
        }
        candidate.setSkills(skillSet(candidateRequestDto.getSkills()));
        candidate.setRecruiter(userRepository.findUserByAccountIgnoreCase(candidateRequestDto.getAccount()));
    }

    /**
     * Convert CandidateRequestDto Person
     *
     * @param person              Person
     * @param candidateRequestDto CandidateRequestDto
     */
    private void toPerson(Person person, CandidateRequestDto candidateRequestDto) {
        String dob = candidateRequestDto.getDob();
        person.setFullName(candidateRequestDto.getFullName());
        if (!"".equals(dob) && dob != null) {
            person.setDob(DateUtils.convertStringToDate(dob));
        }
        person.setPhoneNumber(candidateRequestDto.getPhoneNumber());
        person.setEmail(candidateRequestDto.getEmail());
        person.setAddress(candidateRequestDto.getAddress());
        person.setGender(GenderEnum.getByKey(Integer.parseInt(candidateRequestDto.getGender())));
    }

}
