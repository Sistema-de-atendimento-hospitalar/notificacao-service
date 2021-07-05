package br.com.bublemedical.notificationservice.service.impl;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.bublemedical.notificationservice.domain.model.Notificacao;
import br.com.bublemedical.notificationservice.domain.request.GerarTokenRequest;
import br.com.bublemedical.notificationservice.domain.request.ValidarTokenRequest;
import br.com.bublemedical.notificationservice.repository.NotificacaoRepository;
import br.com.bublemedical.notificationservice.service.TokenService;

public class TokenImpl implements TokenService {
	
	@Autowired
	public NotificacaoRepository notificacaoRepository;
	
	@Override
	public void gerarToken(GerarTokenRequest gerarTokenRequest) {
		
	     Notificacao notificacaoToken = new Notificacao();
	     
	     notificacaoToken.setNome(gerarTokenRequest.getNome());
	     notificacaoToken.setEmail(gerarTokenRequest.getEmail());
	     notificacaoToken.setToken(getRandomString(8));
	     notificacaoToken.setDataHoraExpiracao(LocalDateTime.now().plusMinutes(10));
		
		 notificacaoRepository.save(notificacaoToken);
	}
	
	private void enviarEmail(Notificacao notificacao) {
		
		
		
	}

	@Override
	public void validarToken(ValidarTokenRequest validarTokenRequest) {

	}

	private String getRandomString(int i) {
		String theAlphaNumericS;
		StringBuilder builder;

		theAlphaNumericS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789";
		builder = new StringBuilder(i);

		for (int m = 0; m < i; m++) {
			int myindex = (int) (theAlphaNumericS.length() * Math.random());
			builder.append(theAlphaNumericS.charAt(myindex));
		}

		return builder.toString();
	}

}
