package com.medical.taskdemo.service;

import com.medical.taskdemo.domain.entity.Product;
import com.medical.taskdemo.domain.repository.ProductRepository;
import com.medical.taskdemo.dto.CsvDto;
import com.medical.taskdemo.dto.ProductDto;
import com.medical.taskdemo.exception.ContentTypeNotMatchException;
import com.medical.taskdemo.exception.FileEmptyException;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;

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
            products.sort(Comparator.comparing(CsvDto::getIdLength));
            products.forEach(csvDto -> {
                String csvId = csvDto.getId().trim();
                String name = csvDto.getName();
                String docDate = csvDto.getDocDate();
                log.info("Id : {}, Name : {}, DocDate : {}", csvId, name, docDate);
                String[] split = csvId.split("\\.", 0);
                for (int i = 0; i < split.length; i++) {
                    if (i == 0) {
                        Optional<Product> byId = repository.findById(split[i]);
                        if (byId.isEmpty()) {
                            repository.save(new Product(split[i], name, docDate,null));
                        }
                        continue;
                    }
                    Optional<Product> byProductIdAndParentId = repository.findByIdAndParentId(split[i], split[i - 1]);
                    if (byProductIdAndParentId.isEmpty()) {
                        repository.save(new Product(split[i], name, docDate, split[i-1]));
                    }
                }
            });
        } catch (Exception e) {
            log.error("Exception {}", e.getMessage());
        }
    }

    public List<ProductDto> getAllAsTree() {
        List<ProductDto> products = new ArrayList<>();
        List<Product> allByParentId = repository.findAllByParentId(null);
        allByParentId.forEach(product -> {
            products.add(getChildren(product));
        });
        return products;
    }

    private ProductDto getChildren(Product product) {
        String parentId = product.getId();
        String parentName = product.getName();
        ProductDto dto = new ProductDto(parentId, parentName);
        List<Product> allByParentId1 = repository.findAllByParentId(parentId);
        if (!allByParentId1.isEmpty()) {
            dto.setChildren(allByParentId1.stream()
                    .map(this::getChildren)
                    .collect(Collectors.toList()));
        }
        return dto;
    }
}
