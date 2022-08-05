package com.app.misc.service;

import com.app.misc.wkhtmltopdf.wrapper.Pdf;
import com.app.misc.wkhtmltopdf.wrapper.params.Param;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class HtmlToPdfService {

    public Pdf generateReportForDownload(
            String hostName, String domString)
            throws IOException, InterruptedException {
        domString =
                domString
                        .replaceAll("href=\"/", "href=\"" + hostName)
                        .replaceAll("src=\"/bower_components", "src=\"" + hostName + "bower_components")
                        .replaceAll("src=\"/modules", "src=\"" + hostName + "modules")
                        .replaceAll(
                                "src=\"../../../content/images/LopesWriteFeedback.png\" alt=\"LopesWrite Feedback Center",
                                "src=\""
                                        + hostName
                                        + "modules/plagiarism-report/content/images/LopesWriteFeedback.png\"")
                        .replaceAll(
                                "src=\"../../../content/images/AcademicGCU-color.svg\" alt=\"Grand Canyon University",
                                "src=\""
                                        + hostName
                                        + "modules/plagiarism-report/content/images/AcademicGCU-color.svg\"")
                        .replaceAll(
                                "alt=\"LopesWrite Feedback Center\" src=\"../../../content/images/LopesWriteFeedback.png\"",
                                "src=\""
                                        + hostName
                                        + "modules/plagiarism-report/content/images/LopesWriteFeedback.png\"")
                        .replaceAll(
                                "alt=\"Grand Canyon University\" src=\"../../../content/images/AcademicGCU-color.svg\"",
                                "src=\""
                                        + hostName
                                        + "modules/plagiarism-report/content/images/AcademicGCU-color.svg\"")
                        .replaceAll(
                                "<img class=\"plag--brand-logo\" ng-src=\"http://\""
                                        + hostName
                                        + "modules/plagiarism-report/app/assets/images/LopesWriteFeedback.png\" alt=\"LopesWrite Feedback Center\" src=\""
                                        + hostName
                                        + "modules/plagiarism-report/app/assets/images/LopesWriteFeedback.png\">",
                                "")
                        .replaceAll(
                                "<img class=\"plag--brand-logo\" alt=\"LopesWrite Feedback Center\" src=\"http://localhost:8080/modules/plagiarism-report/app/assets/images/LopesWriteFeedback.png\" ng-src=\"http://localhost:8080/modules/plagiarism-report/app/assets/images/LopesWriteFeedback.png\">",
                                "")
                        .replaceAll("<script", "<!--<script")
                        .replaceAll("</script>", "</script>--!>");
        Pdf pdf = new Pdf();
        pdf.addParam(new Param("--print-media-type"));
        pdf.addParam(new Param("--page-size", "letter"));
        pdf.addParam(new Param("--orientation", "portrait"));
        pdf.addParam(new Param("--dpi", "200"));
        pdf.addPageFromString(domString);

        return pdf;
    }
}
