package myconext.mail;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.MustacheFactory;
import myconext.model.User;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class MailBox {

    private JavaMailSender mailSender;
    private String magicLinkUrl;
    private String mySURFconextURL;
    private String emailFrom;

    private final MustacheFactory mustacheFactory = new DefaultMustacheFactory();

    public MailBox(JavaMailSender mailSender, String emailFrom, String magicLinkUrl, String mySURFconextURL) {
        this.mailSender = mailSender;
        this.emailFrom = emailFrom;
        this.magicLinkUrl = magicLinkUrl;
        this.mySURFconextURL = mySURFconextURL;
    }

    public void sendMagicLink(User user, String hash, String requesterId) {
        String title = "Magic Link";

        Map<String, Object> variables = variables(user, title);
        variables.put("destination", requesterId);
        variables.put("hash", hash);
        variables.put("magicLinkUrl", magicLinkUrl);
        sendMail("mail_templates/magic_link.html", title, variables, user.getEmail());
    }

    public void sendAccountVerification(User user, String hash) {
        String title = "Please verify your email address for your SURFconext Guest Account";

        Map<String, Object> variables = variables(user, title);
        variables.put("hash", hash);
        variables.put("magicLinkUrl", magicLinkUrl);
        sendMail("mail_templates/account_verification.html", title, variables, user.getEmail());
    }

    public void sendAccountConfirmation(User user) {
        String title = "Your Guest Account has been created";

        Map<String, Object> variables = variables(user, title);
        variables.put("mySurfConextURL", mySURFconextURL);
        sendMail("mail_templates/account_confirmation.html", title, variables, user.getEmail());
    }

    private Map<String, Object> variables(User user, String title) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("title", title);
        variables.put("name", user.getGivenName() + " " + user.getFamilyName());
        return variables;
    }

    private void sendMail(String templateName, String subject, Map<String, Object> variables, String... to) {
        String html = this.mailTemplate(templateName, variables);

        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, false);
            helper.setSubject(subject);
            helper.setTo(to);
            setText(html, helper);
            helper.setFrom(emailFrom);
            doSendMail(message);
        } catch (MessagingException | IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    protected void setText(String html, MimeMessageHelper helper) throws MessagingException, IOException {
        helper.setText(html, true);
    }

    protected void doSendMail(MimeMessage message) {
        new Thread(() -> mailSender.send(message)).start();
    }

    private String mailTemplate(String templateName, Map<String, Object> context) {
        return mustacheFactory.compile(templateName).execute(new StringWriter(), context).toString();
    }

}
