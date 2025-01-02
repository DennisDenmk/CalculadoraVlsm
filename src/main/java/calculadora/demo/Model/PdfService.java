package calculadora.demo.Model;

import org.xhtmlrenderer.pdf.ITextRenderer;

import com.lowagie.text.DocumentException;

import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class PdfService {

    public byte[] generatePdfFromHtml(String htmlContent) throws IOException, DocumentException {
        // Crear un renderer para convertir HTML a PDF
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(htmlContent);
        renderer.layout();

        // Crear un byte array para almacenar el PDF generado
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        renderer.createPDF(outputStream);

        // Devolver el archivo PDF en formato byte array
        return outputStream.toByteArray();
    }
}