package br.com.bublemedical.notificationservice.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.bublemedical.notificationservice.domain.request.TokenRequest;
import br.com.bublemedical.notificationservice.domain.request.ValidarTokenRequest;
import br.com.bublemedical.notificationservice.service.TokenService;

@RestController
@CrossOrigin
public class TokenController {
	
	@Autowired
	private TokenService tokenService;
	
	@PostMapping("/v1/autorization/token")
	@ResponseStatus(HttpStatus.CREATED)
	public void createToken(@RequestBody @Valid TokenRequest tokenRequest) {
		tokenService.gerarToken(tokenRequest);
	}
	
	@PostMapping("/v1/autorization/token/valid")
	public void validToken(@RequestBody @Valid ValidarTokenRequest tokenRequest) throws Exception {
		tokenService.validarToken(tokenRequest);
	}

}
