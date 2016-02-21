package it.unipd.math.pcd.actors.utils.messages;

import it.unipd.math.pcd.actors.Message;

/**
 * Created by federico on 11/02/16.
 */
public class SlowMessage implements Message{
    public SlowMessage(long m){
        try {
            Thread.sleep(m);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
