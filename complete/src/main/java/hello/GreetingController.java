package hello;


import java.io.*;
import java.util.concurrent.atomic.AtomicLong;


import hello.storage.StorageService;
import org.eclipse.rdf4j.model.*;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.model.impl.TreeModel;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import org.eclipse.rdf4j.model.vocabulary.FOAF;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;

import java.io.IOException;
import java.io.InputStream;


@RestController
public class GreetingController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    private final StorageService storageService;

    @Autowired
    public GreetingController(StorageService storageService) {
        this.storageService = storageService;
    }


    @PostMapping("/greeting")
    public Greeting handleFileUpload(@RequestParam("file") MultipartFile file,
                                     RedirectAttributes redirectAttributes) throws IOException {
        this.getNamedGraph();
        storageService.store(file);
//        this.testing(file);
//        this.ex(file);
//        redirectAttributes.addFlashAttribute("message",
//                "You successfully uploaded " + file.getOriginalFilename() + "!");

        return new Greeting(counter.incrementAndGet(),
                String.format(template, "name"));
    }

    void getNamedGraph() throws IOException {
//        FileOutputStream fop = null;
        File file2;
//        // We'll use a ModelBuilder to create two named graphs, one containing data about
//        // picasso, the other about Van Gogh.
//        ModelBuilder builder = new ModelBuilder();
//        builder.setNamespace("ex", "http://example.org/");
//
//        // in named graph 1, we add info about Picasso
//        builder.namedGraph("ex:namedGraph1")
//                .subject("ex:Picasso")
//                .add(RDF.TYPE, "artist")
//                .add(FOAF.FIRST_NAME, "Pablo");
//
//        // in named graph2, we add info about Van Gogh.
//        builder.namedGraph("ex:namedGraph2")
//                .subject("ex:VanGogh")
//                .add(RDF.TYPE, "artist")
//                .add(FOAF.FIRST_NAME, "Vincent");
//
//
//        // We're done building, create our Model
//        Model model = builder.build();
        file2 = new File(System.getProperty("user.dir") + "\\src\\main\\resources\\" +"resources.rdf");
//        fop = new FileOutputStream(file2);
//
//        Rio.write(model, fop, RDFFormat.RDFXML);

        InputStream input =new FileInputStream(file2);

        Model model = Rio.parse(input, "", RDFFormat.RDFXML);
        for (Statement statement: model) {
            System.out.println(statement);
        }
    }

    void ex(MultipartFile file) throws IOException {
        String fpath = System.getProperty("user.dir") + "\\upload-dir\\" + file.getOriginalFilename();
        // read the file 'example-data-artists.ttl' as an InputStream.
        InputStream input =new FileInputStream(fpath);

//                GreetingController.class.getResourceAsStream("/" + fpath);

        // Rio also accepts a java.io.Reader as input for the parser.
        try {
            Model model = Rio.parse(input, "", RDFFormat.RDFXML);
        }

        // To check that we have correctly read the file, let's print out the model to the screen again
        for (Statement statement: model) {
            System.out.println(statement);
        }

    }

    void testing(MultipartFile file) throws IOException {
        String fpath = System.getProperty("user.dir") + "\\upload-dir\\" + file.getOriginalFilename();
        FileOutputStream fop = null;
        File file2;
        // We'll use a ModelBuilder to create two named graphs, one containing data about
        // picasso, the other about Van Gogh.
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
        file2 = new File(System.getProperty("user.dir") + "\\upload-dir\\" +"newfile.txt");
        fop = new FileOutputStream(file2);

        Rio.write(model, fop, RDFFormat.RDFXML);

//        try {
//
//            file2 = new File(System.getProperty("user.dir") + "\\upload-dir\\" +"newfile.txt");
//            fop = new FileOutputStream(file2);
//
//            // each named graph is stored as a separate context in our Model
//            for (Resource context : model.contexts()) {
//                System.out.println("Named graph " + context + " contains: ");
//
//                // write _only_ the statemements in the current named graph to the console, in N-Triples format
//                Rio.write(model.filter(null, null, null, context), fop, RDFFormat.NTRIPLES);
//                System.out.println();
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (fop != null) {
//                    fop.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }



    }


    void printer(MultipartFile file) throws IOException {
//        String fpath = System.getProperty("user.dir") + "\\upload-dir\\" + file.getOriginalFilename();
//        System.out.println("Present Project Directory : " + fpath);
//
//        FileInputStream fin = new FileInputStream(fpath);
//        Model model = Rio.parse(fin, "", RDFFormat.RDFXML);
//        ValueFactory vf = SimpleValueFactory.getInstance();


    }

}
