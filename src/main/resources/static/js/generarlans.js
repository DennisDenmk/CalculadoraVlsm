

function generarLANs() {
    const numeroDeLANs = document.getElementById("numeroDeLANs").value;
    const contenedor = document.getElementById("lanContainer");
    contenedor.innerHTML = ""; // Limpia las casillas anteriores

    for (let i = 1; i <= numeroDeLANs; i++) {
        const label = document.createElement("label");
        label.textContent = `LAN${i} (Número de hosts):`;
        label.htmlFor = `lan${i}`;

        const input = document.createElement("input");
        input.type = "number";
        input.id = `lan${i}`;
        input.name = `lan${i}`;
        input.min = "1";
        input.required = true;

        contenedor.appendChild(label);
        contenedor.appendChild(input);
        contenedor.appendChild(document.createElement("br"));
    }
}