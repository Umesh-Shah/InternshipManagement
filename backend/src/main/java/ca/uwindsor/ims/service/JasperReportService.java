package ca.uwindsor.ims.service;

import jakarta.annotation.PostConstruct;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class JasperReportService {

    private static final List<String> REPORT_NAMES =
        List.of("students", "companies", "internship-types", "gpa", "jobs");

    private final Map<String, JasperReport> compiled = new ConcurrentHashMap<>();
    private final ResourceLoader resourceLoader;

    public JasperReportService(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @PostConstruct
    void compileAll() {
        for (String name : REPORT_NAMES) {
            try {
                Resource r = resourceLoader.getResource("classpath:reports/" + name + ".jrxml");
                compiled.put(name, JasperCompileManager.compileReport(r.getInputStream()));
            } catch (JRException | IOException e) {
                throw new IllegalStateException("Failed to compile JasperReport: " + name, e);
            }
        }
    }

    public byte[] generatePdf(String reportName, Collection<?> data) throws JRException {
        JasperReport report = compiled.get(reportName);
        if (report == null) {
            throw new IllegalArgumentException("Unknown report: " + reportName);
        }
        JRDataSource ds = new JRBeanCollectionDataSource(data);
        JasperPrint print = JasperFillManager.fillReport(report, Map.of(), ds);
        return JasperExportManager.exportReportToPdf(print);
    }
}
