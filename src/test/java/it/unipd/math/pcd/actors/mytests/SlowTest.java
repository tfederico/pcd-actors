package it.unipd.math.pcd.actors.mytests;

import it.unipd.math.pcd.actors.ActorSystem;
import it.unipd.math.pcd.actors.TestActorRef;
import it.unipd.math.pcd.actors.utils.ActorSystemFactory;
import it.unipd.math.pcd.actors.utils.actors.SlowActor;
import it.unipd.math.pcd.actors.utils.messages.SlowMessage;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by federico on 11/02/16.
 */
public class SlowTest {
    private ActorSystem system;

    /**
     * Initializes the {@code system} with a concrete implementation before each test.
     */
    @Before
    public void init() {
        system = ActorSystemFactory.buildActorSystem();
    }

    /**
     * Inner class used to check if after an amount of time (waitTime) a SlowActor (actor)
     * has received a SlowMessage.
     * This version check if the mailbox is empty, so the waitTime should be
     * minor of the time necessary to receive the message
     */
    private class CheckSlowdown implements Runnable{

        private SlowActor actor;
        private long waitTime;

        public CheckSlowdown(SlowActor actor, long waitTime){
            this.actor=actor;
            this.waitTime=waitTime;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(waitTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Assert.assertEquals("The mailbox is empty (in this moment)", true, actor.isEmptyMailBox());

        }
    }

    @Test
    public void shouldSlowDownButReceiveTheMessage(){
        TestActorRef ref = new TestActorRef(system.actorOf(SlowActor.class));
        SlowActor actor = (SlowActor) ref.getUnderlyingActor(system);
        //checking if after 500ms there's a message (it shouldn't)
        Thread t=new Thread(new CheckSlowdown(actor,500));
        t.start();
        //creation of a SlowMessage takes 1000ms
        ref.send(new SlowMessage(1000), ref);
        //now the mailbox should contain the SlowMessage
        Assert.assertEquals("The mailbox contains a message", false, actor.isEmptyMailBox());

    }
}
