package info.batey.netty.handlers;

import info.batey.netty.messages.StorageCommand;

public class MemcacheAddMessage {
    private final StorageCommand storageCommand;

    public MemcacheAddMessage(StorageCommand storageCommand) {
        this.storageCommand = storageCommand;
    }

    public byte[] getData() {
        return storageCommand.getData();
    }

    public String getKey() {
        return storageCommand.getKey();
    }

    public int getTtl() {
        return storageCommand.getTtl();
    }

    @Override
    public String toString() {
        return "MemcacheAddMethod{" +
                "storageCommand=" + storageCommand +
                '}';
    }
}
