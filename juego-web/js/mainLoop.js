//aps = actualizaciones x seg ---- generalmente se actualiza 60 veces x seg
//fps = frame por segundos--- generalmente se dibuja 60 veces x seg
//1 seg = 1000ms


var mainPrincipal = {
    idEjecucion: null,
    ultimoRegistro: 0,
    aps: 0,
    fps: 0,
    iterar: function(registroTemporal){
        mainPrincipal.idEjecucion = window.requestAnimationFrame(mainPrincipal.iterar);

        mainPrincipal.actualizar(registroTemporal);
        mainPrincipal.dibujar(); 

        if (registroTemporal - mainPrincipal.ultimoRegistro > 999){
            mainPrincipal.ultimoRegistro = registroTemporal;
            console.log("APS: " + mainPrincipal.aps +  "| FPS: " + mainPrincipal.fps);
            mainPrincipal.aps = 0;
            mainPrincipal.fps = 0;

        }

    },
    detener: function(){

    },

    actualizar: function(registroTemporal){
        teclado.reiniciar();
        mando.actualizar();
        mainPrincipal.aps++;
    },

    dibujar: function(registroTemporal){
        mainPrincipal.fps++;
    }
};