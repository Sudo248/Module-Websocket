package org.sudo248.frames;

import org.sudo248.utils.CharsetUtils;
import org.sudo248.common.Opcode;
import org.sudo248.exceptions.InvalidDataException;
import org.sudo248.exceptions.InvalidFrameException;
import org.sudo248.utils.ByteBufferUtils;

import java.nio.ByteBuffer;
import java.util.Objects;

/**
 * Class to represent a close frame
 */
public class CloseFrame extends ControlFrame {

    /**
     * indicates a normal closure, meaning whatever purpose the connection was established for has
     * been fulfilled.
     */
    public static final int NORMAL = 1000;

    /**
     * 1001 indicates that an endpoint is "going away", such as a server going down, or a browser
     * having navigated away from a page.
     */
    public static final int GOING_AWAY = 1001;

    /**
     * 1002 indicates that an endpoint is terminating the connection due to a protocol error.
     */
    public static final int PROTOCOL_ERROR = 1002;

    /**
     * 1003 indicates that an endpoint is terminating the connection because it has received a type of
     * data it cannot accept (e.g. an endpoint that understands only text data MAY send this if it
     * receives a binary message).
     */
    public static final int REFUSE = 1003;

    /**
     * 1005 is a reserved value and MUST NOT be set as a status code in a Close control frame by an
     * endpoint. It is designated for use in applications expecting a status code to indicate that no
     * status code was actually present.
     */
    public static final int NO_CODE = 1005;

    /**
     * 1006 is a reserved value and MUST NOT be set as a status code in a Close control frame by an
     * endpoint. It is designated for use in applications expecting a status code to indicate that the
     * connection was closed abnormally, e.g. without sending or receiving a Close control frame.
     */
    public static final int ABNORMAL_CLOSE = 1006;

    /**
     * 1007 indicates that an endpoint is terminating the connection because it has received data
     * within a message that was not consistent with the type of the message (e.g., non-UTF-8
     * [RFC3629] data within a text message).
     */
    public static final int NO_UTF8 = 1007;

    /**
     * 1008 indicates that an endpoint is terminating the connection because it has received a message
     * that violates its policy. This is a generic status code that can be returned when there is no
     * other more suitable status code (e.g. 1003 or 1009), or if there is a need to hide specific
     * details about the policy.
     */
    public static final int POLICY_VALIDATION = 1008;

    /**
     * 1009 indicates that an endpoint is terminating the connection because it has received a message
     * which is too big for it to process.
     */
    public static final int TOO_BIG = 1009;

    /**
     * 1010 indicates that an endpoint (client) is terminating the connection because it has expected
     * the server to negotiate one or more extension, but the server didn't return them in the
     * response message of the WebSocket handshake. The list of extensions which are needed SHOULD
     * appear in the /reason/ part of the Close frame. Note that this status code is not used by the
     * server, because it can fail the WebSocket handshake instead.
     */
    public static final int EXTENSION = 1010;

    /**
     * 1011 indicates that a server is terminating the connection because it encountered an unexpected
     * condition that prevented it from fulfilling the request.
     **/
    public static final int UNEXPECTED_CONDITION = 1011;

    /**
     * 1012 indicates that the service is restarted. A client may reconnect, and if it choses to do,
     * should reconnect using a randomized delay of 5 - 30s. See https://www.ietf.org/mail-archive/web/hybi/current/msg09670.html
     * for more information.
     *
     **/
    public static final int SERVICE_RESTART = 1012;

    /**
     * 1013 indicates that the service is experiencing overload. A client should only connect to a
     * different IP (when there are multiple for the target) or reconnect to the same IP upon user
     * action. See https://www.ietf.org/mail-archive/web/hybi/current/msg09670.html for more
     * information.
     *
     **/
    public static final int TRY_AGAIN_LATER = 1013;

    /**
     * 1014 indicates that the server was acting as a gateway or proxy and received an invalid
     * response from the upstream server. This is similar to 502 HTTP Status Code See
     * https://www.ietf.org/mail-archive/web/hybi/current/msg10748.html fore more information.
     *
     **/
    public static final int BAD_GATEWAY = 1014;

    /**
     * 1015 is a reserved value and MUST NOT be set as a status code in a Close control frame by an
     * endpoint. It is designated for use in applications expecting a status code to indicate that the
     * connection was closed due to a failure to perform a TLS handshake (e.g., the server certificate
     * can't be verified).
     **/
    public static final int TLS_ERROR = 1015;

    /**
     * 1016 indicates that an endpoint is terminating the connection because it has received data
     * within a serializable that was not consistent with the type of the Object
     */
    public static final int NOT_SERIALIZABLE = 1016;

    /**
     * The connection had never been established
     */
    public static final int NEVER_CONNECTED = -1;

    /**
     * The connection had a buggy close (this should not happen)
     */
    public static final int BUGGY_CLOSE = -2;

    /**
     * The connection was flushed and closed
     */
    public static final int FLASH_POLICY = -3;

    /**
     * The close code used in this close frame
     */
    private int code;

    /**
     * The close message used in this close frame
     */
    private String reason;

    /**
     * Constructor for a close frame
     * <p>
     * Using opcode closing and fin = true
     */
    public CloseFrame() {
        super(Opcode.CLOSING);
        setReason("");
        setCode(CloseFrame.NORMAL);
    }

    /**
     * Set the close code for this close frame
     *
     * @param code the close code
     */
    public void setCode(int code) {
        this.code = code;
        if (code == TLS_ERROR) {
            this.code = NO_CODE;
            this.reason = "";
        }
        updatePayload();
    }

    /**
     * Set the close reason for this close frame
     *
     * @param reason the reason code
     */
    public void setReason(String reason) {
        if (reason == null) {
            reason = "";
        }
        this.reason = reason;
        updatePayload();
    }

    /**
     * Get the used close code
     *
     * @return the used close code
     */
    public int getCloseCode() {
        return code;
    }

    /**
     * Get the message that closeframe is containing
     *
     * @return the message in this frame
     */
    public String getMessage() {
        return reason;
    }

    @Override
    public String toString() {
        return super.toString() + "code: " + code;
    }

    @Override
    public void isValid() throws InvalidDataException {
        super.isValid();
        if (code == NO_UTF8 && reason.isEmpty()) {
            throw new InvalidDataException(NO_UTF8, "Received text is no valid utf8 string!");
        }
        if (code == NO_CODE && !reason.isEmpty()) {
            throw new InvalidDataException(PROTOCOL_ERROR, "A close frame must have a closecode if it has a reason");
        }
        if (code > TLS_ERROR && code < 3000) {
            throw new InvalidDataException(PROTOCOL_ERROR, "Trying to send an illegal close code!");
        }
        if (code == ABNORMAL_CLOSE || code == TLS_ERROR || code == NO_CODE || code > 4999 || code < 1000 || code == 1004) {
            throw new InvalidFrameException("closecode must not be sent over the wire: " + code);
        }
    }

    @Override
    public void setPayload(ByteBuffer payload) {
        code = NO_CODE;
        reason = "";
        payload.mark();
        if (payload.remaining() == 0) {
            code = CloseFrame.NORMAL;
        } else if (payload.remaining() == 1) {
            code = CloseFrame.PROTOCOL_ERROR;
        } else {
            if (payload.remaining() >= 2) {
                ByteBuffer bb = ByteBuffer.allocate(4);
                bb.position(2);
                bb.putShort(payload.getShort());
                bb.position(0);
                code = bb.getInt();
            }
            payload.reset();
            try {
                int mark = payload.position();// because stringUtf8 also creates a mark
                validateUtf8(payload, mark);
            } catch (InvalidDataException e) {
                code = CloseFrame.NO_UTF8;
                reason = null;
            }
        }
    }

    @Override
    public ByteBuffer getPayloadData() {
        if (code == NO_CODE) {
            return ByteBufferUtils.getEmptyByteBuffer();
        }
        return super.getPayloadData();
    }

    /**
     * Validate the payload to valid utf8
     *
     * @param mark    the current mark
     * @param payload the current payload
     * @throws InvalidDataException the current payload is not a valid utf8
     */
    private void validateUtf8(ByteBuffer payload, int mark) throws InvalidDataException {
        try {
            payload.position(payload.position() + 2);
            reason = CharsetUtils.stringUtf8(payload);
        } catch (IllegalArgumentException e) {
            throw new InvalidDataException(CloseFrame.NO_UTF8);
        } finally {
            payload.position(mark);
        }
    }

    /**
     * Update the payload to represent the close code and the reason
     */
    private void updatePayload() {
        byte[] by = CharsetUtils.utf8Bytes(reason);
        ByteBuffer buf = ByteBuffer.allocate(4);
        buf.putInt(code);
        buf.position(2);
        ByteBuffer pay = ByteBuffer.allocate(2 + by.length);
        pay.put(buf);
        pay.put(by);
        pay.rewind();
        super.setPayload(pay);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        CloseFrame that = (CloseFrame) o;

        if (code != that.code) {
            return false;
        }
        return Objects.equals(reason, that.reason);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + code;
        result = 31 * result + (reason != null ? reason.hashCode() : 0);
        return result;
    }
}
