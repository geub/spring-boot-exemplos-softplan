package br.com.softplan.unj.exemplo.config;

import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.jasig.cas.client.validation.Cas20ServiceTicketValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.cas.authentication.CasAuthenticationProvider;
import org.springframework.security.cas.web.CasAuthenticationEntryPoint;
import org.springframework.security.cas.web.CasAuthenticationFilter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

@Configuration
@EnableGlobalMethodSecurity(jsr250Enabled = true)
public class UnjExemploApplication extends WebSecurityConfigurerAdapter {

	public static final List<GrantedAuthority> ROLES_USUARIO_AUTENTICADO = Arrays.asList(new SimpleGrantedAuthority("ROLE_ACESSO_SERVICO_RESTRITO"));

	@Value("${cas.loginpage}")
	private String casLoginPage;
	@Value("${cas.ticketvalidation}")
	private String casTicketValidation;
	@Value("${service.url}")
	private String serviceUrl;
	private ServiceProperties serviceProperties;

	@PostConstruct
	void criarServiceProperties() throws URISyntaxException {
		this.serviceProperties = new ServiceProperties();
		this.serviceProperties.setService(this.serviceUrl);
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		CasAuthenticationProvider provider = new CasAuthenticationProvider();
		provider.setAuthenticationUserDetailsService((token) -> new User(token.getName(), null, ROLES_USUARIO_AUTENTICADO));
		provider.setTicketValidator(new Cas20ServiceTicketValidator(this.casTicketValidation));
		provider.setServiceProperties(this.serviceProperties);
		auth.authenticationProvider(provider);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		CasAuthenticationFilter filter = new CasAuthenticationFilter();
		filter.setServiceProperties(this.serviceProperties);
		filter.setAuthenticationManager(authenticationManager());
		http.addFilter(filter);
		CasAuthenticationEntryPoint entryPoint = new CasAuthenticationEntryPoint();
		entryPoint.setLoginUrl(this.casLoginPage);
		entryPoint.setServiceProperties(this.serviceProperties);
		http.exceptionHandling().authenticationEntryPoint(entryPoint);
		http.csrf().disable();
	}

	public static void main(String[] args) {
		SpringApplication.run(UnjExemploApplication.class, args);
	}

}
