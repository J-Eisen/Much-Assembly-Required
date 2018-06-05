package net.simon987.server.game.item;

import org.bson.Document;

public class ItemIron extends Item {

    public static final int ID = 0x0004;

    @Override
    public int getId() {
        return ID;
    }

    public ItemIron(Document document) {
        super(document);
    }

    @Override
    public char poll() {
        return ID;
    }
}
