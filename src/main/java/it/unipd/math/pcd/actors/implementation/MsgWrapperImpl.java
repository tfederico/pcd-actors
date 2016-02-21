package it.unipd.math.pcd.actors.implementation;

import it.unipd.math.pcd.actors.ActorRef;
import it.unipd.math.pcd.actors.Message;

/**
 * Created by federico on 10/02/16.
 */
public final class MsgWrapperImpl<T extends Message> implements MsgWrapper<T> {

    /**
     * Implements the interface MsgWrapper
     */

    //message to wrap
    private final T message;
    //sender of the message
    private final ActorRef<T> sender;

    public MsgWrapperImpl(T m, ActorRef<T> s){
        message = m;
        sender = s;
    }

    /**
     * Returns the sender of message
     * @return ActorRef the ActorRef of the Actor sender
     */
    @Override
    public ActorRef<T> getSender(){
        return sender;
    }

    /**
     * Returns the message into the wrapper
     * @return T message of type T (subtype of Message)
     */
    @Override
    public T getMessage(){
        return message;
    }



    /**
     * No setter methods since fields of the class
     * are final
     */
}
