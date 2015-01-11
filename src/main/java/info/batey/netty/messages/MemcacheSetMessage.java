package info.batey.netty.messages;

public class MemcacheSetMessage {
    private final StorageCommand storageCommand;

    public MemcacheSetMessage(StorageCommand storageCommand) {
        this.storageCommand = storageCommand;
    }

    @Override
    public String toString() {
        return "MemcacheSetMessage{" +
                "storageCommand=" + storageCommand +
                '}';
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
}
