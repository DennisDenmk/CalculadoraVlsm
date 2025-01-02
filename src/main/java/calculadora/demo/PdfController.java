package calculadora.demo;

import calculadora.demo.Model.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.lowagie.text.DocumentException;

import org.springframework.stereotype.Controller;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class PdfController {

    @Autowired
    private PdfService pdfService;

    @PostMapping("/generate-pdf")
    public ResponseEntity<byte[]> generatePdf(@RequestParam String result, @RequestParam String redes, HttpServletResponse response) throws DocumentException {
        try {
            // Convertir los datos a HTML para el PDF
            String htmlContent = "<html><body>"
                    + "<h1>Resultado del Cálculo VLSM</h1>"
                    + "<h2>Redes Resultantes:</h2>"
                    + "<pre>" + redes + "</pre>"
                    + "<h2>Proceso:</h2>"
                    + "<pre>" + result + "</pre>"
                    + "</body></html>";

            // Llamar al servicio para generar el PDF
            byte[] pdfBytes = pdfService.generatePdfFromHtml(htmlContent);

            // Establecer los encabezados para la descarga del archivo PDF
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/pdf");
            headers.add("Content-Disposition", "attachment; filename=result_vlsm.pdf");

            // Devolver el PDF como un archivo en la respuesta
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build(); // En caso de error
        }
    }
}
