package com.medical.taskdemo.service;

import com.medical.taskdemo.domain.entity.Company;
import com.medical.taskdemo.domain.repository.CompanyRepository;
import com.medical.taskdemo.dto.CompanyDto;
import com.medical.taskdemo.dto.CsvDto;
import com.medical.taskdemo.exception.ContentTypeNotMatchException;
import com.medical.taskdemo.exception.FileEmptyException;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CompanyService {
    private final CompanyRepository repository;

    public void uploadFile(MultipartFile file) throws IOException {
        if (file.getSize() <= 0) {
            throw new FileEmptyException("File is empty");
        }
        String filename = file.getOriginalFilename();
        String extension = filename.substring(filename.lastIndexOf(".") + 1);
        if (!extension.equals("csv")) {
            throw new ContentTypeNotMatchException("Not .csv file");
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            CsvToBean<CsvDto> csvToBean = new CsvToBeanBuilder(reader).withType(CsvDto.class).withIgnoreLeadingWhiteSpace(true).build();
            List<CsvDto> products = csvToBean.parse();
            products.forEach(csvDto -> {
                String csvId = csvDto.getId().trim();
                String name = csvDto.getName();
                String docDate = csvDto.getDocDate();
                log.info("Id : {}, Name : {}, DocDate : {}", csvId, name, docDate);
                String[] split = csvId.split("\\.", 0);
                for (int i = 0; i < split.length; i++) {
                    if (i == 0) {
                        Optional<Company> byId = repository.findById(split[i]);
                        if (byId.isEmpty()) {
                            repository.save(new Company(split[i], name, docDate));
                        }
                        continue;
                    }
                    Optional<Company> byId = repository.findByIdAndParent_Id(split[i],split[i-1]);
                    if (byId.isEmpty()) {
                        Company company = new Company(split[i], name, docDate, repository.findById(split[i-1]).get());
                        repository.save(company);
                    }
                }
            });
        } catch (Exception e) {
            log.error("Exception {}", e.getMessage());
        }
    }

    public List<CompanyDto> getAllAsTree() {
        List<CompanyDto> dtos = new ArrayList<>();
        List<Company> allParents = repository.findAllByParent_Id(null);
        allParents.forEach(company -> dtos.add(getDirectChildren(company)));
        return dtos;
    }

    public CompanyDto getDirectChildren(Company company) {
        CompanyDto dto = new CompanyDto(company.getId(), company.getName());
        Set<Company> children = company.getChildren();
        if (!children.isEmpty()) {
            dto.setChildren(children.stream().map(this::getDirectChildren).collect(Collectors.toList()));
        }
        return dto;
    }
}
