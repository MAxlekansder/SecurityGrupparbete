package org.example.securitygrupparbete.Configuration;

/**

csrf-tokenvalidering (Cross site Request Forgery) - vi implementerar en egen skräddarsydd
 csrfhantering i vårt program, och går bort från .csrf(Customizer.withDefaults()) för att
 förstå mer hur vi kan implementera lambdauttrycket, som ungefärligt fungerar som  ett
 functional interface. Lambdauttrycket låter oss skapa smidigare instanser där vi får allt
 direkt i metodanropet. Då vi kallar på csrf-token, skapar vi upp valida sessioner mellan
 klienten och servern för att stoppa falska förfrågningar får tredjepartsklienter.
 -- Alexander

 XSS-attacker (Cross-site Scripting) - vi konfigurerar våra HTTP-headers för att sätta upp
 en CSP (Content Security Policy). Det vi gör är att vi inkluderar och introducerar endast
 en regel som säger att vi endast kan köra script på vår sida som ligger inom samma domän.
 I enkelhet är det endast vår egna applikation som får köra scripts på vår sida.
 Då detta är skalbart, har vi möjligheten att inkludera andra hemsidor. Vi vitlistar dem
 och ger dem tillgång till att köra script som tredjepart. Varför detta är en nödvänding
 implementering har att göra med skyddet för scripts och datainjektioner som annars hade
 kunnat uppstå. Vi följer samma syntax som i csrf, med att använda sig av lambda för att
 hämta de nödvändiga metoderna, "Policy" är alltså vårt objekt vi hämtar in och kallar på
 dess metoder som vi vill nyttja och anpassa.
 -- Alexander

 Authorize specifika get-requests. Vi ser även till att vardera vy har sin begränsning
 för vilka som får respektive sida. Vi har då alltså satt upp, beroende på din roll som
 användare vart du får komma in. Du kan dock inte nå något om du inte är autentiserad.
 Det betyder att du kommer behöva logga in i första hand för att kunna nå de sidor som
 din behörighet tillåter. Vi tar in en mer universell lösning och tar det säkra före
 det osökra genom att begränsa vilka som faktiskt får göra en POST / DELETE / PUT
 där endast admin får göra dessa typer av förfrågningar. Utöver det har alla som är
 autentiserade möjlighet att nå de webbplatser som inte är begränsade till endast admin
 som t ex, register, adminpage, deleteUser, update etc.
 -- Alexander

 Loginfunktionen är inget speciellt, vi använder oss av Springs automagiska inloggningssida
 istället för att bygga upp en egen och hålla det i sin egen htmlsida. Vi skickar användaren
 vidare efter en lyckad inloggning till vår homepage, och möter användaren med dess roll.
 Vi använder oss av permitAll() för att låta alla ha möjligheten att logga in i första hand.
 Det blir vår första kontakt för användaren att nå vårt program.
 -- Alexander

 När vi loggar ut så ser vi till att rensa upp lite saker och se till att vi hanterar
 användarensutloggning korrekt. Vi går igenom ett par antal steg här. Vi använder oss
 av metodeninvalidateHttpSession som vi sätter till "true". försäkrar vi oss om session
 blir ogiltig när användaren loggar ut. En viktig del i att radera all sessiondata för
 användare, för att undvika kapningar. invalidateHttpSession är en viktig del av
 säkerhetsaspekten i vår applikation för stärka säkerheten. Tillslut rensar vi även
 cookies genom att radera JSESSIONID och XSRF-TOKEN. Vi försäkrar oss om att all information
 från användaren raderas för att upprätthålla säkerheten för applikationen efter en
 färdig session från vår användare. Den stora skillnaden mellan dessa två,
 .deleteCookies och .invalidateHttpSession är att de orienterar sig till varsin del
 av hela kedjan - .deleteCookies går mot klientsidan och .invaldiateHttpSession går mot
 serversidan. Vi erbjuder alltså en heltäckande och omfattande säkerhetslösning.
 -- Alexander



**/

import org.example.securitygrupparbete.Service.UserDetailsServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(SecurityConfiguration.class);


    @Bean
    public AuthenticationManager authenticationManager(UserDetailsServiceImpl userDetailsService) {
        var provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        LOG.warn("New provider in Auth Manager created", provider);
        return new ProviderManager(provider);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrfCustomizer ->                      // stoppar scripts från att sno cookien (withHttpOnlyFalse)
                        csrfCustomizer                       // nödvändig eftersom vi skickar forms från clientsidan till servern
                                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                )
                .authorizeHttpRequests(autRequest ->
                        autRequest
                                .requestMatchers("/").authenticated()
                              //  .requestMatchers("/logout").authenticated() verkar som att spring overridar säkerheten oavsett
                                .requestMatchers( "/admin"
                                                    , "/update"
                                                    , "/deleteUser"
                                                    , "/deleteUserResult"
                                                    , "/register")
                                .hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE).hasRole("ADMIN")
                                .requestMatchers(HttpMethod.POST).hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PUT).hasRole("ADMIN")
                                .anyRequest().authenticated()
                )
                .formLogin(formLogin ->
                        formLogin
                                .defaultSuccessUrl("/")
                                .permitAll()
                )
                .logout(logout ->
                        logout
                                .logoutUrl("/logout")
                                .logoutSuccessUrl("/logoutSuccess")
                                .invalidateHttpSession(true)
                                .deleteCookies("JSESSIONID", "XSRF-TOKEN")
                                .permitAll()
                )
                .headers(headers ->
                        headers
                                .contentSecurityPolicy(policy ->    // stoppar XSS-attacker och scripts utifrån med 'self'
                                        policy                      // vi kan tillåta betrodda sidor, genom att inkludera dem i policyDirectives
                                                .policyDirectives("script-src 'self'")
                                )
                )
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

}
