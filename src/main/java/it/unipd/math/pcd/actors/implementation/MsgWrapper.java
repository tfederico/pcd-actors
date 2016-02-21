package it.unipd.math.pcd.actors.implementation;

import it.unipd.math.pcd.actors.ActorRef;
import it.unipd.math.pcd.actors.Message;

/**
 * Created by federico on 10/02/16.
 */
public interface MsgWrapper <T extends Message>{

    /**
     * Interface used for the mailbox.
     * This interface defines the functionality to link a message
     * and it's sender.
     * Implementing it, it's possible to associate a sender (ActorRef) to a message.
     */

    /**
     * The method returns the Message
     * @return T, subtype of Message
     */
    T getMessage();

    /**
     * The method returns the sender of the Message (Mail)
     * @return ActorRef of type T
     */
    ActorRef<T> getSender();
}
