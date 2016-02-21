package it.unipd.math.pcd.actors.utils.actors;

import it.unipd.math.pcd.actors.AbsActor;
import it.unipd.math.pcd.actors.exceptions.UnsupportedMessageException;
import it.unipd.math.pcd.actors.utils.messages.SlowMessage;

/**
 * Created by federico on 11/02/16.
 */
public class SlowActor extends AbsActor<SlowMessage> {
    @Override
    public void receive(SlowMessage message) throws UnsupportedMessageException {
        //do nothing
    }
}
