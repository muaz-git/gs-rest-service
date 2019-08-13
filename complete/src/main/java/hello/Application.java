package hello;

import hello.storage.StorageProperties;
import hello.storage.StorageService;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.model.vocabulary.FOAF;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import hello.storage.StorageProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.CommandLineRunner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class Application {

    public static void main(String[] args) throws FileNotFoundException {

        SpringApplication.run(Application.class, args);
//        Application.dummyXML();
    }

    private static void dummyXML() throws FileNotFoundException {
        ModelBuilder builder = new ModelBuilder();
        builder.setNamespace("ex", "http://example.org/");

        // in named graph 1, we add info about Picasso
        builder.namedGraph("ex:namedGraph1")
                .subject("ex:Picasso")
                .add(RDF.TYPE, "artist")
                .add(FOAF.FIRST_NAME, "Pablo");

        // in named graph2, we add info about Van Gogh.
        builder.namedGraph("ex:namedGraph2")
                .subject("ex:VanGogh")
                .add(RDF.TYPE, "artist")
                .add(FOAF.FIRST_NAME, "Vincent");


        // We're done building, create our Model
        Model model = builder.build();

        File file2 = new File(System.getProperty("user.dir") + "\\src\\main\\resources\\" + "resources.rdf");

        FileOutputStream fop = new FileOutputStream(file2);
        Rio.write(model, fop, RDFFormat.RDFXML);
    }
    @Bean
    CommandLineRunner init(StorageService storageService) {
        return (args) -> {
            storageService.deleteAll();
            storageService.init();
        };
    }
}
