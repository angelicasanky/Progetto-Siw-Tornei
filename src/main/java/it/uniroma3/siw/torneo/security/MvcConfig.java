package it.uniroma3.siw.torneo.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    /**
     * Configura un handler per le risorse statiche caricate dagli utenti.
     * Mappa tutti gli URL che iniziano con {@code /uploads/} alla cartella fisica
     * {@code uploads/} nella directory di lavoro dell'applicazione.
     * Questo permette di servire immagini e loghi caricati tramite il form
     * (es. foto giocatori, loghi squadre, immagini tornei).
     *
     * @param registry il registro a cui aggiungere i gestori di risorse
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Mappa gli URL che iniziano con /uploads/ alla cartella fisica "uploads"
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
    }
}
