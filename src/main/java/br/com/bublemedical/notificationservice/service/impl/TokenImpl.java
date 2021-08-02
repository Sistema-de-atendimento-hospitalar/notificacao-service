package br.com.bublemedical.notificationservice.service.impl;

import java.time.LocalDateTime;
import java.util.Optional;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.TypedSort;
import org.springframework.stereotype.Service;

import br.com.bublemedical.notificationservice.domain.model.TokenAutorization;
import br.com.bublemedical.notificationservice.domain.request.TokenRequest;
import br.com.bublemedical.notificationservice.domain.request.ValidarTokenRequest;
import br.com.bublemedical.notificationservice.exception.InvalidTokenException;
import br.com.bublemedical.notificationservice.repository.NotificacaoRepository;
import br.com.bublemedical.notificationservice.service.NotificacaoService;
import br.com.bublemedical.notificationservice.service.TokenService;

@Service
public class TokenImpl implements TokenService {

	@Autowired
	private NotificacaoRepository notificacaoRepository;
	
	@Autowired
	private NotificacaoService notificacaoSns;
	

	@Override
	public void gerarToken(TokenRequest tokenRequest) {

		TokenAutorization notificacaoToken = new TokenAutorization();

		notificacaoToken.setEmail(tokenRequest.getEmail());
		notificacaoToken.setCpf(tokenRequest.getCpf());
		notificacaoToken.setToken(getRandomNumber(6));
		notificacaoToken.setDataHoraExpiracao(LocalDateTime.now().plusMinutes(10));

		notificacaoRepository.save(notificacaoToken);
		try {
			notificacaoSns.sendNotification(notificacaoToken);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void validarToken(ValidarTokenRequest validarTokenRequest) throws Exception {
		TypedSort<TokenAutorization> person = Sort.sort(TokenAutorization.class);

		Optional<TokenAutorization> tokenAutoOptional = notificacaoRepository.findFirstByEmail(validarTokenRequest.getEmail(),
				person.by(TokenAutorization::getDataHoraExpiracao).descending());
		
		if (!tokenAutoOptional.isPresent()) {
			throw new InvalidTokenException("Token não encontrado");
		}
		
		TokenAutorization tokenAutorization = tokenAutoOptional.get();
		
		System.out.print(validarTokenRequest.getToken());
		
		if (!tokenAutorization.getToken().equals(validarTokenRequest.getToken())) {
			throw new InvalidTokenException("Token inválido");
		}
		
		if (tokenAutorization.getDataHoraExpiracao().isBefore(LocalDateTime.now())) {
			throw new InvalidTokenException("Token Expirado");
		}

	}

	private String getRandomNumber(int i) {
		String numbers = "0123456789";
		StringBuilder builder = new StringBuilder(i);

		for (int m = 0; m < i; m++) {
			int myindex = (int) (numbers.length() * Math.random());
			builder.append(numbers.charAt(myindex));
		}

		return builder.toString();
	}

}
