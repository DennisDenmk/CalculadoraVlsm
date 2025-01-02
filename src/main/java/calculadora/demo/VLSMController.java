package calculadora.demo;
import calculadora.demo.Model.CalculoRequest;
import calculadora.demo.Model.VLSM;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class VLSMController {

    @GetMapping("/")
    public String showForm() {
        return "vlsmForm";
    }

    @GetMapping("/calculate")
    public String mostrarFormulario() {
        return "vlsmForm"; // Devuelve la vista del formulario
    }

    @PostMapping("/calculate")
    public String CalcularRedes(@RequestParam Map<String, String> parametros,
            @ModelAttribute CalculoRequest request, Model model) {
    
        // Crear una lista de hosts a partir de los parámetros generados dinámicamente
        List<Integer> hostsPorSubred = parametros.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith("lan")) // Filtramos las claves que empiezan con "lan"
                .map(entry -> Integer.parseInt(entry.getValue())) // Convertimos los valores a enteros
                .collect(Collectors.toList());
    
        // Crear instancia de VLSM utilizando los datos del request
        VLSM vlsmService = new VLSM(request.getIpBase(), request.getMascara(), hostsPorSubred);
       
        if (vlsmService.puedeCrearRedes()) {
            // Si se pueden crear redes, agregar el resultado al modelo
            vlsmService.obteneredes();
            model.addAttribute("result", vlsmService.ImprimirVlsm());
            model.addAttribute("redes",vlsmService.ImprimirRedesValidas());
            return "fragments/vlsmResult"; // Devuelve una vista llamada "resultado.html"
        }
        else{
            model.addAttribute("error", "La red padre no puede crear las suficientes redes");
            return "vlsmForm";
        }
        

    }
    

}
