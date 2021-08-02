package br.com.bublemedical.notificationservice.service;

import javax.mail.MessagingException;

import br.com.bublemedical.notificationservice.domain.model.TokenAutorization;

public interface NotificacaoService {
	
	public void sendNotification(TokenAutorization notificacaoToken) throws MessagingException;

}
