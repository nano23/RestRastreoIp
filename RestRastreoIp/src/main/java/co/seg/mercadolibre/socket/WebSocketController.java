package co.seg.mercadolibre.socket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

/**
 * controlador para el manejo del web socket
 * @author Carlos Gomez
 *
 */
@Controller
public class WebSocketController {
	
    private final SimpMessagingTemplate template;

    @Autowired
    WebSocketController(SimpMessagingTemplate template){
        this.template = template;
    }

    /**
     * Envio de mensaje a sessiones activas
     * @param message cuerpo del mensaje tipo json
     */
    @MessageMapping("/send/message")
    public void sendMessage(String message){
        this.template.convertAndSend("/message",  message);
    }

}
