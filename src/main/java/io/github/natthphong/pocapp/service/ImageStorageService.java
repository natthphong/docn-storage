package io.github.natthphong.pocapp.service;

import io.github.natthphong.pocapp.model.entity.EmployeeEntity;
import io.github.natthphong.pocapp.repository.EmployeeRepository;
import io.github.natthphong.pocapp.repository.FileRepository;
import io.github.natthphong.pocapp.utils.FileStorageComponent;
import io.github.natthphong.pocapp.utils.JasperReportComponent;
import net.sf.jasperreports.engine.JRException;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.io.*;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class ImageStorageService {

    private final FileStorageComponent fileStorageComponent;
    private final FileRepository fileRepository;
    private final EmployeeRepository employeeRepository;
    private final JasperReportComponent jasperReportComponent;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMM");

    public ImageStorageService(FileStorageComponent fileStorageComponent, FileRepository fileRepository, EmployeeRepository employeeRepository, JasperReportComponent jasperReportComponent) {
        this.fileStorageComponent = fileStorageComponent;
        this.fileRepository = fileRepository;
        this.employeeRepository = employeeRepository;
        this.jasperReportComponent = jasperReportComponent;
    }


    public void generatePdfTest(String companyCode) {
        List<EmployeeEntity> employeeEntities = employeeRepository.findAllByCreateDateAndCompanyCode(LocalDate.now(), companyCode);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ZipOutputStream zos = new ZipOutputStream(byteArrayOutputStream);
        try {
            if (!employeeEntities.isEmpty()) {
                for (EmployeeEntity employeeEntity : employeeEntities) {
                    String folder = String.format("user/%s", employeeEntity.getEmployeeId());
                    String fileName = String.format("%s_%s.pdf", employeeEntity.getEmployeeId(), employeeEntity.getCreateDate().format(formatter));
                    Map<String, Object> params = new HashMap<>();
                    ZipEntry zipEntry = new ZipEntry(fileName);
                    //10pdf test total memory 35.61
                    byte[] buffer = jasperReportComponent.generateReport(List.of(employeeEntity), params, "jasper/test.jasper");
                    zos.putNextEntry(zipEntry);
                    zos.write(buffer, 0, buffer.length);
                    //10pdf test total memory 17.58
                    fileStorageComponent.uploadFile(buffer,"application/pdf",folder,fileName);
                    zos.closeEntry();

//                    File pdfFile = File.createTempFile("report_", ".pdf");
//                  10 pdf test total memory 36.4 using  FileTemp
//                    jasperReportComponent.generateReport(List.of(employeeEntity), params, "jasper/test.jasper", pdfFile.getAbsolutePath());
//                    FileInputStream fis = new FileInputStream(pdfFile);
//
//                    zos.putNextEntry(zipEntry);
//                    byte[] buffer = new byte[4096];
//                    int length;
//                    while ((length = fis.read(buffer)) > 0) {
//                        zos.write(buffer, 0, length);
//                    }
//                      10 pdf test total memory 16.86 using  FileTemp
//                    fileStorageComponent.uploadFile(fis, fis.available(), "application/pdf", folder, fileName);
//                    System.out.println(pdfFile);
//                    pdfFile.delete();
//                    zos.closeEntry();
                }

                String fileName = String.format("%s_%s.zip", companyCode, LocalDate.now().format(formatter));
                String key = String.format("zip/%s", fileName);
                fileStorageComponent.uploadFile(byteArrayOutputStream.toByteArray(), "application/zip", key);
                zos.close();

            }
        } catch (IOException | JRException e) {
            throw new RuntimeException(e);
        }

    }

    private void writeToZip(ZipOutputStream zos, byte[] buffer, int length) throws IOException {
        zos.write(buffer, 0, length);
    }

    public void saveFile(MultipartFile multipartFile) throws IOException {

    }

    public void deleteFile(Long fileId) throws Exception {

    }


    public URL getImage(String key) {
        return fileStorageComponent.getFileUrl(key);
    }

    public List<String> getImages(String folder) {
        return fileStorageComponent.listFiles(folder);
    }
}
