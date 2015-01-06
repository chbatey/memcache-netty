package info.batey.netty

import spock.lang.Specification

/**
 * Created by chbatey on 03/01/2015.
 */
class MemcacheSpec extends Specification {

    private OutputStream outputStream
    private DataInputStream inputStream


    def setup() {
        Socket socket = new Socket("localhost", 9090)
        inputStream = new DataInputStream(socket.inputStream)
        outputStream = socket.outputStream
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

    private void sendDataForKey(String key, byte[] bytes) {
        outputStream.write("set ${key} 0 100 ${bytes.length}\r\n".getBytes())
        outputStream.write(bytes)
        outputStream.write("\r\n".getBytes())
        inputStream.readLine()
    }
}
