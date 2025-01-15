package com.mail.enumeration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ActiveProfiles;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class MailTemplateTest {

    @Test
    @DisplayName("메일 템플릿 파일이 실제 존재하는지 체크한다.")
    void existTemplateFile() {
        final String directoryPath = "/templates";
        final String fileExtension = ".html";

        Stream.of(MailTemplate.values())
                .map(MailTemplate::getTemplate)
                .forEach(templateFilePath -> {
                    String filePath = directoryPath + templateFilePath + fileExtension;
                    Resource resource = new ClassPathResource(filePath);
                    boolean exists = resource.exists() && resource.isFile();
                    System.out.println("Checking file: " + filePath + " -> Exists: " + exists);
                    assertThat(exists).isTrue();
                });
    }

    @Test
    @DisplayName("메일 템플릿에 포함되는 첨부파일이 실제 존재하는지 체크한다.")
    void existAttachments() {
        Stream.of(MailTemplate.values())
                .flatMap(mailTemplate -> mailTemplate.getAttachments()
                        .stream())
                .forEach(attachmentPath -> {
                    Resource resource = new ClassPathResource(attachmentPath);
                    boolean exists = resource.exists() && resource.isFile();
                    System.out.println("Checking file: " + attachmentPath + " -> Exists: " + exists);
                    assertThat(exists).isTrue();
                });
    }

}