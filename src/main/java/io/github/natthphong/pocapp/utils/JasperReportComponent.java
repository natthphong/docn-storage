package io.github.natthphong.pocapp.utils;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
public class JasperReportComponent {

    /**
     * Generates a PDF report based on the provided data and report template.
     *
     * @param data           The list of data to populate the report.
     * @param parameters     The parameters to pass to the report.
     * @param jasperFilePath The classpath location of the .jasper file.
     * @return The PDF report as a byte array.
     * @throws JRException if there's an error generating the report.
     */
    public byte[] generateReport(List<?> data, Map<String, Object> parameters, String jasperFilePath) throws JRException, IOException {

        ClassPathResource resource = new ClassPathResource(jasperFilePath);
        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(resource.getInputStream());


        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(data);

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        return JasperExportManager.exportReportToPdf(jasperPrint);

    }
}