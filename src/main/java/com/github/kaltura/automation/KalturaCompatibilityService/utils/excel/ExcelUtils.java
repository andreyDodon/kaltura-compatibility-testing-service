package com.github.kaltura.automation.KalturaCompatibilityService.utils.excel;

import com.github.kaltura.automation.KalturaCompatibilityService.model.KalturaEnum.KalturaEnum;
import com.github.kaltura.automation.KalturaCompatibilityService.model.KalturaEnum.KalturaEnums;
import com.github.kaltura.automation.KalturaCompatibilityService.model.KalturaError.KalturaError;
import com.github.kaltura.automation.KalturaCompatibilityService.model.KalturaError.KalturaErrors;
import com.github.kaltura.automation.KalturaCompatibilityService.model.KalturaXml;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author andrey.dodon - 06/05/2020
 */
@Component
public class ExcelUtils {


    public ByteArrayInputStream parseXmlToExcel(KalturaXml kalturaXml) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        XSSFWorkbook workbook = new XSSFWorkbook();
        addEnumsToExcel(kalturaXml.getKalturaEnums(), workbook);
        addErrorsToExcel(kalturaXml.getKalturaErrors(), workbook);

        workbook.write(out);
        return new ByteArrayInputStream(out.toByteArray());

    }


    private void addEnumsToExcel(List<KalturaEnum> kalturaEnums, XSSFWorkbook workbook) {
        XSSFSheet sheet = workbook.createSheet("Enums(" + kalturaEnums.size() + ")");
        Row headerRow = sheet.createRow(0);
        Cell headerCell = headerRow.createCell(0);
        headerCell.setCellValue("Enum Name");
        headerCell = headerRow.createCell(1);
        headerCell.setCellValue("Enum type");
        headerCell = headerRow.createCell(2);
        headerCell.setCellValue("Enum consts");
        int rowCount = 0;
        for (KalturaEnum kalturaEnum : kalturaEnums) {
            fillEnumRow(kalturaEnum, sheet.createRow(rowCount++));
        }

    }

    private void fillEnumRow(KalturaEnum kalturaEnum, XSSFRow row) {
        Cell cell = row.createCell(0);
        cell.setCellValue(kalturaEnum.getEnumName());
        cell = row.createCell(1);
        cell.setCellValue(kalturaEnum.getEnumType());
        cell = row.createCell(2);
        String result = Optional.ofNullable(kalturaEnum.getEnumConsts())
                .map(Collection::stream)
                .orElseGet(Stream::empty)
                .map(c -> c.getConstName() + "=" + c.getConstValue() + ";\n")
                .collect(Collectors.joining());
        cell.setCellValue(result);
    }


    private void addErrorsToExcel(List<KalturaError> kalturaErrors, XSSFWorkbook workbook) {
        XSSFSheet sheet = workbook.createSheet("Errors(" + kalturaErrors.size() + ")");
        Row headerRow = sheet.createRow(0);
        Cell headerCell = headerRow.createCell(0);
        headerCell.setCellValue("Error Name");
        headerCell = headerRow.createCell(1);
        headerCell.setCellValue("Error code");
        headerCell = headerRow.createCell(2);
        headerCell.setCellValue("Error description");
        int rowCount = 0;
        for (KalturaError kalturaError : kalturaErrors) {
            fillErrorRow(kalturaError, sheet.createRow(rowCount++));
        }

    }

    private void fillErrorRow(KalturaError kalturaError, XSSFRow row) {
        Cell cell = row.createCell(0);
        cell.setCellValue(kalturaError.getErrorName());
        cell = row.createCell(1);
        cell.setCellValue(kalturaError.getErrorCode());
        cell = row.createCell(2);
        cell.setCellValue(kalturaError.getErrorDescription());
    }


}
