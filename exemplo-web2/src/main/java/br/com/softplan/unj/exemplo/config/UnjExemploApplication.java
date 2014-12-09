package br.com.softplan.unj.exemplo.config;

import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.validation.Cas20ServiceTicketValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
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

import javax.annotation.PostConstruct;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableAutoConfiguration
@ComponentScan("br.com.softplan.unj")
@EnableGlobalMethodSecurity(jsr250Enabled = true)
public class UnjExemploApplication extends WebSecurityConfigurerAdapter {

    private static final String ROLE_ACESSO_SERVICO_RESTRITO = "ACESSO_SERVICO_RESTRITO";
	public static final List<GrantedAuthority> ROLES_USUARIO_AUTENTICADO_AUTHORITIES = Arrays.asList(new SimpleGrantedAuthority("ACESSO_SERVICO_RESTRITO"));

	@Value("${cas.loginpage}")
	private String casLoginPage;
	@Value("${cas.ticketvalidation}")
	private String casTicketValidation;
	@Value("${cas.serviceurl}")
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
		provider.setAuthenticationUserDetailsService((token) -> new User(token.getName(), null, ROLES_USUARIO_AUTENTICADO_AUTHORITIES));
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
        http.addFilterAfter(new SingleSignOutFilter(), filter.getClass());
		CasAuthenticationEntryPoint entryPoint = new CasAuthenticationEntryPoint();
		entryPoint.setLoginUrl(this.casLoginPage);
		entryPoint.setServiceProperties(this.serviceProperties);
		http.exceptionHandling().authenticationEntryPoint(entryPoint);
		http.csrf().disable();
        configurarAcesso(http);
    }

    /**
     * Exemplo de configuracao para exigir que acesso a todos endpoints exijam role ROLE_ACESSO_SERVICO_RESTRITO. Ver UnjExemploEndpoint.testarServidor para
     * ver exemplo da mesma configuração mas com anotações
     *
     * @see http://docs.spring.io/autorepo/docs/spring-security/4.0.0.CI-SNAPSHOT/reference/htmlsingle/#authorize-requests
     * @param http
     * @throws Exception
     */
    private void configurarAcesso(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/**").hasRole(ROLE_ACESSO_SERVICO_RESTRITO);
    }

    public static void main(String[] args) {
		SpringApplication.run(UnjExemploApplication.class, args);
	}

}
