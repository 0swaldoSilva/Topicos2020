package sample.events;

import javafx.event.Event;
import javafx.event.EventHandler;

public class EventosMemorama implements EventHandler {

    int opcion;

    public EventosMemorama(int opc){
        opcion = opc;
    }

    @Override
    public void handle(Event event) {
        //System.out.println("Mi segundo evento desde otra clase");
        /*if (opcion == 1){
            System.out.println("Presionaste el primer botón");
        }
        else{
            System.out.println("Presionaste el segundo botón");
        }*/



    }

}
