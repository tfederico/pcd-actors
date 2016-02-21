package it.unipd.math.pcd.actors.mytests;

import it.unipd.math.pcd.actors.ActorSystem;
import it.unipd.math.pcd.actors.TestActorRef;
import it.unipd.math.pcd.actors.utils.ActorSystemFactory;
import it.unipd.math.pcd.actors.utils.actors.StoreActor;
import it.unipd.math.pcd.actors.utils.messages.StoreMessage;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by federico on 12/02/16.
 */
public class HeavyTest {

    private ActorSystem system;

    /**
     * Initializes the {@code system} with a concrete implementation before each test.
     */
    @Before
    public void init() {
        system = ActorSystemFactory.buildActorSystem();
    }

    /**
     * Inner class used to spam an actor
     */
    private class HeavySpammer implements Runnable{

        private TestActorRef ref;
        private long numOfMessages;

        public HeavySpammer(TestActorRef ref, long numOfMessages) {
            this.ref = ref;
            this.numOfMessages = numOfMessages;
        }

        @Override
        public void run() {
            //spamming itself, such a noob
            for(int i=0; i<numOfMessages; i++) {
                ref.send(new StoreMessage("What about a little spam?"), ref);
            }
        }
    }

    @Test
    public void StoreActorShouldStandHeavyTraffic(){

        TestActorRef ref = new TestActorRef(system.actorOf(StoreActor.class));
        StoreActor actor = (StoreActor) ref.getUnderlyingActor(system);
        //creating 3 spammers
        Thread t1 = new Thread(new HeavySpammer(ref,10000));
        Thread t2 = new Thread(new HeavySpammer(ref,100000));
        Thread t3 = new Thread(new HeavySpammer(ref,1000000));
        //launching the spammers
        t1.start();
        t2.start();
        t3.start();
        try {
            //giving some time to elaborate the messages
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Assert.assertEquals("Spammers caused no effects", true, actor.isEmptyMailBox());



    }
}
