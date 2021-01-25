package curso.api.spring.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class SpringrestapiApplication implements WebMvcConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(SpringrestapiApplication.class, args);
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

        /**
        registry.addMapping("/users/**")
                .allowedMethods("POST", "PUT", "DELETE", "GET")
                .allowedOrigins("*");

        **
        /** liberando acesso a um endpoint específico
        registry.addMapping("/users/{id}");
        **/
    }
}
