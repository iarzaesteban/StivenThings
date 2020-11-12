
var mando = {

    objeto: null,
    eventosDisponibles: 'ongamepadconnected' in window,
    conectado: false,
    
    iniciar: function(){
        if(mando.eventosDisponibles){
            window.addEventListener("gamepadconnected", mando.conectar);
            window.addEventListener("gamepaddisconncted", mando.desconectar);
        }else{
            mando.actualizar();
        }
    },

    conectar: function(evento){
        mando.objeto = evento.gamepad;
        mando.identificar();
    },

    desconectar: function(){
        console.log("el mando se ha desconectado del indice %d: %s.", evento.gamepad.index, evento.gamepad.id);
    },

    actualizar: function(){
        if(!mando.eventosDisponibles){
            mandos = null;
            try{
                mandos = navigator.getGamepads ? navigator.getGamepads() : (navigator.webkitGetGamepads ? navigator.webkitGetGamepads : []);
                mando.objeto = mandos[0];

                if(!mando.conectado){
                    mando.conectado = true;
                    ("mando.identificar();", 500);
                    //mando.identificar();
                }
            }catch(exception){
                console.log(exception.message);
            }
           
        }

        if(!mando.objeto){
            return;
        }

        if(mando.botonPulsado(mando.objeto.buttons[0])){
            console.log("Mando : A");
        }
    },

    botonPulsado: function(boton){
        if(typeof(boton) == "object"){
            return boton.pressed;
        }
        return boton == 1.0;
    },

    identificar: function(){
        console.log("vieneE????");
        console.log("Mando conectado en el indice %d: %s. %d buttons, %d axes", 
        mando.objeto.index, mando.objeto.id, mando.objeto.buttons.length, mando.objeto.axes.length);

    }
};