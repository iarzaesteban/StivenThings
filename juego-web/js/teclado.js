
var teclado = {

    teclas: new Array(),   // 0,1,2,3,4.....

    iniciar: function() {
        document.onkeydown = teclado.guardarTecla;
    },

    guardarTecla: function(e){
        teclado.teclas.push(e.key);
        console.log("tecla : " + e.key);
    },

    teclaPulsada: function(codigoTecla){
        return (teclado.teclas.indexOf(codigoTecla) !== -1) ? true: false;
    },
    
    reiniciar: function(){
        teclado.teclas = new Array();

    }
};