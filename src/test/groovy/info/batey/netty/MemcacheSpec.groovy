package info.batey.netty

import spock.lang.Shared
import spock.lang.Specification

class MemcacheSpec extends Specification {

    public static final int PORT = 9091
    private OutputStream outputStream
    private DataInputStream inputStream

    @Shared
    private static Thread threadForMemcacheServer;


    def setup() {
//        Socket socket = new Socket("192.168.2.2", 11211)
        Socket socket = new Socket("localhost", PORT)
        inputStream = new DataInputStream(socket.inputStream)
        outputStream = socket.outputStream
    }

    def setupSpec() {
        threadForMemcacheServer = new Thread(new MemcacheServer(PORT))
        threadForMemcacheServer.start()
        Thread.sleep(1000)
    }

    def cleanupSpec() {
        threadForMemcacheServer.interrupt()
    }

    def "Store should return STORED"() {
        when:
        outputStream.write("set batey 0 100 2\r\n".getBytes())
        byte[] bytes = [1, 2]
        outputStream.write(bytes)
        outputStream.write("\r\n".getBytes())
        def line = inputStream.readLine()

        then:
        line == "STORED"
    }

    def "Get of a key that does not exist"() {
        when:
        outputStream.write("get noexist\r\n".getBytes())
        def line = inputStream.readLine()

        then:
        line == "END"
    }

    def "Save and get some data"() {
        given:
        byte[] bytes = [1, 2]
        String key = "batey"
        sendDataForKey(key, bytes)

        when:
        outputStream.write("get ${key}\r\n".getBytes())
        def valueLine = inputStream.readLine()
        byte[] dataBack = new byte[2]
        inputStream.read(dataBack)
        inputStream.readLine()
        def end = inputStream.readLine()

        then:
        valueLine == "VALUE batey 0 2"
        dataBack == bytes
        end == "END"
    }

    def "Should allow \r\n in the data"() {
        given:
        byte[] bytes = [1, 2, (byte) '\r', (byte)'\n']
        String key = "batey"
        sendDataForKey(key, bytes)

        when:
        outputStream.write("get ${key}\r\n".getBytes())
        def valueLine = inputStream.readLine()
        byte[] dataBack = new byte[4]
        inputStream.read(dataBack)
        inputStream.readLine()
        def end = inputStream.readLine()

        then:
        valueLine == "VALUE batey 0 4"
        dataBack == bytes
        end == "END"
    }

    def "replace should store nothing if key not already present"() {
        when:
        outputStream.write("replace reallynotexist 0 100 2\r\n".getBytes())
        byte[] bytes = [1, 2]
        outputStream.write(bytes)
        outputStream.write("\r\n".getBytes())
        def line = inputStream.readLine()

        then:
        line == "NOT_STORED"
    }

    def "replace should store if key already present"() {
        given:
        def key = "there"
        byte[] bytes = [1, 2]
        byte[] replacedBytes = [3, 4]
        sendDataForKey(key, bytes)

        when:
        outputStream.write("replace ${key} 0 100 2\r\n".getBytes())
        outputStream.write(replacedBytes)
        outputStream.write("\r\n".getBytes())
        def line = inputStream.readLine()
        def dataBack = getDataForKey(key)

        then:
        line == "STORED"
        dataBack == replacedBytes
    }

    private byte[] getDataForKey(String key) {
        outputStream.write("get ${key}\r\n".getBytes())
        def valueLine = new ValueLine(inputStream.readLine())
        byte[] dataBack = new byte[valueLine.numberOfBytes]
        inputStream.read(dataBack)
        inputStream.readLine()
        def end = inputStream.readLine()
        return dataBack
    }

    private static class ValueLine {
        private final String key;
        private final String flags;
        private final int numberOfBytes;

        ValueLine(String line) {
            String[] array = line.split(" ");
            key = array[1]
            flags = array[2]
            numberOfBytes = Integer.parseInt(array[3])
        }
    }

    private void sendDataForKey(String key, byte[] bytes) {
        outputStream.write("set ${key} 0 100 ${bytes.length}\r\n".getBytes())
        outputStream.write(bytes)
        outputStream.write("\r\n".getBytes())
        inputStream.readLine()
    }
}
