package br.com.bublemedical.notificationservice.service.impl;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import br.com.bublemedical.notificationservice.domain.model.TokenAutorization;
import br.com.bublemedical.notificationservice.service.NotificacaoService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NotificationEmailImpl implements NotificacaoService {

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private QueueMessagingTemplate queueMessagingTemplate;

	@Value("${cloud.aws.end-point.uri}")
	private String endpoint;

	private final String TEMPLATE_EMAIL = "<html>\r\n" + "    <body>\r\n" + "        <p>Olá,</p><br><br>\r\n" + "  \r\n"
			+ "  <p>Segue o token criado para sua autenticação.</p>\r\n" + "  				<p>%s</p><br><br>\r\n"
			+ "\r\n" + "<p>Lembre-se que ele é valido por apenas 10 minutos</p><br><br>\r\n" + "\r\n"
			+ "		<p>bublemedical @Copyright</p><br>\r\n"
			+ "        <a href=\"http://buble-medical-web.s3-website.us-east-2.amazonaws.com/#/home\">Buble Medical</a>\r\n"
			+ "    </body>\r\n" + "</html>";

	@Override
	public void sendNotification(TokenAutorization notificacaoToken) throws MessagingException {
		log.info("Sending Message to SQS", notificacaoToken);
		queueMessagingTemplate.convertAndSend(endpoint, notificacaoToken);
		log.info("Sucesso ao Send Message to SQS");
	}

	@SqsListener(value = "sqsSendEmail", deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
	public void processMessage(TokenAutorization notificacaoToken) throws MessagingException {
		log.info("Receiver message on SQS", notificacaoToken);
		MimeMessage msg = mailSender.createMimeMessage();
		String template = String.format(TEMPLATE_EMAIL, notificacaoToken.getToken());

		MimeMessageHelper helper = new MimeMessageHelper(msg, true);
		helper.setTo(notificacaoToken.getEmail());
		helper.setSubject("Token de autenticação");
		helper.setText(template, true);

		log.info("Sending email", msg);
		mailSender.send(msg);
		log.info("Sucesso ao send email", msg);
	}

}
