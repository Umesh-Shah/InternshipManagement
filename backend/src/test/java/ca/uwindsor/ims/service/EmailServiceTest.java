package ca.uwindsor.ims.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock JavaMailSender mailSender;
    @InjectMocks EmailService service;

    @Test
    void sendRegistrationEmail_whenDisabled_skipsMailSender() {
        // enabled defaults to false (injected as @Value("${spring.mail.enabled:false}"))
        service.sendRegistrationEmail("a@b.com", "alice", "pass", 1001);

        verifyNoInteractions(mailSender);
    }

    @Test
    void sendRegistrationEmail_whenEnabled_sendsCorrectMessage() {
        ReflectionTestUtils.setField(service, "enabled", true);
        ReflectionTestUtils.setField(service, "fromAddress", "noreply@test.com");

        service.sendRegistrationEmail("student@example.com", "alice", "pass123", 1001);

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(captor.capture());
        SimpleMailMessage msg = captor.getValue();
        assertThat(msg.getTo()).containsExactly("student@example.com");
        assertThat(msg.getFrom()).isEqualTo("noreply@test.com");
        assertThat(msg.getSubject()).contains("Registration");
        assertThat(msg.getText()).contains("alice").contains("pass123").contains("1001");
    }

    @Test
    void sendRegistrationEmail_whenMailSenderThrows_doesNotRethrow() {
        ReflectionTestUtils.setField(service, "enabled", true);
        ReflectionTestUtils.setField(service, "fromAddress", "noreply@test.com");
        doThrow(new RuntimeException("SMTP down")).when(mailSender).send(any(SimpleMailMessage.class));

        assertThatCode(() -> service.sendRegistrationEmail("a@b.com", "alice", "pass", 1001))
                .doesNotThrowAnyException();
    }
}
