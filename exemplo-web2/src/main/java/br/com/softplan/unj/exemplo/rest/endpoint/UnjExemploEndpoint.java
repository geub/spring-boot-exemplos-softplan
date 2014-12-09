package br.com.softplan.unj.exemplo.rest.endpoint;

import java.util.Objects;
import java.util.UUID;

import javax.annotation.security.RolesAllowed;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UnjExemploEndpoint {

	private String chave;

	private Logger logger = LoggerFactory.getLogger(getClass());

	@RequestMapping("/testarServidor")
	@RolesAllowed("ROLE_ACESSO_SERVICO_RESTRITO")
	public String testarServidor() {
		this.logger.info("Servidor testado");
		return Objects.toString(this.chave, UUID.randomUUID().toString());
	}

	public void setChave(String chave) {
		this.chave = chave;
	}

}