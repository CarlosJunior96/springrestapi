package curso.api.spring.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EnableCaching
public class SpringrestapiApplication implements WebMvcConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(SpringrestapiApplication.class, args);
        System.out.println(new BCryptPasswordEncoder().encode("123"));
    }

    /** CENTRALIZADO ACESSO CROSSORIGIN A API**/
    @Override
    public void addCorsMappings(CorsRegistry registry) {

        /** liberando acesso a todos os controles e end points **/
       // registry.addMapping("/**");

        /** liberando apenas os endpoints de um controller específico **/

        /** o padrão do allowedMethos("*") que libera para todos os métodos HTTP, porém posso especificar
         *  qual método desejo **/

        /** o padrão do allowedOrigins("*") é o servidor de origem que faz requisição a api, pode ser especificado 1 ou mais
         *  servidores que poderão ter acesso a o método específico **/


        registry.addMapping("/usuario/**")
                .allowedMethods("*")
                .allowedOrigins("*");


        /** liberando acesso a um endpoint específico
        registry.addMapping("/users/{id}");
        **/
    }
}
