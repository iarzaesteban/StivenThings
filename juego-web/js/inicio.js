//ctrl + f5 -- recarga pag limpiando la cache



document.addEventListener('DOMContentLoaded', function() {
    inicio.iniciarJuego();
}, false);


var inicio = {

    iniciarJuego: function(){
        console.log("Juego Iniciado");
        teclado.iniciar();
        dimensiones.iniciar();
        mando.iniciar();
        inicio.recargarTiles();
        
        //var r = new Rectangulo(10,10,100,100);
        mainPrincipal.iterar(); 
    },

    recargarTiles: function(){
        document.getElementById("juego").innerHTML= "";

        for(var y = 0; y < dimensiones.obtenerTilesVerticales(); y++){    
            for(var x = 0 ; x <  dimensiones.obtenerTilesHorizontales(); x++){
                var r = new Rectangulo(x * dimensiones.ladoTiles, y * dimensiones.ladoTiles,dimensiones.ladoTiles,dimensiones.ladoTiles);
                
            }
        }
    }
};