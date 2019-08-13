package hello;


import java.io.*;
import java.util.concurrent.atomic.AtomicLong;


import hello.storage.StorageService;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Statement;

import java.io.IOException;
import java.io.InputStream;


@RestController
public class GreetingController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    private final StorageService storageService;
    private File repo = new File(System.getProperty("user.dir") + "\\src\\main\\resources\\" + "resources.rdf");

    @Autowired
    public GreetingController(StorageService storageService) {
        this.storageService = storageService;
    }


    @PostMapping("/greeting")
    public Greeting handleFileUpload(@RequestParam("file") MultipartFile file,
                                     RedirectAttributes redirectAttributes) throws IOException {

        storageService.store(file); // saves the incoming file
        Model curr_model = this.getModelCurrentFile(file); // loads RDF model of incoming file in to java object.
        this.updateRepo(curr_model); // Update the database with current object with the previous ones.

        return new Greeting(counter.incrementAndGet(),
                String.format(template, "name"));
    }

    private void updateRepo(Model currModel) throws IOException {
        ModelBuilder builder = new ModelBuilder();
        builder.setNamespace("ex", "http://example.org/");

        for (Statement statement : currModel) {
            builder.namedGraph("ex:namedGraph1")
                    .subject(statement.getSubject())
                    .add(statement.getPredicate(), statement.getObject());
        }

        if (this.repo.exists()) {
            // if resource exist, just append new results to them.

            InputStream input = new FileInputStream(this.repo);
            Model rootModel = Rio.parse(input, "", RDFFormat.RDFXML);

            for (Statement statement : rootModel) {
                builder.namedGraph("ex:namedGraph1")
                        .subject(statement.getSubject())
                        .add(statement.getPredicate(), statement.getObject());
            }
        }

        FileOutputStream fop = new FileOutputStream(this.repo);
        Model model = builder.build();
        Rio.write(model, fop, RDFFormat.RDFXML);
    }

    private Model getModelCurrentFile(MultipartFile file) throws IOException {
        String fpath = System.getProperty("user.dir") + "\\upload-dir\\" + file.getOriginalFilename();
        InputStream input = new FileInputStream(fpath);
        return Rio.parse(input, "", RDFFormat.RDFXML);
    }

}
