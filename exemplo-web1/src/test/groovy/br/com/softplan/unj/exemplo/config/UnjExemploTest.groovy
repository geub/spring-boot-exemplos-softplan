package br.com.softplan.unj.exemplo.config

import groovyx.net.http.ContentType
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method

import org.junit.Assert;
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.IntegrationTest
import org.springframework.boot.test.SpringApplicationConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.web.WebAppConfiguration

import br.com.softplan.unj.exemplo.rest.endpoint.UnjExemploEndpoint;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = UnjExemploApplication.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")
class UnjExemploTest {
	
	@Autowired
	private UnjExemploEndpoint endpoint;
	
	@Value('${local.server.port}')
	int port

	@Test
	void testeServidor() {
		def chaveEsperada = "chave"
		endpoint.chave = chaveEsperada
		def http = new HTTPBuilder("http://localhost:${port}")
		http.request(Method.GET, ContentType.TEXT) {
			uri.path = '/testarServidor'
			response.success = { resp, reader ->
				def text = reader.text
				println "Resposta do servidor: ${text}"
				Assert.assertEquals("resposta inesperada", chaveEsperada, text)
				
			}
		}
	}

}
