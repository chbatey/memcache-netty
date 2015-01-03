package info.batey.netty

import spock.lang.Specification

/**
 * Created by chbatey on 03/01/2015.
 */
class MemcacheSpec extends Specification {

    private OutputStream outputStream
    private BufferedReader inputStream


    def setup() {
        Socket socket = new Socket("localhost", 9090)
        inputStream = new BufferedReader(new InputStreamReader(socket.inputStream))
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
}
