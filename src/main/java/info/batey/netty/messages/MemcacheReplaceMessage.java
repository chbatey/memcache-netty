package info.batey.netty.messages;

public class MemcacheReplaceMessage {

    private final StorageCommand storageCommand;

    public MemcacheReplaceMessage(StorageCommand storageCommand) {
        this.storageCommand = storageCommand;
    }

    @Override
    public String toString() {
        return "MemcacheReplaceMessage{" +
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
