package it.unipd.math.pcd.actors.implementation;

import it.unipd.math.pcd.actors.AbsActorSystem;
import it.unipd.math.pcd.actors.ActorRef;
import it.unipd.math.pcd.actors.ActorSystem;
import it.unipd.math.pcd.actors.Message;

/**
 * Created by federico on 09/02/16.
 */
public abstract class AbsActorRef <T extends Message> implements ActorRef<T> {

    protected final AbsActorSystem system;

    public AbsActorRef(ActorSystem s){
        system = (AbsActorSystem) s;
    }

    /**
     * Method overriden from the class Object
     * @param actorRef An ActorRef to an Actor
     * @return 0 if this is equal to actorRef, -1 otherwise
     */
    @Override
    public int compareTo(ActorRef actorRef) {
        if(this == actorRef)
            return 0;
        else
            return -1;
    }

    /**
	 * This method runs tasks only on the CachedThreadPool
     * of the actor system
     * @param r Runnable that the system should start
     */
    public void AbsActorRefExecute(Runnable r){
        system.systemExecute(r);
    }


}
