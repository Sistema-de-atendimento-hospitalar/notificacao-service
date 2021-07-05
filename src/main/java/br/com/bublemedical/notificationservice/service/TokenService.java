package br.com.bublemedical.notificationservice.service;

import br.com.bublemedical.notificationservice.domain.request.GerarTokenRequest;
import br.com.bublemedical.notificationservice.domain.request.ValidarTokenRequest;

public interface TokenService {
	
	
	
	public void gerarToken(GerarTokenRequest gerarTokenRequest); 
	
	public void validarToken (ValidarTokenRequest validarTokenRequest); 
		
		
	

}
