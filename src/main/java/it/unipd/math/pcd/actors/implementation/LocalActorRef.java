package it.unipd.math.pcd.actors.implementation;
import it.unipd.math.pcd.actors.*;

/**
 * Created by federico on 09/02/16.
 */
public class LocalActorRef <T extends Message> extends AbsActorRef<T>{

    public LocalActorRef(ActorSystem s){
        super(s);
    }

    /**
     * Messages that should be sent are added into mailbox of the receiver.
     * To keep a reference of the sender it creates a MailImpl: this class
     * is meant to be a wrapper for an ActorRef and a Message, so it's possible
     * to link a message to it's sender.
     * @param message The message to send
     * @param to The actor to which sending the message
     */

    @Override
    public void send(T message, ActorRef to){
        ((AbsActor<T>)system.findActor(to)).addInMailbox(new MsgWrapperImpl<T>(message, this));
    }

}
