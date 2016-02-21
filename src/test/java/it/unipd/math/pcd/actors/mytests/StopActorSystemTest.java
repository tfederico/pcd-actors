package it.unipd.math.pcd.actors.mytests;

import it.unipd.math.pcd.actors.ActorRef;
import it.unipd.math.pcd.actors.ActorSystem;
import it.unipd.math.pcd.actors.exceptions.NoSuchActorException;
import it.unipd.math.pcd.actors.utils.ActorSystemFactory;
import it.unipd.math.pcd.actors.utils.actors.TrivialActor;
import it.unipd.math.pcd.actors.utils.messages.TrivialMessage;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by federico on 10/02/16.
 */
public class StopActorSystemTest {
    private ActorSystem system;

    /**
     * Initializes the {@code system} with a concrete implementation before each test.
     */
    @Before
    public void init() {
        system = ActorSystemFactory.buildActorSystem();
    }

    @Test(expected = NoSuchActorException.class)
    public void shouldStopAllActorsAndTheyShouldNotBeStoppedTwice() {
        ActorRef ref1 = system.actorOf(TrivialActor.class);

        for(int i = 0; i < 50; i++)
            ref1.send(new TrivialMessage(), ref1);

        system.stop();
        system.stop(ref1);
    }

    @Test(expected = NoSuchActorException.class)
    public void shouldStopAllActorsAndTheyShouldNotBeAbleToReceiveNewMessages() {
        ActorRef ref1 = system.actorOf(TrivialActor.class);
        
        for(int i = 0; i < 50; i++)
            ref1.send(new TrivialMessage(), ref1);

        system.stop();
        ref1.send(new TrivialMessage(), ref1);
    }
}
