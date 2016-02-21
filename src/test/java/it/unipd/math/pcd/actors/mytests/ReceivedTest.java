package it.unipd.math.pcd.actors.mytests;

import it.unipd.math.pcd.actors.ActorSystem;
import it.unipd.math.pcd.actors.Message;
import it.unipd.math.pcd.actors.TestActorRef;
import it.unipd.math.pcd.actors.exceptions.UnsupportedMessageException;
import it.unipd.math.pcd.actors.implementation.ActorSystemImpl;
import it.unipd.math.pcd.actors.utils.actors.StoreActor;
import it.unipd.math.pcd.actors.utils.messages.SlowMessage;
import it.unipd.math.pcd.actors.utils.messages.StoreMessage;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by federico on 10/02/16.
 */
public class ReceivedTest {
    private ActorSystem system;

    /**
     * Initializes the {@code system} with a concrete implementation before each test.
     */
    @Before
    public void init() {
        this.system = new ActorSystemImpl();
    }

    @Test
    public void shouldSendMessageAndFlushMailBox() throws InterruptedException {
        TestActorRef ref = new TestActorRef(system.actorOf(StoreActor.class));
        StoreActor actor = (StoreActor) ref.getUnderlyingActor(system);
        //sending a message
        ref.send(new StoreMessage("Hello World!"), ref);
        Assert.assertEquals("The mailbox has something inside", false, actor.isEmptyMailBox());
        //flushing mailbox
        actor.flushMailbox();
        Assert.assertEquals("The mailbox is empty", true, actor.isEmptyMailBox());

    }
}
