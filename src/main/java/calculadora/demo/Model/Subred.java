package calculadora.demo.Model;

public class Subred {
    private String direccionRed;
    private int[][] redBinario;
    private int mascara;
    private boolean utilizada;

    public boolean isUtilizada() {
        return utilizada;
    }

    public void setUtilizada(boolean utilizada) {
        this.utilizada = utilizada;
    }

    public int[][] getRedBinario() {
        return redBinario;
    }

    public Subred(String direccionRed, int mascara) {
        this.direccionRed = direccionRed;
        this.mascara = mascara;
        this.utilizada = false;
        this.redBinario = this.ipABinario();
    }

    public String getDireccionRed() {
        return direccionRed;
    }

    public int getMascara() {
        return mascara;
    }

    public boolean VerificarMascara() {
        int cont = 0;
        for (int i = 0; i < this.redBinario.length; i++) {
            for (int j = 0; j < this.redBinario[i].length; j++) {
                cont++;
                if (cont >= this.mascara - 1 && this.redBinario[i][j] != 0) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return "      " + direccionRed + ""
                + " /" + mascara + " ";

    }

    public int[][] ipABinario() {
        // Dividir la IP en sus cuatro octetos
        String[] octetos = this.direccionRed.split("\\.");

        // Crear un array para almacenar cada octeto en binario
        int[][] binario = new int[4][8];

        // Convertir cada octeto a binario
        for (int i = 0; i < octetos.length; i++) {
            int valor = Integer.parseInt(octetos[i]); // Convertir octeto a entero

            // Llenar cada posición del array con los bits del binario
            for (int j = 7; j >= 0; j--) {
                binario[i][j] = valor % 2; // Obtener el bit menos significativo
                valor /= 2; // Dividir el número por 2
            }
        }

        return binario;
    }

    public String ImprimirBinario() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < this.redBinario.length; i++) {
            for (int j = 0; j < this.redBinario[i].length; j++) {
                s.append(redBinario[i][j]);
            }
            if (i < this.redBinario.length - 1) {
                s.append("."); // Agregar punto solo entre octetos
            }
        }
        return s.toString();
    }
    public int[] binarioADecimalIP(){
        return binarioADecimalIP(this.ipABinario());
    }
    public int[] binarioADecimalIP(int[][] testbinario) {
        int[][] binario = testbinario;

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

    public int[] calcularBroadcast() {
        String ip = this.direccionRed;

        int[][] broadcast = new int[this.redBinario.length][this.redBinario[0].length];
        for (int i = 0; i < broadcast.length; i++) {
            for (int j = 0; j < broadcast[i].length; j++) {
                broadcast[i][j] =redBinario[i][j];
            }
        }
        int cont=0;
        for (int i = 0; i < broadcast.length; i++) {
            for (int j = 0; j < broadcast[i].length; j++) {
                if(cont>=this.mascara){
                    broadcast[i][j] = 1; 
                }
                cont++;
            }
        }
        return this.binarioADecimalIP(broadcast);
    }

    public int[] calcularPrimeraIP() {
        int[] primeraIP = this.binarioADecimalIP();
        primeraIP[3] += 1;

        // Manejar el carry (acarreo) si algún octeto supera 255
        for (int i = 3; i > 0; i--) {
            if (primeraIP[i] > 255) {
                primeraIP[i] = 0; // Reiniciar el octeto actual
                primeraIP[i - 1] += 1; // Propagar el carry al octeto anterior
            }
        }
        return primeraIP;

    }

    public int[] calcularUltimaIP() {
        int[] ultimaIP = this.calcularBroadcast();
        // Restar 1 a la dirección de broadcast para obtener la última IP
        ultimaIP[3] -= 1;

        // Ajustar los octetos si hay un acarreo negativo
        for (int i = 3; i > 0; i--) {
            if (ultimaIP[i] < 0) {
                ultimaIP[i] += 256;
                ultimaIP[i - 1] -= 1;
            }
        }

        // Construir la dirección en formato decimal
        return ultimaIP;
    }

    // Metodo convertidor arreglos a strings
    public String ConvertidorIaS(int[] aux) {
        String s = "";

        for (int i = 0; i < aux.length - 1; i++) {
            s += aux[i] + ".";
        }
        s += aux[3];
        return s;
    }

}
