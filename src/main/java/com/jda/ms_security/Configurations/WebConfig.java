package com.jda.ms_security.Configurations;

import com.jda.ms_security.Interceptors.SecurityInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
   public class WebConfig implements WebMvcConfigurer {  //Esta clase es para activar la clase de los interceptores
    @Autowired
    private SecurityInterceptor securityInterceptor; //El policia

 @Override
    public void addInterceptors(InterceptorRegistry registry) {  //Parese en tal parte, SI


        registry.addInterceptor(securityInterceptor) //Capa de cebolla, para que no este tan expuesto
                .addPathPatterns("/api/**")  //Para endpoints, proteger.
                .excludePathPatterns("/api/public/**"); //todo lo que termina en eso lo analiza el policia. aca se excluye lo publico.


    }
}//De esta manera podemos desactivar la seguridad de la aplicacion.