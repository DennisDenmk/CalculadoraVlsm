package calculadora.demo.Model;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class VLSM {
    public Subred ipBase;
    private List<Integer> hostsPorSubred;
    private int indicehost;
    private List<Subred> redes;
    private int indiceredes;
    private List<String> nombresRed;

    public VLSM(){
    }

    public VLSM(String ipBase, int mascara, List<Integer> hostsPorSubred) {
        this.ipBase = new Subred(ipBase, mascara);
        this.hostsPorSubred = hostsPorSubred;
        this.redes = new ArrayList<>();
        this.redes.add(this.ipBase);
        this.nombresRed = new ArrayList<>();
        this.Ordenamiento();
        this.indicehost = 0;
        this.indiceredes = 0;
    }
    public String ImprimirRedesValidas(){
        String redesvalidas ="";
        int cont =0;
       for (Subred aux : redes) {
            if(aux.isUtilizada()){
                redesvalidas += nombresRed.get(cont)+" = "+aux.getDireccionRed()+" /"+aux.getMascara()+
                " Numero de host: "+ hostsPorSubred.get(cont)+"\n";
                cont++;
            }
           
       }
        return redesvalidas;
        
    }
    private void Ordenamiento(){

        for (int i = 0; i < this.hostsPorSubred.size(); i++) {
            this.nombresRed.add("Lan" + (i + 1));
        }

        if (hostsPorSubred == null || hostsPorSubred.isEmpty()) {
            return;
        }

        int n = hostsPorSubred.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                // Cambiar la comparación para ordenar de mayor a menor
                if (hostsPorSubred.get(j) < hostsPorSubred.get(j + 1)) {
                    // Intercambiar elementos en hostsPorSubred
                    int temp = hostsPorSubred.get(j);
                    hostsPorSubred.set(j, hostsPorSubred.get(j + 1));
                    hostsPorSubred.set(j + 1, temp);

                    // Intercambiar nombres correspondientes en nombresRed
                    String temp2 = nombresRed.get(j);
                    nombresRed.set(j, nombresRed.get(j + 1));
                    nombresRed.set(j + 1, temp2);
                }
            }
        }
            
    }

    public boolean obteneredes() {
        if (this.puedeCrearRedes()) {
            Subred prueba = this.redes.get(indiceredes);
            this.sumarbit(prueba);
            while (this.indicehost < (this.hostsPorSubred.size())) {
                prueba = this.redes.get(indiceredes);

                if (this.MascaraRequerida(this.hostsPorSubred.get(indicehost)) == prueba.getMascara()) {
                    prueba.setUtilizada(true);
                    this.indiceredes++;
                    this.indicehost++;
                } else {
                    this.sumarbit(prueba);
                    this.indiceredes++;
                }
            }
            if (redes.size() >= 2) {
                redes.remove(redes.size() - 1); // Elimina el último elemento
                redes.remove(redes.size() - 1); // Elimina el penúltimo elemento
            }
            
            return true;
        }
        return false;
    }

    public boolean puedeCrearRedes() {
        // Calcular el total de direcciones disponibles en la IP base
        int direccionesDisponibles = (int) Math.pow(2, 32 - this.ipBase.getMascara());

        // Calcular el total de direcciones requeridas
        int direccionesRequeridas = 0;
        for (int hosts : hostsPorSubred) {
            // Cada red requiere al menos 2 direcciones adicionales (red y broadcast)
            int hostsReales = hosts + 2;
            int bitsNecesarios = 0;

            // Calcular los bits necesarios para soportar los hostsReales
            while (Math.pow(2, bitsNecesarios) < hostsReales) {
                bitsNecesarios++;
            }

            // Agregar el total de direcciones requeridas para esta red
            direccionesRequeridas += (int) Math.pow(2, bitsNecesarios);
        }

        // Comparar direcciones disponibles con direcciones requeridas
        return direccionesRequeridas <= direccionesDisponibles;
    }

    public boolean sumarbit(Subred ipBinario) {
        int[][] arreglo = new int[ipBinario.getRedBinario().length][ipBinario.getRedBinario()[0].length];
        for (int i = 0; i < arreglo.length; i++) {
            for (int j = 0; j < arreglo[i].length; j++) {
                arreglo[i][j] = ipBinario.getRedBinario()[i][j];
            }
        }
        // Nuevas redes creadas
        Subred red0, red1;
        int nuevaMascara = ipBinario.getMascara() + 1;
        // Bit a modificar
        int byteIndex = 0;
        int aux = 0;
        if (nuevaMascara >= 0 && nuevaMascara <= 8) {
            aux = nuevaMascara;
        } else if (nuevaMascara > 8 && nuevaMascara <= 16) {
            byteIndex = 1;
            aux = nuevaMascara - 8;
        } else if (nuevaMascara > 16 && nuevaMascara <= 24) {
            byteIndex = 2;
            aux = nuevaMascara - 16;
        } else {
            byteIndex = 3;
            aux = nuevaMascara - 24;
        }

        arreglo[byteIndex][aux - 1] = 1;
        int[] red1Binario = this.binarioADecimalIP(arreglo);
        // Creacion de las 2 redes
        red0 = new Subred(ipBinario.getDireccionRed(), nuevaMascara);
        red1 = new Subred(red0.ConvertidorIaS(red1Binario), nuevaMascara);
        redes.add(this.indiceredes + 1, red0);
        redes.add(this.indiceredes + 2, red1);
        return true;
    }

    public String ImprimirVlsm() {
        String s =  "";
        String lan = "";
        int cont=0;
        for (Subred elemento : redes) {
            if(elemento.isUtilizada()){
                lan = "=>"+nombresRed.get(cont);
                cont++;
            }
            else{
                lan="";
            }
            s += this.saltoImprimir(elemento) + elemento.ImprimirBinario()
            +elemento+lan+"\n";
        }
        return s;
    }

    private String saltoImprimir(Subred red) {
        int aux = this.ipBase.getMascara();
        String saltos = "";

        for (int i = aux; i < red.getMascara(); i++) {
            saltos += " ";
        }
        return saltos;
    }

    public int[] binarioADecimalIP(int[][] binario) {
        // Verificar que la matriz sea válida (4 filas y 8 columnas)
        if (binario.length != 4 || binario[0].length != 8) {
            throw new IllegalArgumentException("El array debe ser de tamaño 4x8.");
        }

        // Crear un arreglo para almacenar los octetos decimales
        int[] ipDecimal = new int[4];

        for (int i = 0; i < binario.length; i++) {
            int decimal = 0;

            // Convertir cada octeto de binario a decimal
            for (int j = 0; j < binario[i].length; j++) {
                decimal += binario[i][j] * Math.pow(2, 7 - j); // 7 - j para posiciones decrecientes
            }

            // Almacenar el valor decimal en el arreglo
            ipDecimal[i] = decimal;
        }

        return ipDecimal;
    }

    // Metodo para calcular la mascara que se requiere para cumplir con los hosts
    private int MascaraRequerida(int hosts) {
        for (int i = 1; i < 30; i++) {
            if (hosts <= (Math.pow(2, i) - 2)) {
                return (32 - i);
            }
        }
        return 0;
    }

    public String toString(int[][] binario) {
        String s = "";
        for (int i = 0; i < binario.length; i++) {
            for (int j = 0; j < binario[i].length; j++) {
                s += binario[i][j] + "";
            }
            if (i != binario.length) {
                s += ".";
            }
        }
        return s;

    }

}
