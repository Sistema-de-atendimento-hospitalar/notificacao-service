package br.com.bublemedical.notificationservice.service;

import br.com.bublemedical.notificationservice.domain.request.TokenRequest;
import br.com.bublemedical.notificationservice.domain.request.ValidarTokenRequest;

public interface TokenService {

	public void gerarToken(TokenRequest tokenRequest);

	public void validarToken(ValidarTokenRequest validarTokenRequest) throws Exception;

}
