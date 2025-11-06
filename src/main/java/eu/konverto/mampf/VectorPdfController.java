package eu.konverto.mampf;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/vector-pdf")
public class VectorPdfController {
    private final VectorStore vectorStore;

    public VectorPdfController(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    @GetMapping
    public String processPDF() {
        var reader = new PagePdfDocumentReader("sample.pdf");

        var splitter = new TokenTextSplitter();

        List<Document> documents = splitter.apply(reader.get());

        vectorStore.add(documents);

        return "processed " + documents.size() + " documents";
    }
}
