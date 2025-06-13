package com.Fabrikka.loadProduct.service;

import com.Fabrikka.loadProduct.entity.ProductFile;
import com.Fabrikka.loadProduct.repository.ProductFileRepository;
import com.fabrikka.common.CategoryDto;
import com.fabrikka.common.InventoryDto;
import com.fabrikka.common.ProductDto;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Value;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Optional;


public class ExcelReader implements ItemReader<ProductDto> {

    private final Iterator<Row> rowIterator;

    public ExcelReader(@Value("#{jobParameters['fileId']}") Long fileId, ProductFileRepository productFileRepository) throws IOException {
        Optional<ProductFile> productFile = productFileRepository.findById(fileId);
        if (productFile.isEmpty()) {
            throw new IllegalArgumentException("File not found for ID: " + fileId);
        }
        InputStream inputStream = productFile.get().getFileData() != null ?
                new ByteArrayInputStream(productFile.get().getFileData()) : null;

        if (inputStream==null){
            throw new IllegalArgumentException("File data is empty for file ID: " + fileId);
        }
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);
        this.rowIterator = sheet.rowIterator();
        if(rowIterator.hasNext())
            rowIterator.next(); // Skip header row if present

    }
    @Override
    public ProductDto read() throws Exception, UnexpectedInputException,
            ParseException, NonTransientResourceException {
        if (rowIterator == null || !rowIterator.hasNext()) {
            return null; // No more rows to read
        }
        Row row = rowIterator.next();
        ProductDto productDto = new ProductDto();
        productDto.setName(row.getCell(0).getStringCellValue());
        productDto.setDescription(row.getCell(1).getStringCellValue());
        productDto.setPrice(row.getCell(2).getNumericCellValue() > 0 ?
                BigDecimal.valueOf(row.getCell(2).getNumericCellValue()) : null);
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName(row.getCell(3).getStringCellValue());
        productDto.setCategory(categoryDto);
        InventoryDto inventoryDto = new InventoryDto();
        inventoryDto.setAvailableStock((int) row.getCell(4).getNumericCellValue());
        productDto.setInventory(inventoryDto);
        productDto.setImageUrl(row.getCell(5).getStringCellValue());
        return productDto;
    }

}
